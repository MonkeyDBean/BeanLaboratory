package com.monkeybean.labo.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

/**
 * 通用加解密或Hash算法
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class Coder {
    private static final String KEY_SHA = "SHA";
    private static final String KEY_MD5 = "MD5";
    private static final String KEY_MAC = "HmacMD5";
    private static Logger logger = LoggerFactory.getLogger(Coder.class);
    private static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private Coder() {
    }

    /**
     * 对字节数组做md5散列
     */
    public static String getMd5(byte[] msg) throws Exception {
        MessageDigest md = MessageDigest.getInstance(KEY_MD5);
        md.update(msg);
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }

    /**
     * 账户密码，DES算法盐作为密钥加密
     *
     * @param passwordMd5 未des加密的字符串
     * @param key         密钥
     */
    public static String encryptPassWithSlat(String passwordMd5, String key) {
        String desPassword = "";
        try {
            byte[] enc = DesUtil.encrypt(passwordMd5.getBytes(), key);
            //加密结果需要转换成hex才能存入数据库
            desPassword = Hex.encodeHexString(enc);
        } catch (Exception e) {
            logger.error("Coder, encryptPassWithSlat error: [{}]", e);
        }
        return desPassword;
    }

    /**
     * 账户密码，DES算法盐作为密钥解密
     *
     * @param desPassword des加密的密码
     * @param key         密钥
     */
    public static String decryptPsWithSlat(String desPassword, String key) {
        String passwordMd5 = "";
        try {
            //desPassword是hex格式，应该先转换
            byte[] hex = Hex.decodeHex(desPassword.toCharArray());
            byte[] dec = DesUtil.decrypt(hex, key);
            passwordMd5 = new String(dec);
        } catch (Exception e) {
            logger.error("decryptPsWithSlat error: [{}]", e);
        }
        return passwordMd5;
    }

    /**
     * 密码进行n次md5运算，对比结果是否正确
     *
     * @param originPassword  初始密码
     * @param predictPassword 预期结果
     * @param loop            md5次数
     * @return 正确则返回true
     */
    public static boolean manyMd5PasswordCompare(String originPassword, String predictPassword, int loop) {
        for (int i = 0; i < loop; i++) {
            originPassword = DigestUtils.md5Hex(originPassword);
        }
        return originPassword.equals(predictPassword);
    }

    /**
     * 16进制编码
     */
    public static String encryptHex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 16进制解码
     */
    public static byte[] decryptHex(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * BASE64编码
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder().encodeBuffer(key));
    }

    /**
     * BASE64解码
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * MD5转为字符串再进行Base64编码
     *
     * @param origin 明文
     * @return 编码后的密码
     */
    public static String encodeMd5StrAndBase64(String origin) {
        String result = "";
        try {
            MessageDigest md;
            md = MessageDigest.getInstance(KEY_MD5);
            md.update(origin.getBytes());

            //base64调用封装
            result = encryptBASE64(encryptHex(md.digest()).getBytes());

            //base64使用方法库
            //result = new String(Base64.encodeBase64(encryptHex(md.digest()).getBytes()));
        } catch (Exception e) {
            logger.error("Exception:-> [{}]", e);
        }
        return result;
    }

    /**
     * MD5字节数组直接进行Base64编码(与常规MD5转为字符串再进行Base64编码不同)
     *
     * @param origin 明文
     * @return 编码后的密码
     */
    public static String encodeMd5ByteAndBase64(String origin) {
        String result = "";
        try {
            MessageDigest md;
            md = MessageDigest.getInstance(KEY_MD5);
            md.update(origin.getBytes());
            result = new String(Base64.encodeBase64(md.digest()));
        } catch (Exception e) {
            logger.error("Exception:-> [{}]", e);
        }
        return result;
    }

    /**
     * SHA编码
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

    /**
     * 生成HmacMD5密钥
     */
    public static String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }

    /**
     * HMAC加密
     */
    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

}
