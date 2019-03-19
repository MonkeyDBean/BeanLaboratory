package com.monkeybean.algorithm.category;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by MonkeyBean on 2019/3/19.
 */
public class ChinaDateTest {

    @Test
    public void lunarToSolar() throws Exception {
        String lunarDate1 = "1992-12-24";
        String lunarDate2 = "1993-12-24";
        String expectSolarDate1 = "1993-01-16";
        String expectSolarDate2 = "1994-02-04";
        String solarDateResult1 = ChinaDate.lunarToSolar(lunarDate1);
        String solarDateResult2 = ChinaDate.lunarToSolar(lunarDate2);
        assertTrue(expectSolarDate1.equals(solarDateResult1));
        assertTrue(expectSolarDate2.equals(solarDateResult2));
    }

    @Test
    public void solarToLunar() throws Exception {
        System.out.println("阳历转阴历: ");
        String origin1 = "1994-02-04";
        String origin2 = "2015-12-04";
        String expectLunarDate1 = "1993-12-24";
        String result1 = ChinaDate.solarToLunar(origin1, true);
        String result2 = ChinaDate.solarToLunar(origin2, false);
        assertTrue(expectLunarDate1.equals(result1));
        System.out.println("阳历：" + origin1 + " 转为 阴历：" + result1);
        System.out.println("阳历：" + origin2 + " 转为 " + result2);
    }

}