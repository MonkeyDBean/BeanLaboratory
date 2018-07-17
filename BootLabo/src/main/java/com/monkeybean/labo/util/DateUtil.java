package com.monkeybean.labo.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期时间处理工具
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class DateUtil {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 获取昨天日期的字符串格式，格式：yyyy-MM-dd
     */
    public static String getLastDay() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.DATE, -1);
        return new DateTime(calendar.getTime()).toString(DATE_PATTERN);
    }

    /**
     * 获取上周开始日期的字符串格式，格式：yyyy-MM-dd
     * 以周一为开始时间
     */
    public static String getLastWeekStart(String date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(DateTime.parse(date, DateTimeFormat.forPattern(DATE_PATTERN)).toDate());
        int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int startNum;
        if (i == 0) {   //周天
            startNum = 7 + 6;
        } else {
            startNum = 7 + i - 1;
        }
        calendar.add(Calendar.DATE, -startNum);
        return new DateTime(calendar.getTime()).toString(DATE_PATTERN);
    }

    /**
     * 获取本周开始日期的字符串格式，格式：yyyy-MM-dd
     * 以周一为开始时间
     */
    public static String getWeekStart(String date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(DateTime.parse(date, DateTimeFormat.forPattern(DATE_PATTERN)).toDate());
        int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int startNum;
        if (i == 0) {
            startNum = 6;
        } else {
            startNum = i - 1;
        }
        calendar.add(Calendar.DATE, -startNum);
        return new DateTime(calendar.getTime()).toString(DATE_PATTERN);
    }

    /**
     * 获取一个月之前日期的字符串格式，格式：yyyy-MM-dd
     */
    public static String getOneMonthAgo() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.MONTH, -1);
        return new DateTime(calendar.getTime()).toString(DATE_PATTERN);
    }

    /**
     * String类型日期（yyyy-MM-dd HH:mm:ss）转Calendar
     */
    public static Calendar strToCalendar(String dateStr) {
        DateTimeFormatter format = DateTimeFormat.forPattern(TIME_PATTERN);
        DateTime dateTime = DateTime.parse(dateStr, format);
        return dateTime.toCalendar(Locale.CHINA);
    }

    /**
     * yyyy-MM-dd字符串格式转为Date
     */
    public static Date dateStr2Date(String str) {
        Date d = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            d = sdf.parse(str);
        } catch (Exception e) {
            logger.error("dateStr2Date, parse error: {}", e);
        }
        return d;
    }

    /**
     * yyyy-MM-dd HH:mm:ss字符串格式转为Date
     */
    public static Date timeStr2Date(String str) {
        Date d = null;
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
        try {
            d = sdf.parse(str);
        } catch (Exception e) {
            logger.error("timeStr2Date, parse error: {}", e);
        }
        return d;
    }

    /**
     * 判断日期间隔是否在最近一个月内
     *
     * @param dateFrom 开始时间(格式: yyyy-MM-dd)
     * @param dataTo   结束时间(格式: yyyy-MM-dd)
     * @return 后者日期在前者日期之前，且在最近一月内返回true
     */
    public static boolean checkOneMonthLegal(String dateFrom, String dataTo) {
        Calendar fromCalendar = Calendar.getInstance(TimeZone.getDefault());
        fromCalendar.setTime(DateUtil.dateStr2Date(dateFrom));
        Calendar endCalendar = Calendar.getInstance(TimeZone.getDefault());
        endCalendar.setTime(DateUtil.dateStr2Date(dataTo));
        if (!fromCalendar.after(endCalendar)) {
            Calendar standardCalendar = Calendar.getInstance(TimeZone.getDefault());
            boolean endLegal = !endCalendar.after(standardCalendar);
            standardCalendar.add(Calendar.MONTH, -1);
            boolean startLegal = !standardCalendar.after(fromCalendar);
            return endLegal && startLegal;
        }
        return false;
    }

    /**
     * 检查当前时间是否在参数时间之前
     * 若是，返回true
     */
    public static boolean checkNowBeforeDate(Date date) {
        Date dateNow = new Date();
        return dateNow.before(date);
    }

    public static boolean checkNowBeforeDate(String dateStr) {
        Calendar sCalendar = DateUtil.strToCalendar(dateStr);
        return Calendar.getInstance().before(sCalendar);
    }

}
