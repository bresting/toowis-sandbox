package com.toowis.tomcat;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.crypto.NoSuchPaddingException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;

public class EncryptedDataSourceFactoryDbcp extends BasicDataSourceFactory {
    private Encryptor encryptor = null;
    public EncryptedDataSourceFactoryDbcp() {
        Object key = System.getProperties().get("datasource.secret.key");
        try {
            if (key == null) {
                encryptor = new Encryptor();
            } else {
                encryptor = new Encryptor(key.toString());
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException e) {
            System.err.println("Error instantiating decryption class." + e);
            throw new RuntimeException(e);
        }
    }
    
    // 첫번째 방법
    // @Override
    // public Object getObjectInstance(final Object obj, final Name name, final Context nameCtx, final Hashtable<?, ?> environment) throws Exception {
    //     if (obj == null || !(obj instanceof Reference)) {
    //         return null;
    //     }
    //     
    //     final Reference ref = (Reference) obj;
    //     if (!"javax.sql.DataSource".equals(ref.getClassName())) {
    //         return null;
    //     }
    //     
    //     Reference newRef = new Reference(ref.getClassName());
    //     final Enumeration<RefAddr> allRefAddrs = ref.getAll();
    //     while (allRefAddrs.hasMoreElements()) {
    //         RefAddr refAddr = allRefAddrs.nextElement();
    //         String key = refAddr.getType();
    //         String val = refAddr.getContent().toString();
    //         
    //         if ("password".equals( key )) {
    //             val = encryptor.decrypt(val);
    //         }
    //         
    //         newRef.add(new StringRefAddr(key, val));
    //     }
    //     return super.getObjectInstance(newRef, name, nameCtx, environment);
    // }
    
    @Override
    public Object getObjectInstance(final Object obj, final Name name, final Context nameCtx, final Hashtable<?, ?> environment) throws Exception {
        if ( obj instanceof Reference ) {
            decrypt( (Reference) obj, "password");
        }
        return super.getObjectInstance(obj, name, nameCtx, environment);
    }
    
    private void decrypt(Reference ref, String refType) throws Exception {
        int posn = position(ref, refType);
        if (posn == -1) {
            return;
        }
        // replace encrypt value to decrypt value
        RefAddr removedAddr = (RefAddr) ref.remove(posn);
        ref.add(posn, new StringRefAddr( refType, encryptor.decrypt( removedAddr.getContent().toString() ) ) );
    }
    
    private int position(Reference ref, String type) {
        final Enumeration<RefAddr> allRefAddrs = ref.getAll();
        for (int i = 0; allRefAddrs.hasMoreElements(); i++) {
            RefAddr refAddr = allRefAddrs.nextElement();
            if (refAddr.getType().equals(type)) {
                return i;
            }
        }
        return -1;
    }
}