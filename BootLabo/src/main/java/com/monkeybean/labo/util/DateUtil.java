package com.monkeybean.labo.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期时间处理工具
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class DateUtil {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private DateUtil() {
    }

    /**
     * 获取昨天日期的字符串格式，格式：yyyy-MM-dd
     */
    public static String getLastDayStr() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.DATE, -1);
        return new DateTime(calendar.getTime()).toString(DATE_PATTERN);
    }

    /**
     * 获取昨天的日期
     */
    public static Date getLastDay() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
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
     * 以当前日期为基准，获取前后天数的日期
     *
     * @param n 正数为n天后，负数为n天前
     * @return 时间格式为yyyy-MM-dd
     */
    public static String getNDay(int n) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        if (n != 0) {
            calendar.add(Calendar.DATE, n);
        }
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
     * 判断日期间隔是否在最近n个月内
     *
     * @param dateFrom    开始时间(格式: yyyy-MM-dd)
     * @param dataTo      结束时间(格式: yyyy-MM-dd)
     * @param monthNumber 判断的月份区间
     * @return 后者日期在前者日期之前，且在最近n个月内返回true
     */
    public static boolean checkNMonthLegal(String dateFrom, String dataTo, int monthNumber) {
        Calendar fromCalendar = Calendar.getInstance(TimeZone.getDefault());
        fromCalendar.setTime(DateUtil.dateStr2Date(dateFrom));
        Calendar endCalendar = Calendar.getInstance(TimeZone.getDefault());
        endCalendar.setTime(DateUtil.dateStr2Date(dataTo));
        if (!fromCalendar.after(endCalendar)) {
            Calendar standardCalendar = Calendar.getInstance(TimeZone.getDefault());
            boolean endLegal = !endCalendar.after(standardCalendar);
            standardCalendar.add(Calendar.MONTH, -monthNumber);
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

    /**
     * 判断当前日期是否在给定日期之前
     *
     * @param dateStr 日期字符串，格式：yyyy-MM-dd HH:mm:ss
     * @return true为当前日期在给定日期之前
     */
    public static boolean checkNowBeforeDate(String dateStr) {
        Calendar calendar = DateUtil.strToCalendar(dateStr);
        return Calendar.getInstance().before(calendar);
    }

    /**
     * 校验给定日期是否在时间间隔内，包含左右边界
     *
     * @param startStr 开始时间(格式: yyyy-MM-dd)
     * @param endStr   结束时间(格式: yyyy-MM-dd)
     * @param dataStr  待校验时间(格式: yyyy-MM-dd)
     * @return 在时间间隔内则返回true
     */
    public static boolean checkDateInterval(String startStr, String endStr, String dataStr) {
        Date startDate = DateUtil.dateStr2Date(startStr);
        Date endDate = DateUtil.dateStr2Date(endStr);
        Date checkDate = DateUtil.dateStr2Date(dataStr);
        Calendar startCalendar = Calendar.getInstance(TimeZone.getDefault());
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance(TimeZone.getDefault());
        endCalendar.setTime(endDate);
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(checkDate);
        return !cal.before(startCalendar) && !cal.after(endCalendar);
    }

    /**
     * 获取指定日期延后秒数的日期
     *
     * @param date   日期
     * @param second 描述
     */
    public static Date getDelayTime(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 指定日期间隔, 随机生成n个日期
     *
     * @param startDate 起始日期
     * @param endDate   截至日期
     * @param count     随机个数, 10以内
     * @return 随机日期列表，失败返回空列表
     */
    public static List<Date> getRandomDateList(Date startDate, Date endDate, int count) {
        List<Date> dateList = new ArrayList<>();
        if (startDate.getTime() >= endDate.getTime() || count < 1 || count > 10) {
            logger.warn("getRandomDateList, param illegal, startDate: {}, endDate: {}, count: {}", startDate.getTime(), endDate.getTime(), count);
            return dateList;
        }
        for (int i = 0; i < count; i++) {
            dateList.add(new Date(randomTimeStamp(startDate.getTime(), endDate.getTime())));
        }

        //升序排列
        if (dateList.size() > 1) {
            dateList.sort((date1, date2) -> (int) (date1.getTime() - date2.getTime()));
        }
        return dateList;
    }

    /**
     * 获取指定时间戳内的随机日期
     *
     * @param begin 开始日期时间戳
     * @param end   截止截止时间戳
     * @return 随机时间戳
     */
    private static long randomTimeStamp(long begin, long end) {
        if (begin < 0 || end < 0 || begin >= end) {
            logger.warn("randomTimeStamp param illegal, begin: {}, end: {}", begin, end);
            return 0;
        }
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end) {
            return randomTimeStamp(begin, end);
        }
        return rtn;
    }

}
