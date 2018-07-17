package com.monkeybean.labo.util;

import java.text.DecimalFormat;

/**
 * 其他通用工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class CommonUtil {

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

}
