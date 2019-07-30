package com.monkeybean.labo.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 字符校验工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class FilterUtf8Mb4Util {

    private FilterUtf8Mb4Util() {
    }

    /**
     * 过滤非汉字的utf8的字符，如emoji
     *
     * @param text 原字符串
     * @return 过滤后的字符串
     */
    public static String filterOffUtf8Mb4(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256; //去掉符号位
            if (((b >> 5) ^ 0x06) == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            } else if (((b >> 4) ^ 0x0E) == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            } else if (((b >> 3) ^ 0x1E) == 0) {
                i += 4;
            } else if (((b >> 2) ^ 0xBE) == 0) {
                i += 5;
            } else {
                i += 6;
            }
        }
        buffer.flip();
        return new String(buffer.array(), StandardCharsets.UTF_8);
    }

    /**
     * 检测是否有非法字符
     *
     * @param source 待检测字符串
     * @return 包含则返回true
     */
    public static boolean containsOutOfUtf8(String source) {
        return !(source == null || "".equals(source)) && source.length() != filterOffUtf8Mb4(source).length();
    }

}
