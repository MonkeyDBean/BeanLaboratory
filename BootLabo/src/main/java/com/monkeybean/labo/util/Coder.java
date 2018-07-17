package com.monkeybean.labo.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

/**
 * 通用加解密或Hash算法
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class Coder {
    private static Logger logger = LoggerFactory.getLogger(Coder.class);

    private static char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

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
            logger.error("Coder, encryptPassWithSlat error: {}", e);
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
            logger.error("decryptPsWithSlat error: {}", e);
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
    public static boolean nMd5PasswordCompare(String originPassword, String predictPassword, int loop) {
        for (int i = 0; i < loop; i++) {
            originPassword = DigestUtils.md5Hex(originPassword);
        }
        return originPassword.equals(predictPassword);
    }

    /**
     * Md5字节数组转换为16进制字符串格式
     *
     * @param msg 字节数组
     * @return md5 16进制字符串
     * @throws Exception NoSuchAlgorithm
     */
    public static String convertMd5Hex(byte[] msg) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(msg);
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }

    /**
     * 16进制加密
     */
    public static String encryptHex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_CHAR[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_CHAR[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 16进制解密
     */
    public static byte[] decryptHex(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

}
