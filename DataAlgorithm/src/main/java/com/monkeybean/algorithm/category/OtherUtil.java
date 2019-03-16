package com.monkeybean.algorithm.category;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MonkeyBean on 2018/8/6.
 */
public final class OtherUtil {
    /**
     * 10天干
     */
    private static String[] trunk = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    /**
     * 12地支
     */
    private static String[] branch = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    /**
     * 12生肖
     */
    private static String[] zodiacAnimal = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    /**
     * 甲子序数对应五行前缀，共30个，每两年一换
     */
    private static String[] fiveElementPrefix = {"海中", "炉中", "大林", "路旁", "剑峰", "山头", "涧下", "城墙", "白蜡", "杨柳", "泉中", "屋上", "霹雳", "松柏", "长流",
            "砂中", "山下", "平地", "壁上", "金箔", "佛灯", "天河", "大驿", "钗钏", "桑柘", "大溪", "沙中", "天上", "石榴", "大海"};
    /**
     * 五行顺序：土、木、金、水、火 对应 0、1、2、3、4
     */
    private static String[] fiveElement = {"土", "木", "金", "水", "火"};

    /**
     * 阳历日期计算起点
     */
    private final static String START_DATE = "19000130";

    /**
     * 允许输入的最小年份
     */
    private final static int MIN_YEAR = 1900;

    /**
     * 允许输入的最大年份
     */
    private final static int MAX_YEAR = 2049;

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
    private static int[] lunarData = {
            0x04bd8,0x04ae0,0x0a570,0x054d5,0x0d260,0x0d950,0x16554,0x056a0,0x09ad0,0x055d2, //1900---1909
            0x04ae0,0x0a5b6,0x0a4d0,0x0d250,0x1d255,0x0b540,0x0d6a0,0x0ada2,0x095b0,0x14977, //1910---1919
            0x04970,0x0a4b0,0x0b4b5,0x06a50,0x06d40,0x1ab54,0x02b60,0x09570,0x052f2,0x04970, //1920
            0x06566,0x0d4a0,0x0ea50,0x06e95,0x05ad0,0x02b60,0x186e3,0x092e0,0x1c8d7,0x0c950, //1930
            0x0d4a0,0x1d8a6,0x0b550,0x056a0,0x1a5b4,0x025d0,0x092d0,0x0d2b2,0x0a950,0x0b557, //1940
            0x06ca0,0x0b550,0x15355,0x04da0,0x0a5b0,0x14573,0x052b0,0x0a9a8,0x0e950,0x06aa0, //1950
            0x0aea6,0x0ab50,0x04b60,0x0aae4,0x0a570,0x05260,0x0f263,0x0d950,0x05b57,0x056a0, //1960
            0x096d0,0x04dd5,0x04ad0,0x0a4d0,0x0d4d4,0x0d250,0x0d558,0x0b540,0x0b6a0,0x195a6, //1970
            0x095b0,0x049b0,0x0a974,0x0a4b0,0x0b27a,0x06a50,0x06d40,0x0af46,0x0ab60,0x09570, //1980
            0x04af5,0x04970,0x064b0,0x074a3,0x0ea50,0x06b58,0x055c0,0x0ab60,0x096d5,0x092e0, //1990
            0x0c960,0x0d954,0x0d4a0,0x0da50,0x07552,0x056a0,0x0abb7,0x025d0,0x092d0,0x0cab5, //2000
            0x0a950,0x0b4a0,0x0baa4,0x0ad50,0x055d9,0x04ba0,0x0a5b0,0x15176,0x052b0,0x0a930, //2010
            0x07954,0x06aa0,0x0ad50,0x05b52,0x04b60,0x0a6e6,0x0a4e0,0x0d260,0x0ea65,0x0d530, //2028
            0x05aa0,0x076a3,0x096d0,0x04bd7,0x04ad0,0x0a4d0,0x1d0b6,0x0d250,0x0d520,0x0dd45, //2030
            0x0b5a0,0x056d0,0x055b2,0x049b0,0x0a577,0x0a4b0,0x0aa50,0x1b255,0x06d20,0x0ada0  //2040--2049
    };

    private OtherUtil() {
    }

    /**
     * 输出生辰相关信息，参数为阳历日期
     *
     * @param timeStr 时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static void printBirthInfo(String timeStr) {
        DateTime date = new DateTime(timeStr);
        printBirthInfo(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), date.getHourOfDay(), date.getMinuteOfHour(), date.getSecondOfMinute());
    }

    /**
     * 输出生辰相关信息，参数为阳历日期
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     */
    public static void printBirthInfo(int year, int month, int day, int hour, int minute, int second) {
        //输出生肖
        printZodiacAnimal(year, month, day, hour, minute, second);

        //输出星座
        printConstellation(month, day);

        //阴阳历转换
        Map<String, Integer> result = solarToLunar(year, month, day);
        if(result != null){


            //输出干支纪年信息
//            printTrunkBranchInfo(1993, 12, 24, 11, 15, 30);
            printTrunkBranchInfo(1992, 5, 24, 11, 15, 30);
        }
    }

    /**
     * 输出某一年的纪年信息，包括干支纪年(天干地支产生于汉代)、 在六十甲子中序数
     * 参数为农(阴)历, 仅处理公元后的年
     */
    public static void printTrunkBranchInfo(int year, int month, int day, int hour, int minute, int second) {
        if (year < 0 || month < 0 || day < 0 || minute < 0 || second < 0 || month > 12 || day > 31 || hour > 24 || minute > 60 || second > 60) {
            return;
        }
        System.out.println("阴历" + year + "年" + month + "月" + day + "日，" + hour + "时" + minute + "分" + second + "秒");

        //公元元年是辛酉年, 辛8酉10
        //干的周期是10，支的周期是12，天干按甲癸顺序为1-10，地支按子亥顺序为1-12, a为天干序号，b为地支序号
        int a = year % 10 - 3;
        int b = year % 12 - 3;
        a = a <= 0 ? a + 10 : a;
        b = b <= 0 ? b + 12 : b;
        String yearDes = trunk[a - 1] + branch[b - 1];

        //六十甲子序数，甲子为1， 癸亥为60; a为天干的序数，b为地支的序数，c为所求的六十甲子序数: c=[(a+10-b) mod 10]÷2×12+b
        int cSeq = (a + 10 - b) % 10 * 6 + b;

        //年份对应五行；c为五行的序数，计算函数如下：
        //f(x)=[x+(x mod 2)] / 2；
        //g(x)=[(x-1) mod 6] + 1；
        //c=[f(a)+f(g(b))] mod 5；
        int middleValue = (b - 1) % 6 + 1;
        int cElementSeq = ((a + a % 2) / 2 + (middleValue + middleValue % 2) / 2) % 5;
        String elementInfo = fiveElementPrefix[(cSeq - 1) / 2] + fiveElement[cElementSeq];
        System.out.println(year + "年对应六十甲子序数为: " + cSeq + ", " + yearDes + "年, 五行为" + elementInfo);

        //月干公式为：月干=年干数*2+月份, 末尾1为甲，0(10)为癸
        //月支为每年固定，正月为寅，腊月为丑
        int monthTrunk = (a * 2 + month - 1) % 10;
        System.out.println(month + "月为" + trunk[monthTrunk] + branch[(month + 2) % 12 - 1] + "月");

    }

    /**
     * 计算阴历年，润哪个月(1-12)
     *
     * @param year 阴历年
     * @return 月份，无润月返回0
     */
    private static int getLeapMonth(int year){
        return lunarData[year - 1900] & 0xf;
    }

    /**
     * 获取阴历年的润月天数
     *
     * @param year 阴历年
     * @return 天数
     */
    private static int getLeapMonthDays(int year){
        if(getLeapMonth(year)!=0){
            if((lunarData[year - 1900] & 0xf0000)==0){
                return 29;
            }else {
                return 30;
            }
        }else{
            return 0;
        }
    }

    /**
     * 计算阴历年，月的天数
     *
     * @param year 阴历年
     * @param month 阴历月
     * @return 该月天数
     */
    private static int getMonthDays(int year, int month) {
        if (month > 12 || month < 0) {
            return 0;
        }
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
        int sum = 29*12;
        for(int i=0x8000;i>=0x8;i>>=1){
            if((lunarData[year-1900]&0xfff0&i)!=0){
                sum++;
            }
        }
        return sum+getLeapMonthDays(year);
    }

    /**
     * 计算两个阳历日期相差的天数
     *
     * @param startDate 开始日期
     * @param endDate 截止日期
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

        //calendar1 < calendar2, 减去小的时间在这一年已经过了的天数, 加上大的时间已过的天数
        if(calendar1.before(calendar2)){
            days -= calendar1.get(Calendar.DAY_OF_YEAR);
            days += calendar2.get(Calendar.DAY_OF_YEAR);
            tempCalendar = calendar1;
        }else{
            days -= calendar2.get(Calendar.DAY_OF_YEAR);
            days += calendar1.get(Calendar.DAY_OF_YEAR);
            tempCalendar = calendar2;
        }
        for (int i = 0; i < Math.abs(year2-year1); i++) {

            //获取小的时间当前年的总天数
            days += tempCalendar.getActualMaximum(Calendar.DAY_OF_YEAR);

            //计算下一年
            tempCalendar.add(Calendar.YEAR, 1);
        }
        return days;
    }

    /**
     * 检查阴历日期是否合法
     * @param lunarYear 阴历年
     * @param lunarMonth 阴历月
     * @param lunarDay 阴历日
     * @param leapMonthFlag 闰月标志
     * @return true为合法，false为非法
     */
    private static boolean checkLunarDate(int lunarYear, int lunarMonth, int lunarDay, boolean leapMonthFlag){
        if ((lunarYear < MIN_YEAR) || (lunarYear > MAX_YEAR)) {
            return false;
        }
        if ((lunarMonth < 1) || (lunarMonth > 12)) {
            return false;
        }

        //阴历月最多30天
        if ((lunarDay < 1) || (lunarDay > 30)) {
            return false;
        }

        //计算该年应该闰哪个月
        int leap = getLeapMonth(lunarYear);
        if (leapMonthFlag && (lunarMonth != leap)) {
            return false;
        }
        return true;
    }

    /**
     * 阳历日期转换为阴历日期
     *
     * @param solarDate 阳历日期,格式YYYYMMDD
     * @return 阴历日期
     * @throws Exception
     */
    public static Map<String, Integer> solarToLunar(String solarDate){
        int i;
        int temp = 0;
        int lunarYear;
        int lunarMonth;
        int lunarDay;
        boolean leapMonthFlag =false;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date myDate = null;
        Date startDate = null;
        try {
            myDate = formatter.parse(solarDate);
            startDate = formatter.parse(START_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int offset = daysBetween(startDate,myDate);
        for (i = MIN_YEAR; i <= MAX_YEAR; i++){

            //求当年农历年天数
            temp = getYearDays(i);
            if (offset - temp < 1){
                break;
            }else{
                offset -= temp;
            }
        }
        lunarYear = i;

        //计算该年闰哪个月
        int leapMonth = getLeapMonth(lunarYear);

        //设定当年是否有闰月
        boolean isLeapYear;
        if (leapMonth > 0){
            isLeapYear = true;
        }else{
            isLeapYear = false;
        }
        for (i = 1;  i<=12; i++) {
            if(i==leapMonth+1 && isLeapYear){
                temp = getLeapMonthDays(lunarYear);
                isLeapYear = false;
                leapMonthFlag = true;
                i--;
            }else{
                temp = getMonthDays(lunarYear, i);
            }
            offset -= temp;
            if(offset<=0){
                break;
            }
        }
        offset += temp;
        lunarMonth = i;
        lunarDay = offset;
        Map<String, Integer> data = new HashMap<>();
        data.put("year", lunarYear);
        data.put("month", lunarMonth);
        data.put("day", lunarDay);
        String a = "阴历："+lunarYear+"年"+(leapMonthFlag&&(lunarMonth==leapMonth)?"闰":"")+lunarMonth+"月"+lunarDay+"日";
        return data;

       //https://blog.csdn.net/eacter/article/details/42495291
        //https://baike.baidu.com/item/%E5%B9%B2%E6%94%AF%E7%BA%AA%E5%B9%B4
//        return "阴历："+lunarYear+"年"+(leapMonthFlag&(lunarMonth==leapMonth)?"闰":"")+lunarMonth+"月"+lunarDay+"日";
//        return "阴历："+lunarYear+"年"+(leapMonthFlag&(lunarMonth==leapMonth)?"闰":"")+lunarMonth+"月"+lunarDay+"日";
    }

    /**
     * 阳历日期转为阴历, 参数为阳历的年月日
     *
     * @return 失败返回null, 成功返回map, key包含year, month, day
     */
    private static Map<String, Integer> solarToLunar(int year, int month, int day){
        if(year < 1900 || year > 2049 || month < 0 || month > 12 || day < 0 || day > 31){
            return null;
        }
        Map<String, Integer> data = new HashMap<>();


        return data;
    }

    /**
     * 计算立春时间, 公式为[Y*D+C]-L, 年数的后两位乘以D即0.2422，加上常数C(20世纪为4.6295; 21世纪为3.87; 22世纪为4.15), L为本世纪已经过的闰年数(不包括整千年如2000)
     * 二十四节气是以阳(公)历划分, 立春为24节气之首
     * 立春是每年阳历2月3-2月5, 精确到秒，如1994年立春为2月4日 09:30:56, 时间根据天体运行位置确定，太阳达到黄经315度，实际推算较复杂，当前函数有20秒到5分钟的误差
     *
     * @param year 年份, 仅支持20-22世纪
     * @return 失败返回null, 成功返回map, key包含day, hour, minute, second
     */
    private static Map<String, Integer> getSpringBeginTime(int year) {
        int century = year / 100 + 1;
        if (century >= 20 && century <= 22) {
            double constantC;
            if (century == 20) {
                constantC = 4.6295;
            } else if (century == 21) {
                constantC = 3.87;
            } else {
                constantC = 4.15;
            }
            double springBeginFactor = year % 100 * 0.2422 + constantC - (year % 100 - 1) / 4;
            double springBeginDecimal = springBeginFactor - (int) springBeginFactor;
            int springBeginDayTotalSeconds = (int) (24 * 60 * 60 * springBeginDecimal);
            int springBeginDayHour = springBeginDayTotalSeconds / (60 * 60);
            int springBeginDayMinute = (springBeginDayTotalSeconds - springBeginDayHour * 60 * 60) / 60;
            int springBeginDaySecond = springBeginDayTotalSeconds - springBeginDayHour * 60 * 60 - springBeginDayMinute * 60;
            int day = (int) springBeginFactor;
            System.out.println(year + "年立春时间为: 2月" + day + "日" + springBeginDayHour + "时" + springBeginDayMinute + "分" + springBeginDaySecond + "秒");
            Map<String, Integer> data = new HashMap<>();
            data.put("day", day);
            data.put("hour", springBeginDayHour);
            data.put("minute", springBeginDayMinute);
            data.put("second", springBeginDaySecond);
            return data;
        }
        return null;
    }

    /**
     * 属相按立春划分, 不是除夕(每年最后一天)或春节(初一，每年第一天)
     * 参数均为阳历时间
     *
     * @param year   年 20-22世纪
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     */
    private static void printZodiacAnimal(int year, int month, int day, int hour, int minute, int second) {
        Map<String, Integer> info = getSpringBeginTime(year);
        if (info == null) {
            return;
        }

        //年的地支序号
        int b = (year + 9) % 12;
        b = b == 0 ? 12 : b;
        boolean isLeap = (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
        System.out.print(year + "年是" + zodiacAnimal[b - 1] + "年, ");
        if(isLeap){
            System.out.println("闰年");
        }else{
            System.out.println("平年");
        }
        System.out.print("阳历" + year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒，属相为: ");
        if (month < 3) {
            int springStartHeavy = info.get("day") * 24 * 60 * 60 + info.get("hour") * 60 * 60 + info.get("minute") * 60 + info.get("second");
            int paramHeavy = day * 24 * 60 * 60 + hour * 60 * 60 + minute * 60 + second;
            if (paramHeavy <= springStartHeavy) {
                int zodiacSeq = b - 1;
                zodiacSeq = zodiacSeq == 0 ? 12 : zodiacSeq;
                System.out.println(zodiacAnimal[zodiacSeq - 1]);
                return;
            }
        }
        System.out.println(zodiacAnimal[b - 1]);
    }

    /**
     * 输出星座(星座按阳历划分)
     *
     * @param month 月
     * @param day   日
     */
    public static void printConstellation(int month, int day) {
        System.out.print(month + "月" + day + "日, ");
        if (month == 3 && day >= 21 || month == 4 && day <= 20) {
            System.out.println("白羊座");
        } else if (month == 4 && day >= 21 || month == 5 && day <= 21) {
            System.out.println("金牛座");
        } else if (month == 5 && day >= 22 || month == 6 && day <= 21) {
            System.out.println("双子座");
        } else if (month == 6 && day >= 22 || month == 7 && day <= 22) {
            System.out.println("巨蟹座");
        } else if (month == 7 && day >= 23 || month == 8 && day <= 23) {
            System.out.println("狮子座");
        } else if (month == 8 && day >= 24 || month == 9 && day <= 23) {
            System.out.println("处女座");
        } else if (month == 9 && day >= 24 || month == 10 && day <= 23) {
            System.out.println("天秤座");
        } else if (month == 10 && day >= 24 || month == 11 && day <= 22) {
            System.out.println("天蝎座");
        } else if (month == 11 && day >= 23 || month == 12 && day <= 21) {
            System.out.println("射手座");
        } else if (month == 12 && day >= 22 || month == 1 && day <= 20) {
            System.out.println("魔羯座");
        } else if (month == 1 && day >= 21 || month == 2 && day <= 19) {
            System.out.println("水瓶座");
        } else if (month == 2 && day >= 20 || month == 3 && day <= 20) {
            System.out.println("双鱼座");
        }
    }

    /**
     * 字符打印，输出心形
     *
     * @param isFull 是否输出完整心形，true为整个，false为半个
     */
    public static void printSimpleHeart(boolean isFull) {
        for (double y = 1.5; y > -1.5; y -= 0.1) {
            for (double x = -1.5; x < 1.5; x += 0.05) {
                double a = x * x + y * y - 1;
                if (a * a * a - x * x * y * y * y <= 0) {
                    System.out.print("*");
                } else {
                    if (isFull) {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * 字符打印，输出一箭穿心图案
     */
    public static void printTwoHeart() {
        int i, j, e, a, t1;
        final int I = 8, R = 150;
        for (i = 1, a = I; i <= I / 2; i++, a--) {
            t1 = 6;
            while (t1-- != 0) System.out.print(" ");
            for (j = (int) (I - Math.sqrt(I * I - (a - i) * (a - i))); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * Math.sqrt(I * I - (a - i) * (a - i)); e++)
                System.out.print("*");
            for (j = (int) (2 * (I - Math.sqrt(I * I - (a - i) * (a - i)))); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * Math.sqrt(I * I - (a - i) * (a - i)); e++)
                System.out.print("*");
            for (j = (int) (I - Math.sqrt(I * I - (a - i) * (a - i))); j > 0; j--)
                System.out.print(" ");
            t1 = (int) (I - Math.sqrt(I * I - (a - i) * (a - i)));
            t1 = 2 * t1;
            t1 += (int) (2 * Math.sqrt(I * I - (a - i) * (a - i)));
            t1 = 20 - t1;
            while (t1-- != 0) System.out.print(" ");
            for (j = (int) (I - Math.sqrt(I * I - (a - i) * (a - i))); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * Math.sqrt(I * I - (a - i) * (a - i)); e++)
                System.out.print("*");
            for (j = (int) (2 * (I - Math.sqrt(I * I - (a - i) * (a - i)))); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * Math.sqrt(I * I - (a - i) * (a - i)); e++)
                System.out.print("*");
            for (j = (int) (I - Math.sqrt(I * I - (a - i) * (a - i))); j > 0; j--)
                System.out.print(" ");
            System.out.print("\n");
        }
        for (i = 1; i <= R / 2; i++) {
            if (i % 2 != 0 || i % 3 != 0) continue;
            t1 = 6;
            if (i == 6) System.out.print(">>----");
            else while (t1-- != 0) System.out.print(" ");
            for (j = (int) (R - Math.sqrt(R * R - i * i)); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * (Math.sqrt(R * R - i * i) - (R - 2 * I)); e++)
                System.out.print("*");
            for (j = (int) (R - Math.sqrt(R * R - i * i)); j > 0; j--)
                System.out.print(" ");
            t1 = (int) (R - Math.sqrt(R * R - i * i));
            t1 = 2 * t1;
            t1 += (int) (2 * (Math.sqrt(R * R - i * i) - (R - 2 * I)));
            t1 = 35 - t1;
            if (i == 6)
                System.out.print("LOVE");
            else if (i == 48)
                System.out.print("\t ");
            else
                while (t1-- != 0) System.out.print(" ");
            for (j = (int) (R - Math.sqrt(R * R - i * i)); j > 0; j--)
                System.out.print(" ");
            for (e = 1; e <= 2 * (Math.sqrt(R * R - i * i) - (R - 2 * I)); e++)
                System.out.print("*");
            for (j = (int) (R - Math.sqrt(R * R - i * i)); j > 0; j--)
                System.out.print(" ");
            if (i == 6) System.out.print("----->");
            System.out.print("\n");
        }
    }

}
