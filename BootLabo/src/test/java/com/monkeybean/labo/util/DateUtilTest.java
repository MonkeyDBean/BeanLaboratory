package com.monkeybean.labo.util;

import org.junit.Test;

import java.util.Calendar;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class DateUtilTest {

    @Test
    public void strToCalendar() throws Exception {
        String dateStr = "2018-03-31 22:22:22";
        Calendar calendar = DateUtil.strToCalendar(dateStr);
        System.out.println("calendar, before add, getTime：" + calendar.getTime());
        calendar.add(Calendar.MONTH, -1);
        System.out.println("calendar, after add, getTime：" + calendar.getTime());
        System.out.println("calendar, day：" + calendar.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void checkOneMonthLegal() {
        String dateFrom = "2018-06-10";
        String dateTo = "2018-07-09";
        System.out.println("checkOneMonthLegal result：" + DateUtil.checkOneMonthLegal(dateFrom, dateTo));
    }

}