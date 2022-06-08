package com.toowis;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SimpleTest {
    
    @Test
    @DisplayName("키파일 생성")
    public void createKeyTest() {
        RSAManager.createKey();
    }
    
    @Test
    @DisplayName("암복호화 테스트")
    public void RSATest() {
        String sPlain = "Welcome to RSA";
        String encStr = RSAManager.encrypt(sPlain);
        String decStr = RSAManager.decrypt(encStr);

        System.out.println("평문원본 : " + sPlain);
        System.out.println("암호화문 : " + encStr);
        System.out.println("복호화문 : " + decStr);
    }
    
    @Test
    @DisplayName("테스트")
    public void simpleTest() {
        /**
         * Modulus(모듈)은 공개키, 개인키 동일하다.
         * 
         * 공개키 Exponent(지수), Modulus(모듈) 공개한다.
         * 개인키 Exponent(지수)                숨겨둔다.
         * 
         * - 역원
         */
        
        // 공개키
        BigInteger modulus  = new BigInteger("11117027828089898525403417442706371659427451289475355133764283436519852839107647346863137772653073461576018869702619855331959453720602494170662869634399941");
        BigInteger exponent = new BigInteger("65537");

        // 개인키
        BigInteger privateExponent = new BigInteger("4982875207137048830183337464020319323369720640681425104205652165155113556445686063609598527705184026245620710450305348480626220630689705672123288072893473");
        
        // 평문
        String m = String.valueOf(65);
        
        // 공개키로 암호화 -> 개인키로 복호화
        BigInteger enc = new BigInteger(m).modPow(exponent, modulus);
        BigInteger dec = enc.modPow(privateExponent, modulus);
        
        System.out.println(enc);
        System.out.println(dec);
        
        System.out.println("----------------------------------------");
        
        // 개인키로 암호화 -> 공개키로 복호화
        BigInteger enc2 = new BigInteger(m).modPow(privateExponent, modulus);
        BigInteger dec2 = enc2.modPow(exponent, modulus);
        System.out.println(enc2);
        System.out.println(dec2);
    }
}
