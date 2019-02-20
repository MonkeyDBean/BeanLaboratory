package com.monkeybean.algorithm.other;

/**
 * 简单数据交换
 * <p>
 * Created by MonkeyBean on 2019/2/19.
 */
public class SimpleSwap {

    /**
     * swap, 中间值
     */
//    private static <T extends Number> void swapTemp(T a, T b) {
    private static <T> void swapTemp(T a, T b) {
        System.out.println("swapTemp, param a = " + a + ",\t b = " + b);
        T c = a;
        a = b;
        b = c;
        System.out.println("swapTemp, result a = " + a + ",\t b = " + b);
    }

    /**
     * swap, 按位异或，不引入新的存储开销
     */
    private static void swapXor(long a, long b) {
        System.out.println("swapXor, long param a = " + a + ",\t b = " + b);
        a = a ^ b;
        b = b ^ a;
        a = a ^ b;
        System.out.println("swapXor, result a = " + a + ",\t b = " + b);
    }

    /**
     * swap, 一行代码实现，不建议生产环境使用
     */
    private static void swapSingleLine(int a, int b) {
        System.out.println("swapSingleLine, param a = " + a + ",\t b = " + b);
        a = a + b - (b = a);
        System.out.println("swapSingleLine, result a = " + a + ",\t b = " + b);
    }

    public static void main(String[] args) {
        swapTemp("test1", "test2");
        swapXor(1L, 2L);
        swapSingleLine(1, 2);
    }
}
