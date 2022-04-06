package com.toowis.tomcat;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.Context;
import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.XADataSource;

public class EncryptedDataSourceFactory extends DataSourceFactory {
    private Encryptor encryptor = null;
    public EncryptedDataSourceFactory() {
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

    @Override
    public DataSource createDataSource(Properties properties, Context context, boolean XA) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SQLException, NoSuchAlgorithmException, NoSuchPaddingException {
        
        PoolConfiguration poolProperties = EncryptedDataSourceFactory.parsePoolProperties(properties);
        poolProperties.setPassword(encryptor.decrypt(poolProperties.getPassword()));
        if (poolProperties.getDataSourceJNDI() != null && poolProperties.getDataSource() == null) {
            performJNDILookup(context, poolProperties);
        }
        org.apache.tomcat.jdbc.pool.DataSource dataSource = XA ? new XADataSource(poolProperties) : new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
        dataSource.createPool();
        return dataSource;
    }
}