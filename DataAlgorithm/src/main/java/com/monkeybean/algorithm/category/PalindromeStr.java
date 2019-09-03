package com.monkeybean.algorithm.category;

import java.lang.reflect.InvocationTargetException;

/**
 * 验证字符串是否为回文
 * <p>
 * Created by MonkeyBean on 2019/9/2.
 */
public class PalindromeStr {

    private final static int methodNum = 4;

    /**
     * 仅考虑字母与数字字符, 可忽略字母大小写
     *
     * @param originStr  原始字符串
     * @param methodFlag 方法调用标识
     * @return 若为回文则返回true
     */
    public static boolean verify(String originStr, int methodFlag) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (methodFlag < 0 || methodFlag > methodNum) {
            System.out.println("ReverseEqualStr, verify Method, methodFlag param illegal, is: " + methodFlag);
        }
        if (originStr == null) {
            return false;
        } else if (originStr.length() <= 1) {
            return true;
        }
        return (boolean) PalindromeStr.class.getMethod("method" + methodFlag, String.class).invoke(PalindromeStr.class, originStr);
    }

    /**
     * StringBuffer Reverse
     *
     * @param originStr 原始字符串
     */
    public static boolean method1(String originStr) {
        originStr = filterAlphabetOrDigit(originStr);
        return originStr.equalsIgnoreCase(new StringBuffer(originStr).reverse().toString());
    }

    /**
     * tempArray
     *
     * @param originStr 原始字符串
     */
    public static boolean method2(String originStr) {
        originStr = filterAlphabetOrDigit(originStr.toLowerCase().toCharArray());
        char[] charArray = originStr.toCharArray();
        String reverse = "";
        for (int i = charArray.length - 1; i >= 0; i--) {
            reverse += charArray[i];
        }
        return originStr.equals(reverse);
    }

    /**
     * not use String.toLowerCase() or tempArray
     *
     * @param originStr 原始字符串
     */
    public static boolean method3(String originStr) {
        char[] charArray = originStr.toCharArray();
        int k = 0;
        for (int i = 0; i < charArray.length; i++) {
            if (isABC123(charArray[i])) {
                charArray[k++] = charArray[i];
            }
        }
        int i = 0;
        int j = k - 1;
        while (i < j) {
            if (toLower(charArray[i]) != toLower(charArray[j])) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    /**
     * left ++ and right --
     *
     * @param originStr 原始字符串
     */
    public static boolean method4(String originStr) {
        char[] charArray = originStr.toCharArray();
        int i = 0;
        int j = charArray.length - 1;
        while (i < j) {
            while (i < j && !isABC123(charArray[i])) {
                i++;
            }
            while (i < j && !isABC123(charArray[j])) {
                j--;
            }
            if (i == j) {
                break;
            }
            if (toLower(charArray[i]) != toLower(charArray[j])) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    /**
     * 转为小写字符
     */
    private static char toLower(char ch) {
        if (ch >= 'A' && ch <= 'Z') {
            ch = (char) (ch + 32);
        }
        return ch;
    }

    /**
     * 过滤特殊字符, 仅保留字母和数字
     *
     * @param originStr 原始字符串
     */
    private static String filterAlphabetOrDigit(String originStr) {
        return originStr.replaceAll("[^(A-Za-z0-9)]", "");
    }

    /**
     * 过滤特殊字符, 仅保留字母和数字
     *
     * @param originCharArray 原始字符数组
     */
    private static String filterAlphabetOrDigit(char[] originCharArray) {
        for (int i = 0; i < originCharArray.length; i++) {
            if (!isABC123(originCharArray[i])) {
                originCharArray[i] = ' ';
            }
        }
        return new String(originCharArray).replace(" ", "");
    }

    /**
     * 过滤特殊字符, 仅保留字母和数字
     *
     * @param originCharArray 原始字符数组
     */
    private static String filterAlphabetOrDigit2(char[] originCharArray) {
        StringBuilder sBuilder = new StringBuilder();
        for (char anOriginChar : originCharArray) {
            if (isABC123(anOriginChar)) {
                sBuilder.append(anOriginChar);
            }
        }
        return sBuilder.toString();
    }

    /**
     * 判断是否为字母或数字
     *
     * @param ch 原始字符
     */
    private static boolean isABC123(char ch) {
        return ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z';
    }
}
