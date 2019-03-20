package com.monkeybean.labo.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 文件相关操作
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class FileCommonUtil {

    /**
     * 文件大小合法性判断
     *
     * @param length  文件字节长度
     * @param maxSize 文件最大字节长度限制，若为1以下则不限制大小
     * @return true为合法
     */
    public static boolean isFileSizeLegal(int length, Double maxSize) {
        Double size = length / 1024.;
        return maxSize <= 1 || maxSize - size > 0.001;
    }

    /**
     * 生成文件存储路径
     *
     * @param rootPath   根路径
     * @param hash       文件hash
     * @param nameSuffix 文件名后缀
     */
    public static String generateMappingPath(String rootPath, String hash, String nameSuffix) {
        if (StringUtils.isEmpty(hash) || hash.length() < 32) {
            return "";
        }
        String filePath = rootPath + File.separator
                + hash.substring(0, 3) + File.separator
                + hash.substring(3, 6) + File.separator
                + hash.substring(6, 9) + File.separator;

        //目录不存在则创建
        File tempDir = new File(filePath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return filePath + hash.substring(9) + "." + nameSuffix;
    }

    /**
     * 文件加密，仅简单异或
     *
     * @param fileBytes 文件字节数组
     * @param xor       异或数字
     */
    public static byte[] encryptImgXor(byte[] fileBytes, int xor) {
        for (int i = 0; i < fileBytes.length; i++) {
            fileBytes[i] = (byte) (fileBytes[i] ^ xor);
        }
        return fileBytes;
    }
}
