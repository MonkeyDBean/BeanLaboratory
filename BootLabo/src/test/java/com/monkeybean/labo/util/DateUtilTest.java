package com.monkeybean.labo.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class DateUtilTest {

    //    @Test
    public void strToCalendar() throws Exception {
        String dateStr = "2018-03-31 22:22:22";
        Calendar calendar = DateUtil.strToCalendar(dateStr);
        System.out.println("calendar, before add, getTime：" + calendar.getTime());
        calendar.add(Calendar.MONTH, -1);
        System.out.println("calendar, after add, getTime：" + calendar.getTime());
        System.out.println("calendar, day：" + calendar.get(Calendar.DAY_OF_YEAR));
    }

    //    @Test
    public void checkNMonthLegal() {
        String dateFrom = "2018-06-10";
        String dateTo = "2018-07-09";
        System.out.println("checkNMonthLegal result：" + DateUtil.checkNMonthLegal(dateFrom, dateTo, 2));
    }

    @Test
    public void getRandomDateList() {
        Date startDate = new Date();
        List<Date> resultList = DateUtil.getRandomDateList(startDate, DateUtil.getDelayTime(startDate, 10), 5);
        for (Date d : resultList) {
            System.out.println("getRandomDateList, random date is：" + d.getTime());
        }
    }

}