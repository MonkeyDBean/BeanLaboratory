package com.monkeybean.algorithm.category;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by MonkeyBean on 2018/8/2.
 */
public class DataUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(DataUtilTest.class);

    //    @Test
    public void arrayShuffle() throws Exception {
        int n = 200000;
        int[] array = new int[n];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }
        long start = System.currentTimeMillis();
        DataUtil.shuffleSwap(array);
        long interval = System.currentTimeMillis() - start;
        //n=200000, time cost 10ms
        logger.info("array shufflePump finished, time cost is: {}ms, result is as follows", interval);
//        for(int i = 0; i < array.length; i++){
//            logger.info("index: {}, element: {}", i, array[i]);
//        }
    }

    //    @Test
    public void listShuffle() throws Exception {
        int n = 200000;
        List<Integer> swapOriginList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            swapOriginList.add(i, i + 1);
        }
        long start = System.currentTimeMillis();
        DataUtil.shuffleSwap(swapOriginList);
        long interval = System.currentTimeMillis() - start;
        //n=200000, time cost 50ms
        logger.info("list shuffleSwap finished, time cost is: {}ms, result is as follows", interval);
//        for(int element : swapOriginList){
//            logger.info("element: {}", element);
//        }

        List<Integer> pumpOriginList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            pumpOriginList.add(i, i + 1);
        }
        start = System.currentTimeMillis();
        List<Integer> listAfterPump = DataUtil.shufflePump(pumpOriginList);
        interval = System.currentTimeMillis() - start;
        //n=200000, time cost 2287ms
        logger.info("list shufflePump finished, time cost is: {}ms, result is as follows", interval);
//        for(int element : listAfterPump){
//            logger.info("element: {}", element);
//        }
    }

    //    @Test
    public void getAccurateDivide() throws Exception {
        int a = 4, b = -7, n = 1;
        logger.info("getAccurateDivide {}/{}, n is: {} result is: {}", a, b, n, DataUtil.getAccurateDivide(a, b, n));
        a = 3;
        b = 7;
        n = 30;
        logger.info("getAccurateDivide {}/{}, n is: {} result is: {}", a, b, n, DataUtil.getAccurateDivide(a, b, n));
    }

    @Test
    public void getPrimeNum() throws Exception {
        int range = 100000;
        long start = System.currentTimeMillis();
        Set<Integer> result1 = DataUtil.getPrimeNumInefficient(range);
        long interval = System.currentTimeMillis() - start;
        //range=100000, time cost 32ms
        logger.info("getPrimeNumInefficient, time cost is: {}ms, result is as follows", interval);
//        Iterator<Integer> it1 = result1.iterator();
//        while (it1.hasNext()) {
//            logger.info(it1.next().toString());
//        }

        start = System.currentTimeMillis();
        Set<Integer> result2 = DataUtil.getPrimeNumEfficient(range);
        interval = System.currentTimeMillis() - start;
        //range=100000, time cost 5ms
        logger.info("getPrimeNumEfficient, time cost is: {}ms, result is as follows", interval);
//        Iterator<Integer> it2 = result2.iterator();
//        while (it2.hasNext()) {
//            logger.info(it2.next().toString());
//        }
    }


}