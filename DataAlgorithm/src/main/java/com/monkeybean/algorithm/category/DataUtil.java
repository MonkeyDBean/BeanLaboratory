package com.monkeybean.algorithm.category;

import java.util.*;

/**
 * 简单数据处理
 * <p>
 * Created by MonkeyBean on 2018/8/2.
 */
public class DataUtil {

    /**
     * 列表元素乱序，抽取元素
     *
     * @param origin 原数组列表
     * @return 乱序后的数组列表
     */
    public static List<Integer> shufflePump(List<Integer> origin) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0, tmpIndex, cycleNum = origin.size(); i < cycleNum; i++) {
            tmpIndex = (int) (Math.random() * origin.size());
            result.add(origin.remove(tmpIndex));
        }
        return result;
    }

    /**
     * 列表元素乱序，随机元素互换
     *
     * @param origin 原数组列表
     * @return 乱序后的数组列表
     */
    public static List<Integer> shuffleSwap(List<Integer> origin) {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0, tmpIndex, tmpElement; i < origin.size(); i++) {
            tmpIndex = random.nextInt(origin.size());
            if (tmpIndex != i) {
                tmpElement = origin.get(tmpIndex);
                origin.set(tmpIndex, origin.get(i));
                origin.set(i, tmpElement);
            }
        }
        return origin;
    }

    /**
     * 数组乱序，随机元素互换
     * 数组第一位开始，随机一个数字，第一位和随机出的那位交换，依次遍历这个过程到数组最后一位。时间复杂度O(n)，空间复杂度O(1)
     * 与抽取元素方法相比：随机性更强，已随机的位数仍会与后面随机的数位交换；无需开辟新数组，空间复杂度降低
     *
     * @param origin 原数组
     * @return 乱序后的数组
     */
    public static int[] shuffleSwap(int[] origin) {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0, tmpIndex; i < origin.length; i++) {
            tmpIndex = random.nextInt(origin.length);
            if (tmpIndex != i) {
                origin[tmpIndex] = origin[tmpIndex] ^ origin[i];
                origin[i] = origin[i] ^ origin[tmpIndex];
                origin[tmpIndex] = origin[tmpIndex] ^ origin[i];
            }
        }
        return origin;
    }

    /**
     * 输出a/b, 精确小数点后n位数
     *
     * @param a 分子
     * @param b 分母， 非零
     * @param n 小数位数, 非负数
     * @return 返回相除结果
     * @throws IllegalArgumentException 参数非法异常
     */
    public static String getAccurateDivide(int a, int b, int n) throws IllegalArgumentException {
        if (b == 0 || n < 0) {
            throw new IllegalArgumentException();
        }
        String symbol = "";
        if (a > 0 && b < 0 || a < 0 && b > 0) {
            symbol = "-";
        }
        StringBuilder sBuilder = new StringBuilder(symbol);
        int intPrefix = a / b;
        a = Math.abs(a);
        b = Math.abs(b);
        if (n == 0) {
            if (a % b * 10 / b >= 5) {
                sBuilder.append(intPrefix + 1);
            } else {
                sBuilder.append(intPrefix);
            }
        } else {
            sBuilder.append(intPrefix).append(".");
            if (n == 1) {
                a = a % b * 10;
            } else {
                for (int i = 0; i < n - 1; i++) {
                    a = a % b * 10;
                    sBuilder.append(a / b);
                }
            }
            int c = a / b;
            a = a % b * 10;
            if (a / b >= 5) {
                sBuilder.append(c + 1);
            } else {
                sBuilder.append(c);
            }
        }
        return sBuilder.toString();
    }

    /**
     * 选质数，低效
     *
     * @param range 范围
     * @return 质数集合
     */
    public static Set<Integer> getPrimeNumInefficient(int range) {
        Set<Integer> result = new TreeSet<>();
        for (int i = 2; i <= range; i++) {
            boolean isPrime = true;
            for (int j = 2; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) result.add(i);
        }
        return result;
    }

    /**
     * 选质数，高效
     *
     * @param range 范围
     * @return 质数集合
     */
    public static Set<Integer> getPrimeNumEfficient(int range) {
        int[] flag = new int[range + 1];
        for (int i = 2; i <= Math.sqrt(range); i++) {
            for (int j = i; i * j <= range; j++) {
                flag[i * j] = 1;
            }
        }
        Set<Integer> result = new TreeSet<>();
        for (int i = 2; i <= range; i++) {
            if (flag[i] == 0) {
                result.add(i);
            }
        }
        return result;
    }

    //去重

}
