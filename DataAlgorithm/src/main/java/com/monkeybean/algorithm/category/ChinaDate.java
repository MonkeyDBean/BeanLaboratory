package com.monkeybean.algorithm.category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 阴阳历转换
 * <p>
 * Created by MonkeyBean on 2019/3/19.
 */
public class ChinaDate {

    /**
     * 阴历分大小月，大月30天，小月29天，但一年中哪个月为大月，哪个月为小月是不确定的。
     * 阴历每十年有4个润年，但哪一年为润年也是不确定的。而润月中，哪个润月为大月，哪个为小月也是不确定的。
     * 因此，推算阴历没有统一的算法。
     * 阴历由天文观测得到，从天文台获取1900-2049的阴历数据信息如下。
     * 数据解读：以第一条数据 0x04bd8 为例，16进制数，用二进制表示为 0000 0100 1011 1011 1000,
     * 前4位，即0在这一年是润年时才有意义，它代表这年润月的大小月，为1则润大月，为0则润小月；
     * 中间12位，即4bd，每位代表一个月，为1则为大月，为0则为小月；
     * 最后4位，即8，代表这一年的润月月份，为0则不润。首4位要与末4位搭配使用。
     */
    private static final int[] lunarData = {
            0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
            0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
            0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
            0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
            0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
            0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
            0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
            0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
            0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
            0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
            0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
            0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
            0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
            0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
            0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0
    };

    /**
     * 允许输入的最小年份
     */
    private static final int MIN_YEAR = 1900;
    /**
     * 允许输入的最大年份
     */
    private static final int MAX_YEAR = 2049;
    /**
     * 阳历日期计算起点
     */
    private static final String START_DATE = "1900-01-30";

    /**
     * 计算阴历年，润哪个月(1-12)
     *
     * @param year 阴历年
     * @return 月份，无润月返回0
     */
    private static int getLeapMonth(int year) {
        return lunarData[year - 1900] & 0xf;
    }

    /**
     * 获取阴历年的润月天数
     *
     * @param year 阴历年
     * @return 天数
     */
    private static int getLeapMonthDays(int year) {
        if (getLeapMonth(year) != 0) {
            if ((lunarData[year - 1900] & 0xf0000) == 0) {
                return 29;
            } else {
                return 30;
            }
        } else {
            return 0;
        }
    }

    /**
     * 计算阴历年，月的天数
     *
     * @param year  阴历年
     * @param month 阴历月
     * @return 该月天数:29或30，返回0为传参错误
     */
    private static int getMonthDays(int year, int month) {
        if (month > 12 || month < 0) {
            return 0;
        }

        //0X0FFFF[0000 {1111 1111 1111} 1111]中间12位代表12个月，1为大月，0为小月
        int bit = 1 << (16 - month);
        if (((lunarData[year - 1900] & 0x0FFFF) & bit) == 0) {
            return 29;
        } else {
            return 30;
        }
    }

    /**
     * 计算阴历年的总天数
     *
     * @param year 阴历年
     * @return 总天数
     */
    private static int getYearDays(int year) {
        int sum = 29 * 12;
        for (int i = 0x8000; i >= 0x8; i >>= 1) {
            if ((lunarData[year - 1900] & 0xfff0 & i) != 0) {
                sum++;
            }
        }
        return sum + getLeapMonthDays(year);
    }

    /**
     * 计算两个阳历日期相差的天数
     *
     * @param startDate 开始日期
     * @param endDate   截止日期
     * @return 相差天数
     */
    private static int daysBetween(Date startDate, Date endDate) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(startDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(endDate);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        int days = 0;
        Calendar tempCalendar;

        //如果can1 < calendar2，减去小的时间在这一年已经过了的天数，加上大的时间已过的天数
        if (calendar1.before(calendar2)) {
            days -= calendar1.get(Calendar.DAY_OF_YEAR);
            days += calendar2.get(Calendar.DAY_OF_YEAR);
            tempCalendar = calendar1;
        } else {
            days -= calendar2.get(Calendar.DAY_OF_YEAR);
            days += calendar1.get(Calendar.DAY_OF_YEAR);
            tempCalendar = calendar2;
        }
        for (int i = 0; i < Math.abs(year2 - year1); i++) {

            //获取小的时间当前年的总天数
            days += tempCalendar.getActualMaximum(Calendar.DAY_OF_YEAR);

            //计算下一年
            tempCalendar.add(Calendar.YEAR, 1);
        }
        return days;
    }

    /**
     * 检查阴历日期是否合法
     *
     * @param lunarYear     阴历年
     * @param lunarMonth    阴历月
     * @param lunarDay      阴历日
     * @param leapMonthFlag 闰月标志
     * @throws Exception 参数异常
     */
    private static void checkLunarDate(int lunarYear, int lunarMonth, int lunarDay, boolean leapMonthFlag) throws Exception {
        if ((lunarYear < MIN_YEAR) || (lunarYear > MAX_YEAR)) {
            throw (new Exception("非法农历年份！"));
        }
        if ((lunarMonth < 1) || (lunarMonth > 12)) {
            throw (new Exception("非法农历月份！"));
        }

        //阴历月最多30天
        if ((lunarDay < 1) || (lunarDay > 30)) {
            throw (new Exception("非法农历天数！"));
        }

        //计算该年应该闰哪个月
        int leap = getLeapMonth(lunarYear);
        if (leapMonthFlag && (lunarMonth != leap)) {
            throw (new Exception("非法闰月！"));
        }
    }

    /**
     * 阴历转阳历
     */
    public static Map<String, Integer> lunarToSolar(int year, int month, int day) {
        String monthStr = month < 10 ? "0" + month : "" + month;
        String dayStr = day < 10 ? "0" + day : "" + day;
        String dateStr = year + "-" + monthStr + "-" + dayStr;
        String sonarStr = null;
        try {
            sonarStr = lunarToSolar(dateStr);
        } catch (Exception e) {
            System.err.println("lunarToSolar, param error" + e);
        }
        Map<String, Integer> data = null;
        if (sonarStr != null) {
            data = new HashMap<>();
            String[] str = sonarStr.split("-");
            data.put("year", Integer.parseInt(str[0]));
            data.put("month", Integer.parseInt(str[1]));
            data.put("day", Integer.parseInt(str[2]));
        }
        return data;
    }

    /**
     * 阴历转换为阳历
     *
     * @param lunarDate 阴历日期,格式yyyyMMdd
     * @return 阳历日期, 格式：yyyyMMdd
     * @throws Exception 参数异常
     */
    public static String lunarToSolar(String lunarDate) throws Exception {
        int lunarYear = Integer.parseInt(lunarDate.substring(0, 4));
        int lunarMonth = Integer.parseInt(lunarDate.substring(5, 7));
        int lunarDay = Integer.parseInt(lunarDate.substring(8, 10));

        //是否为闰月
        boolean leapMonthFlag = getLeapMonth(lunarYear) == lunarMonth;

        //校验参数
        checkLunarDate(lunarYear, lunarMonth, lunarDay, leapMonthFlag);

        int offset = 0;
        for (int i = MIN_YEAR; i < lunarYear; i++) {

            // 求阴历某年天数
            int yearDaysCount = getYearDays(i);
            offset += yearDaysCount;
        }

        //计算该年闰几月
        int leapMonth = getLeapMonth(lunarYear);
        if (leapMonthFlag && leapMonth != lunarMonth) {
            throw (new Exception("您输入的闰月标志有误！"));
        }

        //当年没有闰月或月份早于闰月或和闰月同名的月份
        if (leapMonth == 0 || (lunarMonth < leapMonth) || (lunarMonth == leapMonth && !leapMonthFlag)) {
            for (int i = 1; i < lunarMonth; i++) {
                int tempMonthDaysCount = getMonthDays(lunarYear, i);
                offset += tempMonthDaysCount;
            }

            // 检查日期是否大于最大天
            if (lunarDay > getMonthDays(lunarYear, lunarMonth)) {
                throw (new Exception("不合法的农历日期！"));
            }

            // 加上当月的天数
            offset += lunarDay;
        } else {//当年有闰月，且月份晚于或等于闰月
            for (int i = 1; i < lunarMonth; i++) {
                int tempMonthDaysCount = getMonthDays(lunarYear, i);
                offset += tempMonthDaysCount;
            }
            if (lunarMonth > leapMonth) {

                // 计算闰月天数
                int temp = getLeapMonthDays(lunarYear);

                // 加上闰月天数
                offset += temp;
                if (lunarDay > getMonthDays(lunarYear, lunarMonth)) {
                    throw (new Exception("不合法的农历日期！"));
                }
                offset += lunarDay;
            } else { // 如果需要计算的是闰月，则应首先加上与闰月对应的普通月的天数
                // 计算非闰月天数
                int temp = getMonthDays(lunarYear, lunarMonth);
                offset += temp;

                if (lunarDay > getLeapMonthDays(lunarYear)) {
                    throw (new Exception("不合法的农历日期！"));
                }
                offset += lunarDay;
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = formatter.parse(START_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(myDate);
        c.add(Calendar.DATE, offset);
        myDate = c.getTime();
        return formatter.format(myDate);
    }

    /**
     * 阳历转阴历, 参数为阳历时间
     */
    public static Map<String, Integer> solarToLunar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(calendar.getTime());
        String lunarStr = solarToLunar(dateStr, true);
        Map<String, Integer> map = new HashMap<>();
        String[] lunarArray = lunarStr.split("-");
        map.put("year", Integer.parseInt(lunarArray[0]));
        map.put("month", Integer.parseInt(lunarArray[1]));
        map.put("day", Integer.parseInt(lunarArray[2]));
        return map;
    }

    /**
     * 阳历日期转换为阴历日期
     *
     * @param solarDate 阳历日期,格式yyyyMMdd
     * @param isFormat  是否格式化输出,若为true,返回yyyyMMdd;否则返回普通字符串格式
     * @return 阴历日期
     */
    public static String solarToLunar(String solarDate, Boolean isFormat) {
        int i;
        int temp = 0;
        int lunarYear;
        int lunarMonth;
        int lunarDay;
        boolean leapMonthFlag = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = null;
        Date startDate = null;
        try {
            myDate = formatter.parse(solarDate);
            startDate = formatter.parse(START_DATE);
        } catch (ParseException e) {
            System.err.println("ParseException: " + e);
        }
        int offset = daysBetween(startDate, myDate);
        for (i = MIN_YEAR; i <= MAX_YEAR; i++) {

            //求当年农历年天数
            temp = getYearDays(i);
            if (offset - temp < 1) {
                break;
            } else {
                offset -= temp;
            }
        }
        lunarYear = i;

        //计算该年闰哪个月
        int leapMonth = getLeapMonth(lunarYear);

        // 当年是否有闰月
        boolean isLeapYear = leapMonth > 0;
        for (i = 1; i <= 12; i++) {
            if (i == leapMonth + 1 && isLeapYear) {
                temp = getLeapMonthDays(lunarYear);
                isLeapYear = false;
                leapMonthFlag = true;
                i--;
            } else {
                temp = getMonthDays(lunarYear, i);
            }
            offset -= temp;
            if (offset <= 0) {
                break;
            }
        }
        offset += temp;
        lunarMonth = i;
        lunarDay = offset;
        if (isFormat) {
            String lunarMonthStr = lunarMonth < 10 ? "0" + lunarMonth : "" + lunarMonth;
            String lunarDayStr = String.valueOf(lunarDay).length() == 1 ? "0" + lunarDay : "" + lunarDay;
            return lunarYear + "-" + lunarMonthStr + "-" + lunarDayStr;
        } else {
            return "阴历：" + lunarYear + "年" + (leapMonthFlag && (lunarMonth == leapMonth) ? "闰" : "") + lunarMonth + "月" + lunarDay + "日";
        }
    }
}
