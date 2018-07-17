package com.monkeybean.labo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * Des加解密工具
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class DesUtil {
    private static Logger logger = LoggerFactory.getLogger(DesUtil.class);

    /**
     * 加密
     *
     * @param src byte[]
     * @param key String
     * @return byte[]
     */
    public static byte[] encrypt(byte[] src, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secureKey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(src);
        } catch (Throwable e) {
            logger.error("encrypt Throwable error: {}", e);
        }
        return null;
    }

    /**
     * DES解密
     *
     * @param src byte[]
     * @param key String
     * @return byte[]
     */
    public static byte[] decrypt(byte[] src, String key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey secureKey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secureKey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

}
