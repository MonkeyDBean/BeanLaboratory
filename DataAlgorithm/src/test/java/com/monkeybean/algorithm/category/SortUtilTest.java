package com.monkeybean.algorithm.category;

import org.junit.Test;

import java.util.List;


/**
 * Created by MonkeyBean on 2018/8/2.
 */
public class SortUtilTest {

    @Test
    public void multiSort() throws Exception {
        SortUtil sortUtil = new SortUtil();
        List<SortUtil.SortObject<Double>> resultList = sortUtil.multiSort(100, false);
        for (SortUtil.SortObject<Double> each : resultList) {
            System.out.println(each.toString());
        }

    }
}