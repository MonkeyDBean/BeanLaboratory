package com.monkeybean.algorithm.category;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * 干支纪年，生效纪年，星座等
 * 验证准确性：https://www.buyiju.com/bazi/#csshow
 * <p>
 * Created by MonkeyBean on 2019/3/19.
 */
public class FortuneConstellation {
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
     * 日干支推算，年数表(1950-2049)
     */
    private static Map<Integer, Integer> dayYearSeq = new HashMap<Integer, Integer>() {{
        put(1950, 31);
        put(1951, 36);
        put(1952, 42);
        put(1953, 47);
        put(1954, 52);
        put(1955, 57);
        put(1956, 3);
        put(1957, 8);
        put(1958, 13);
        put(1959, 18);
        put(1960, 24);
        put(1961, 29);
        put(1962, 34);
        put(1963, 39);
        put(1964, 45);
        put(1965, 50);
        put(1966, 55);
        put(1967, 0);
        put(1968, 6);
        put(1969, 11);
        put(1970, 16);
        put(1971, 21);
        put(1972, 27);
        put(1973, 32);
        put(1974, 37);
        put(1975, 42);
        put(1976, 48);
        put(1977, 53);
        put(1978, 58);
        put(1979, 3);
        put(1980, 9);
        put(1981, 14);
        put(1982, 19);
        put(1983, 24);
        put(1984, 30);
        put(1985, 35);
        put(1986, 40);
        put(1987, 45);
        put(1988, 51);
        put(1989, 56);
        put(1990, 1);
        put(1991, 6);
        put(1992, 12);
        put(1993, 17);
        put(1994, 22);
        put(1995, 27);
        put(1996, 33);
        put(1997, 38);
        put(1998, 43);
        put(1999, 48);
        put(2000, 54);
        put(2001, 59);
        put(2002, 4);
        put(2003, 9);
        put(2004, 15);
        put(2005, 20);
        put(2006, 25);
        put(2007, 30);
        put(2008, 36);
        put(2009, 41);
        put(2010, 46);
        put(2011, 51);
        put(2012, 57);
        put(2013, 2);
        put(2014, 7);
        put(2015, 12);
        put(2016, 18);
        put(2017, 23);
        put(2018, 28);
        put(2019, 33);
        put(2020, 39);
        put(2021, 44);
        put(2022, 49);
        put(2023, 54);
        put(2024, 0);
        put(2025, 5);
        put(2026, 10);
        put(2027, 15);
        put(2028, 21);
        put(2029, 26);
        put(2030, 31);
        put(2031, 36);
        put(2032, 42);
        put(2033, 47);
        put(2034, 52);
        put(2035, 57);
        put(2036, 3);
        put(2037, 8);
        put(2038, 13);
        put(2039, 18);
        put(2040, 24);
        put(2041, 29);
        put(2042, 34);
        put(2043, 39);
        put(2044, 45);
        put(2045, 50);
        put(2046, 55);
        put(2047, 0);
        put(2048, 6);
        put(2049, 11);
    }};

    /**
     * 日干支推算，月数表(顺序为1-12), 万年通用
     */
    private static int[] dayMonthSeq = {6, 37, 0, 31, 1, 32, 2, 33, 4, 34, 5, 35};

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
        System.out.println("************************");

        //输出星座
        printConstellation(month, day);

        //获取立春时间
        Map<String, Integer> springInfo = getSpringBeginTime(year);

        //转为阴历
        Map<String, Integer> result = ChinaDate.solarToLunar(year, month, day);

        //输出生肖
        printZodiacAnimal(year, month, day, hour, minute, second, springInfo, result.get("year"));

        //输出干支纪年等信息
        printTrunkBranchInfo(result.get("year"), result.get("month"), result.get("day"), hour, minute, second, springInfo);
    }

    /**
     * 输出某一年的纪年信息，包括干支纪年(天干地支产生于汉代)、 在六十甲子中序数
     * 参数为农(阴)历, 仅处理公元后的年
     */
    public static void printTrunkBranchInfo(int year, int month, int day, int hour, int minute, int second, Map<String, Integer> info) {
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
        System.out.println(year + "年对应六十甲子序数为: " + cSeq + ", 年五行为" + elementInfo);
        System.out.println(yearDes + "年");

        //月干公式为：月干=年干数*2+月份, 末尾1为甲，0(10)为癸
        //月支为每年固定，正月为寅，腊月为丑
        //若月日同当年立春相同，则月干支为下一月的干支：一定为寅月支
        int monthTrunkSeq = (a * 2 + month - 1) % 10;
        int monthBranchSeq = (month + 2) % 12 - 1;

        //转为阴历，计算日干支
        Map<String, Integer> sonarMap = ChinaDate.lunarToSolar(year, month, day);
        if (sonarMap.get("month") == 2 && sonarMap.get("day") == info.get("day")) {
            monthTrunkSeq++;
            monthBranchSeq++;
        }
        monthTrunkSeq = monthTrunkSeq == 10 ? 0 : monthTrunkSeq;
        System.out.println(trunk[monthTrunkSeq] + branch[monthBranchSeq] + "月");

        if (sonarMap != null) {
            getDayTB(sonarMap.get("year"), sonarMap.get("month"), sonarMap.get("day"), hour);
        }

    }

    /**
     * 通过阳历计算日干支,参数为阳历时间
     * 日干支公式：日干支=年数+月数+日期（和大于60，则减60；1月、2月各天用上一年数）
     */
    public static void getDayTB(int year, int month, int day, int hour) {
        if (year < 1950 || year > 2049 || month < 0 || month > 12 || day < 1 || day > 31) {
            return;
        }
        int dayYear = month < 3 ? year - 1 : year;
        int dayTB = dayYearSeq.get(dayYear) + dayMonthSeq[month - 1] + day;
        dayTB = dayTB > 60 ? dayTB - 60 : dayTB;
        int dayYearTrunk = dayTB % 10 == 0 ? 10 : dayTB % 10;
        int dayYearBranch = dayTB % 12 == 0 ? 12 : dayTB % 12;
        System.out.println(trunk[dayYearTrunk - 1] + branch[dayYearBranch - 1] + "日");

        //计算时干支
        getHourTB(dayYearTrunk, hour);
    }

    /**
     * 计算时干支
     *
     * @param dayTrunkSeq 日干序数
     * @param hour        小时
     */
    public static void getHourTB(int dayTrunkSeq, int hour) {

        //时支公式：时支=小时÷2-1（小时为偶数），时支=（小时+1）÷2-1（小时为奇数）; -1为晨子，11为夜子
        int hourBranchSeq;
        if ((hour & 1) == 0) {
            hourBranchSeq = hour / 2 - 1;
        } else {
            hourBranchSeq = (hour + 1) / 2 - 1;
        }
        String hourBranchStr = branch[(hourBranchSeq + 1) % 12];

        //时干公式：时干=日干×2+时支（晨子=-1，夜子=11）
        int hourTrunkSeq = (dayTrunkSeq * 2 + hourBranchSeq) % 10;
        if (hourTrunkSeq == 0) {
            hourTrunkSeq = 10;
        }
        System.out.println(trunk[hourTrunkSeq - 1] + hourBranchStr + "时");
    }

    /**
     * 计算立春时间, 公式为[Y*D+C]-L, 年数的后两位乘以D即0.2422，加上常数C(20世纪为4.6295; 21世纪为3.87; 22世纪为4.15), L为本世纪已经过的闰年数(不包括整千年如2000)
     * 二十四节气是以阳(公)历划分, 立春为24节气之首
     * 立春是每年阳历2月3-2月5, 精确到秒，如1994年立春为2月4日 09:30:56, 时间根据天体运行位置确定，太阳达到黄经315度，实际推算较复杂，当前函数有20秒到5分钟的误差
     *
     * @param year 年份, 仅支持20-22世纪
     * @return 失败返回null, 成功返回map, key包含day, hour, minute, second
     */
    public static Map<String, Integer> getSpringBeginTime(int year) {
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
     *
     * @param year      阳历年 20-22世纪
     * @param month     阳历月
     * @param day       阳历日
     * @param hour      时
     * @param minute    分
     * @param second    秒
     * @param info      当年立春时间
     * @param lunarYear 阴历年
     */
    public static void printZodiacAnimal(int year, int month, int day, int hour, int minute, int second, Map<String, Integer> info, int lunarYear) {
        if (info == null) {
            return;
        }
        boolean isLeap = (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
        String leapStr;
        if (isLeap) {
            leapStr = "闰年";
        } else {
            leapStr = "平年";
        }
        System.out.println("阳历" + year + "年是" + leapStr);

        //年的地支序号
        int b = (year + 9) % 12;
        b = b == 0 ? 12 : b;
        System.out.println(year + "年是" + zodiacAnimal[b - 1] + "年");

        //另一参数年的地支序号
        int lunarB = (lunarYear + 9) % 12;
        lunarB = lunarB == 0 ? 12 : lunarB;
        if (year != lunarYear) {
            System.out.println(lunarYear + "年是" + zodiacAnimal[lunarB - 1] + "年");
        }
        int springZodiacSeq = lunarB;

        //生肖序数, 按立春划分
        if (month == 2) {
            int springStartHeavy = info.get("day") * 24 * 60 * 60 + info.get("hour") * 60 * 60 + info.get("minute") * 60 + info.get("second");
            int paramHeavy = day * 24 * 60 * 60 + hour * 60 * 60 + minute * 60 + second;
            if (springStartHeavy <= paramHeavy) {
                springZodiacSeq = b;
            }
        }

        System.out.println("阳历" + year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒，按立春划分，属相为: " + zodiacAnimal[springZodiacSeq - 1]
                + ", 按阴历自然年即除夕划分为，属相为：" + zodiacAnimal[lunarB - 1]);
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
}
