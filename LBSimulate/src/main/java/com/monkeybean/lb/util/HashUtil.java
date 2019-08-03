package com.monkeybean.lb.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by MonkeyBean on 2019/8/2.
 */
public class HashUtil {

    /**
     * 校验Md5
     *
     * @param originStr 原始字符串
     * @return 成功则返回MD5字符串小写形式, 失败返回null
     */
    public static String checkMd5(String originStr) {
        if (originStr == null || "".equals(originStr)) {
            return null;
        }
        byte[] originByteArray = originStr.getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md.update(originByteArray);
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }
}
