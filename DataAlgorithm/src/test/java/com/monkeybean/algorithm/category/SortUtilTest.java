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

}