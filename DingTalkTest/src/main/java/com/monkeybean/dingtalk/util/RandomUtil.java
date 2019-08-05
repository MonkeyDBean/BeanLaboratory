package com.monkeybean.dingtalk.util;

import java.util.Random;

/**
 * 随机工具
 * <p>
 * Created by MonkeyBean on 2019/4/19.
 */
public class RandomUtil {
    private static String stringPool = "abcdefghijklmnopqrstuvwxyz1234567890,._*";

    /**
     * 生成随机数
     *
     * @param count 随机数上限
     * @return 不包含上限，[0, count)区间的随机正整数
     */
    private static int getRandom(int count) {
        Random random = new Random();
        return random.nextInt(count);
    }

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     */
    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        int len = stringPool.length();
        for (int i = 0; i < length; i++) {
            sb.append(stringPool.charAt(getRandom(len)));
        }
        return sb.toString();
    }
}
