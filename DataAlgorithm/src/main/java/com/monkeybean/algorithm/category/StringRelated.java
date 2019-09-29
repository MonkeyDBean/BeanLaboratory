package com.monkeybean.algorithm.category;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

/**
 * 处理字符串相关
 * <p>
 * Created by MonkeyBean on 2019/9/27.
 */
public class StringRelated {

    /**
     * 奇偶打印计数
     */
    private static volatile int i = 1;

    public static void main(String[] args) {

        // 子串查找
        String str1 = "www.taobao.com";
        String str2 = "taobao";
        System.out.println("str1: " + str1 + ", str2:" + str2 + ", result: " + searchSubstring(str1, str2));

        // 字符频率
        String[] strArray = {"www.taobao.com", "taobao", "asdf123123"};
        charFrequency(strArray);

        // 奇偶打印
        System.out.println();
        printOddAndEvenInorderByTwoThread();

    }

    /**
     * 两个线程交替打印1-100的整数，一个打印奇数，一个打印偶数，要求输出结果有序
     */
    public static void printOddAndEvenInorderByTwoThread() {
        final Object object = new Object();

        Runnable runnable = () -> {
            synchronized (object) {
                for (; i <= 100; ) {
                    System.out.println(Thread.currentThread().getName() + "\t" + (i++));
                    try {
                        object.notify();
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                object.notifyAll();
            }
        };

        Thread thread1 = new Thread(runnable, "odd Thread");
        Thread thread2 = new Thread(runnable, "even Thread");
        thread1.start();
        thread2.start();
    }

    /**
     * 查找子串在给定字符串第一次出现的位置
     * 输入：字符串str1，字符串str2
     * 输出：字符串str2在str1中第一次出现的位置，如果没有则返回-1
     * 例如：str1="www.taobao.com" str2="taobao", 输出为4
     *
     * @param str1 待查找的字符串
     * @param str2 子串
     * @return 查找成功则返回第一次出现的位置，失败则返回-1
     */
    public static int searchSubstring(String str1, String str2) {
        if (str1 == null || str2 == null || "".equals(str1) || str1.length() < str2.length()) {
            return -1;
        }
        int methodFlag = 1;
        int res1 = -1;
        int res2 = -1;
        try {
            res1 = (int) StringRelated.class.getMethod("searchMethod" + methodFlag, String.class, String.class).invoke(StringRelated.class, str1, str2);
            methodFlag = 2;
            res2 = (int) StringRelated.class.getMethod("searchMethod" + methodFlag, String.class, String.class).invoke(StringRelated.class, str1, str2);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert res1 == res2;
        return res1;
    }

    /**
     * 查找子串位置
     *
     * @param str1 待查找的字符串
     * @param str2 子串
     * @return 查找成功则返回第一次出现的位置，失败则返回-1
     */
    public static int searchMethod1(String str1, String str2) {
        Matcher matcher = Pattern.compile(str2).matcher(str1);
        if (matcher.find()) {
            return matcher.start();
        } else {
            return -1;
        }
    }

    /**
     * 查找子串位置(jdk8 indexOf实现方式)
     *
     * @param str1 待查找的字符串
     * @param str2 子串
     * @return 查找成功则返回第一次出现的位置，失败则返回-1
     */
    public static int searchMethod2(String str1, String str2) {
        char[] charArray1 = str1.toCharArray();
        char[] charArray2 = str2.toCharArray();
        int max = charArray1.length - charArray2.length;

        for (int i = 0; i <= max; i++) {

            // 查找首个相等字符的位置
            if (charArray1[i] != charArray2[0]) {
                while (++i <= max && charArray1[i] != charArray2[0]) ;
            }

            // 匹配剩余字符
            if (i <= max) {
                int j = i + 1;
                int end = j + charArray2.length - 1;
                for (int k = 1; j < end && charArray1[j] == charArray2[k]; j++, k++) ;
                if (j == end) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 打印字符串出现频率
     * 输入：字符串数组 str[], 假设字符范围为ASCII码
     * 输出：这些字符串中字符的分布情况，按照降序排列
     *
     * @param strArray 给定的字符串数组
     */
    public static void charFrequency(String[] strArray) {
        if (strArray == null || strArray.length == 0) {
            return;
        }
        for (int i = 0; i < strArray.length; i++) {
            Map<Character, Integer> frequencyMap = new HashMap<>();
            char[] charArray = strArray[i].toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                if (frequencyMap.get(charArray[j]) == null) {
                    frequencyMap.put(charArray[j], 1);
                } else {
                    frequencyMap.put(charArray[j], frequencyMap.get(charArray[j]) + 1);
                }
            }

            // 按值降序
            Map<Character, Integer> sortedMap = frequencyMap
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                            LinkedHashMap::new));

            // 打印字符及频率
            System.out.println("\nstring is: " + strArray[i]);
            for (Map.Entry<Character, Integer> entry : sortedMap.entrySet()) {
                System.out.println("char is: " + entry.getKey() + ", frequency is: " + entry.getValue());
            }
        }
    }

}
