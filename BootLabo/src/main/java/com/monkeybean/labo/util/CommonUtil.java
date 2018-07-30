package com.monkeybean.labo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

/**
 * 其他通用工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class CommonUtil {

    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 获取数字某位的值
     *
     * @param num   整数数字
     * @param index 非负数, 0:个位, 1:十位, 2:百位...
     * @return 返回数字某位，参数不合法则返回0
     */
    public static int getNum(int num, int index) {
        return (num / (int) Math.pow(10, index)) % 10;
    }

    /**
     * 格式化为小数点两位
     */
    public static String getString2(String str) {
        Double d = Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    public static String getString2(int value) {
        return getString2(String.valueOf(value));
    }

    public static String getString2(double value) {
        return getString2(String.valueOf(value));
    }

    /**
     * 检查密码是否过于简单, 6-20位，包含字母和数字，该方法可用正则替换
     */
    public static boolean checkPassword(String str) {
        boolean isDigit = false;
        boolean isLetter = false;
        int length = str.length();
        if (length < 6 || length > 20)
            return false;
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {
                isLetter = true;
            } else {
                return false;
            }
        }
        return isDigit && isLetter;
    }
}
