package com.monkeybean.labo.util;

import junit.framework.TestCase;

import java.util.Map;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class RSACoderTest extends TestCase {

    /**
     * 私钥签名，公钥验签
     */
    public void testSignVerify() throws Exception {
        Map<String, Object> key = RSACoder.initKey();
        String publicKey = RSACoder.getPublicKey(key);
        String privateKey = RSACoder.getPrivateKey(key);
        String data = "justTestData";
        String sign = RSACoder.sign(data.getBytes(), privateKey);
        System.out.println("publicKey: " + publicKey);
        System.out.println("publicKey length: " + publicKey.length());
        System.out.println("privateKey: " + privateKey);
        System.out.println("privateKey length: " + privateKey.length());
        System.out.println("sign: " + sign);
        System.out.println("sign length: " + sign.length());
//        RSACoder.verify(data.getBytes("ISO-8859-1"), publicKey, sign);
        RSACoder.verify(data.getBytes("UTF-8"), publicKey, sign);
        assertTrue(RSACoder.verify(data.getBytes(), publicKey, sign));
    }

    /**
     * 私钥加密，公钥解密
     */
    public void testPrivateEncryt() throws Exception {
        Map<String, Object> key = RSACoder.initKey();
        String publicKey = RSACoder.getPublicKey(key);
        String privateKey = RSACoder.getPrivateKey(key);
        String data = "testDataPrivate";
        byte[] encryptDataByte = RSACoder.encryptByPrivateKey(data.getBytes(), privateKey);
        byte[] decryDataByte = RSACoder.decryptByPublicKey(encryptDataByte, publicKey);
        String decryData = new String(decryDataByte);
        assertTrue(decryData.equals(data));
    }

    /**
     * 公钥加密，私钥解密
     */
    public void testPublicEncryt() throws Exception {
        Map<String, Object> key = RSACoder.initKey();
        String publicKey = RSACoder.getPublicKey(key);
        String privateKey = RSACoder.getPrivateKey(key);
        String data = "testDataPublic";
        byte[] encryptDataByte = RSACoder.encryptByPublicKey(data.getBytes(), publicKey);
        byte[] decryDataByte = RSACoder.decryptByPrivateKey(encryptDataByte, privateKey);
        String decryData = new String(decryDataByte);
        assertTrue(decryData.equals(data));
    }
}