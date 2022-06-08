package com.toowis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
 
public class RSAManager {
    
    public static final String privateKey = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAwQMZ0WIu5D2IkZ/7k9UwYAMoB+PogKcqwbFHymhAsUned+V6oC6ElU4GOHts4WkhkQh35qkK4lQYIOto29AWeQIDAQABAkAIoo7hIzdd1rLpcPLcZklHwlxkHfok51WuXLQNhiR+ye8+3v7USF4fHiIm8MOAMdNIDpc4LDL7ItHcZoqxakFBAiEA9pRIMSrQqcFXooUaQnPx2u9T95uT56fm0rIG2FMWKZUCIQDIYuUVe7kl9iqqgS6pHx5DUvXfvxWPjb2C+Bzfd+EoVQIgaVonkiJJ7w21hLG764KgZjt1M8jcI9EgFQuNUyYExRUCIAFuU1x36baPr3ZQPdkPU9P/P6o9XxYLWRMaWnpuDLmRAiB/aEPOyKjBstMBnn15Ndp86UG3K/LJDLw5DiL/vr0SEg==";
    public static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMEDGdFiLuQ9iJGf+5PVMGADKAfj6ICnKsGxR8poQLFJ3nfleqAuhJVOBjh7bOFpIZEId+apCuJUGCDraNvQFnkCAwEAAQ==";
    
    /**
     * 키 생성
     * @return
     */
    public static void createKey() {

        PublicKey publicKey = null;
        PrivateKey privateKey = null;

        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);

            KeyPair keyPair = keyPairGenerator.genKeyPair();
            publicKey  = keyPair.getPublic ();
            privateKey = keyPair.getPrivate();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaPublicKeySpec   = keyFactory.getKeySpec(publicKey , RSAPublicKeySpec.class );
            RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);

            System.out.println("Public  key modulus : " + rsaPublicKeySpec.getModulus());
            System.out.println("Public  key exponent: " + rsaPublicKeySpec.getPublicExponent());
            
            System.out.println("Private key modulus : " + rsaPrivateKeySpec.getModulus());
            System.out.println("Private key exponent: " + rsaPrivateKeySpec.getPrivateExponent());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("키 알고리즘 에러", e);
        }
        
        // 공개키와 개인키를 String으로 변환하여 파일에 저장
        byte[] bPublicKey = publicKey.getEncoded();
        String sPublicKey = Base64.getEncoder().encodeToString(bPublicKey);

        byte[] bPrivateKey = privateKey.getEncoded();
        String sPrivateKey = Base64.getEncoder().encodeToString(bPrivateKey);
        
        // 파일생성
        try ( BufferedWriter bw1 = new BufferedWriter(new FileWriter("PublicKey.txt" ))
            ; BufferedWriter bw2 = new BufferedWriter(new FileWriter("PrivateKey.txt"))
        ) {
            bw1.write(sPublicKey);
            bw1.newLine();
            
            bw2.write(sPrivateKey);
            bw2.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일처리 에러", e);
        }
    }
    
    /**
     * 파일내용 가져오기
     * @param path - "PublicKey.txt" / "PrivateKey.txt"
     * @return
     */
    public static String readFile(String path) {
        String value = null;
        try ( BufferedReader br = new BufferedReader(new FileReader(path)) ) {
            value = br.readLine();
            System.out.println("path file read");
        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("파일 처리 오류", e);
        }
        return value;
    }
    
    /**
     * 공개키로 평문을 암호화 한다.
     * @param plain
     * @return
     * @throws IOException
     */
    public static String encrypt(String plain) {
        return encrypt(plain, publicKey);
    }
    public static String encrypt(String plain, String key) {
        
        // 디코딩
        byte[] bPublicKey = Base64.getDecoder().decode(key.getBytes());
        
        // 디코딩된 공개키정보 PublicKey에 저장
        PublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bPublicKey);
            publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("키 알고리즘 에러", e);
        }
        
        String sCipherBase64 = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bCipher = cipher.doFinal(plain.getBytes());
            sCipherBase64 = Base64.getEncoder().encodeToString(bCipher);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("키 알고리즘 에러", e);
        }
        
        return sCipherBase64;
    }
    
    public static String decrypt(String sCipherBase64) {
        return decrypt(sCipherBase64, privateKey);
    }
    /**
     * 개인키로 암호문을 평문화 하다.
     * @param sCipherBase64
     * @return
     */
    public static String decrypt(String sCipherBase64, String key) {
        
        byte[] bPrivateKey = Base64.getDecoder().decode(key.getBytes());
        
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bPrivateKey);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("키 알고리즘 에러", e);
        }

        // 개인키 이용 복호화
        String plainText = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            byte[] bCipher = Base64.getDecoder().decode(sCipherBase64.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bPlain = cipher.doFinal(bCipher);
            plainText = new String(bPlain);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("키 알고리즘 에러", e);
        }

        return plainText;
    }
}