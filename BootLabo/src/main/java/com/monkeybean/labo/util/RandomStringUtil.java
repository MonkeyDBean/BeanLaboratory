package com.monkeybean.labo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * 生成随机序列
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class RandomStringUtil {
    private static final char[] symbols;
    private static final char[] numbers;
    private static Logger logger = LoggerFactory.getLogger(RandomStringUtil.class);

    static {
        StringBuilder tmp = new StringBuilder();
        StringBuilder tmpNum = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ch++) {
            tmp.append(ch);
            tmpNum.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            tmp.append(ch);
        }
        symbols = tmp.toString().toCharArray();
        numbers = tmpNum.toString().toCharArray();
    }

    private final Random random = new Random();
    private final char[] buf;

    /**
     * 构造函数
     *
     * @param length 值大于1
     */
    public RandomStringUtil(int length) {
        if (length < 1) {
            logger.error("RandomStringUtils constructor, param illegal, length: {}", length);
            throw new IllegalArgumentException("length < 1: " + length);
        }
        buf = new char[length];
    }

    /**
     * 获取随机序列
     *
     * @param hasAlpha 　　是否包含字母，否则产生纯数字的序列
     */
    public String nextString(boolean hasAlpha) {
        char[] workSet = hasAlpha ? symbols : numbers;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = workSet[random.nextInt(workSet.length)];
        }
        return new String(buf);
    }

}
