package com.monkeybean.algorithm.category;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by MonkeyBean on 2018/8/2.
 */
public class SortUtilTest {

    /**
     * 数组长度
     */
    private static int arraySize;

    @Before
    public void setUp() throws Exception {
        arraySize = 10;
        SortUtil.setIsPrint(true);
//        arraySize = 100000;
//        SortUtil.setIsPrint(false);
    }

    @Test
    public void multiSort() throws Exception {
        SortUtil sortUtil = new SortUtil();
        List<SortUtil.SortObject<Double>> resultList = sortUtil.multiSort(100, false);
        for (SortUtil.SortObject<Double> each : resultList) {
            System.out.println(each.toString());
        }
    }

    @Test
    public void testSelectSort() throws Exception {
        int[] origin = SortUtil.initArray(arraySize);
        SortUtil.print(origin);
        long start = System.currentTimeMillis();
        SortUtil.selectSort(origin);
        long interval = System.currentTimeMillis() - start;
        System.out.println("call selectSort, time cost is: " + interval + "ms");
        SortUtil.print(origin);
    }

    @Test
    public void testInsertSort() throws Exception {
        int[] origin = SortUtil.initArray(arraySize);
        SortUtil.print(origin);
        long start = System.currentTimeMillis();
        SortUtil.insertSort(origin);
        long interval = System.currentTimeMillis() - start;
        System.out.println("call insertSort, time cost is: " + interval + "ms");
        SortUtil.print(origin);
    }

    @Test
    public void testBinaryInsertSort() throws Exception {
        int[] origin = SortUtil.initArray(arraySize);
        SortUtil.print(origin);
        long start = System.currentTimeMillis();
        SortUtil.binaryInsertionSort(origin);
        long interval = System.currentTimeMillis() - start;
        System.out.println("call binaryInsertionSort, time cost is: " + interval + "ms");
        SortUtil.print(origin);
    }

    @Test
    public void testShellSort() throws Exception {
        int[] origin = SortUtil.initArray(arraySize);
        SortUtil.print(origin);
        long start = System.currentTimeMillis();
        SortUtil.shellSort(origin);
        long interval = System.currentTimeMillis() - start;
        System.out.println("call shellSort, time cost is: " + interval + "ms");
        SortUtil.print(origin);
    }

    @Test
    public void testBubbleSort() throws Exception {
        int[] origin = SortUtil.initArray(arraySize);
        SortUtil.print(origin);
        long start = System.currentTimeMillis();
        SortUtil.bubbleSort(origin);
        long interval = System.currentTimeMillis() - start;
        System.out.println("call bubbleSort, time cost is: " + interval + "ms");
        SortUtil.print(origin);
    }

    @Test
    public void testQuickStart() {
        int[] origin = SortUtil.initArray(arraySize);
        SortUtil.print(origin);
        long start = System.currentTimeMillis();
        SortUtil.quickSort(origin);
        long interval = System.currentTimeMillis() - start;
        System.out.println("call quickSort, time cost is: " + interval + "ms");
        SortUtil.print(origin);
    }

    @Test
    public void testMergeSort() {
        int[] origin = SortUtil.initArray(arraySize);
        SortUtil.print(origin);
        long start = System.currentTimeMillis();
        SortUtil.mergeSort(origin);
        long interval = System.currentTimeMillis() - start;
        System.out.println("call mergeSort, time cost is: " + interval + "ms");
        SortUtil.print(origin);
    }

    @Test
    public void testHeapSort() {
        int[] origin = SortUtil.initArray(arraySize);
        SortUtil.print(origin);
        long start = System.currentTimeMillis();
        SortUtil.heapSort(origin);
        long interval = System.currentTimeMillis() - start;
        System.out.println("call heapSort, time cost is: " + interval + "ms");
        SortUtil.print(origin);
    }

    @Test
    public void testRadixSort() {
        int a = 1;
        int b = 87654321;
        int c = 2147483647;
        System.out.println("number: " + a + ", bit: " + SortUtil.getDigitBit(a));
        System.out.println("number: " + b + ", bit: " + SortUtil.getDigitBit(b));
        System.out.println("number: " + c + ", bit: " + SortUtil.getDigitBit(c));
        System.out.println("number: " + c + ", 1 bit is: " + SortUtil.getFigure(c, 1) + ", 5 bit is: " + SortUtil.getFigure(c, 5) + ", 10 bit is: " + SortUtil.getFigure(c, 10));

        int[] testArray = {10000, 4, 12, 0, 456, 789, 88, 66, 1224, 1204, 1412, 12345};
        int maxBit = 1;
        for (int i = 0; i < testArray.length; i++) {
            int numberBit = SortUtil.getDigitBit(testArray[i]);
            if (numberBit > maxBit) {
                maxBit = numberBit;
            }
        }
        SortUtil.radixSort(testArray, maxBit);
        System.out.println("call radixSort, result is: ");
        for (int i = 0; i < testArray.length; i++) {
            System.out.println(testArray[i] + "\t");
        }
        System.out.println();
    }

}