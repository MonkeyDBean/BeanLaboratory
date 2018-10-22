package com.monkeybean.labo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 合法性校验
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class LegalUtil {

    private static Logger logger = LoggerFactory.getLogger(LegalUtil.class);
    /**
     * <pre>
     *
     * 省、直辖市代码表：
     *     11 : 北京  12 : 天津  13 : 河北       14 : 山西  15 : 内蒙古
     *     21 : 辽宁  22 : 吉林  23 : 黑龙江  31 : 上海  32 : 江苏
     *     33 : 浙江  34 : 安徽  35 : 福建       36 : 江西  37 : 山东
     *     41 : 河南  42 : 湖北  43 : 湖南       44 : 广东  45 : 广西      46 : 海南
     *     50 : 重庆  51 : 四川  52 : 贵州       53 : 云南  54 : 西藏
     *     61 : 陕西  62 : 甘肃  63 : 青海       64 : 宁夏  65 : 新疆
     *     71 : 台湾
     *     81 : 香港  82 : 澳门
     *     91 : 国外
     * </pre>
     */
    private static String[] cityCode = {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54",
            "61", "62", "63", "64", "65", "71", "81", "82", "91"};
    /**
     * 每位加权因子
     */
    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private LegalUtil() {
    }

    /**
     * 判断是否为合法unix时间戳，毫秒级，到2030年
     */
    public static boolean isLegalTimestamp(String str) {
        String regExp = "^1[5-9]\\d{11}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 手机号的合法性判断
     */
    public static boolean isPhoneLegal(String str) {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：简单匹配
     */
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^1[3456789]\\d{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检测是否为合法性微信号
     * 参考：http://kf.qq.com/touch/faq/120813euEJVf141212Vfi6fA.html
     * 6-20个字母、数字、下划线和减号，必须以字母开头（字母不区分大小写），不支持设置中文
     */
    public static boolean isLegalWeiXin(String str) {
        String regExp = "^[a-zA-Z][-_a-zA-Z0-9]{5,19}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检测时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static boolean isLegalTime(String str) {
        String regExp = "^(((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)) (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检测邮件格式合法性
     */
    public static boolean isMailLegal(String str) {
        String regExp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 中文姓名匹配
     */
    public static boolean matchName(String name) {
        if (name == null || "".equals(name)) {
            return false;
        }
        return name.matches("[\u4e00-\u9fa5]{2,20}");
    }

    /**
     * 是否为qq号
     */
    public static boolean isQQ(String str) {
        String regExp = "[1-9][0-9]{4,10}";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 匹配是否为Ipv4地址
     */
    public static boolean isIpv4(String str) {
        String regExp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 匹配是否为boolean类型（true or false）
     */
    public static boolean isBoolean(String str) {
        String regExp = "^((?i)(true)|(false))$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str.toLowerCase());
        return m.matches();
    }

    /**
     * 验证身份证格式的合法性
     *
     * @param identity 身份证
     * @return 合法返回true，否则返回false
     */
    public static boolean matchIdentity(String identity) {
        if (identity == null || "".equals(identity)) {
            return false;
        }
        if (identity.length() == 15) {
            return validate15IDCard(identity);
        }
        return validate18IdCard(identity);
    }

    /**
     * 判断18位身份证的合法性
     * <p>
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * <p>
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     * <p>
     * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码； 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性； 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。
     * <p>
     * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * <p>
     * 2.将这17位数字和系数相乘的结果相加。
     * <p>
     * 3.用加出来和除以11，看余数是多少
     * <p>
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
     * <p>
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     *
     * @param idCard 18位身份证号
     * @return 验证成功返回true
     */
    private static boolean validate18IdCard(String idCard) {
        if (idCard == null) {
            return false;
        }

        // 非18位为假
        if (idCard.length() != 18) {
            return false;
        }
        // 获取前17位
        String idCard17 = idCard.substring(0, 17);

        // 前17位全部为数字
        if (!isDigital(idCard17)) {
            return false;
        }

        String provinceId = idCard.substring(0, 2);
        // 校验省份
        if (!checkProvinceId(provinceId)) {
            return false;
        }

        // 校验出生日期
        String birthday = idCard.substring(6, 14);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date birthDate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthDate);
            if (!tmpDate.equals(birthday)) {// 出生年月日不正确
                return false;
            }

        } catch (ParseException e) {
            logger.error("validate18IdCard, System, ParseException: {}", e);
            return false;
        }

        // 获取第18位
        String idCard18Code = idCard.substring(17, 18);
        char[] c = idCard17.toCharArray();
        int[] bit = convertCharToInt(c);
        int sum17 = getPowerSum(bit);

        // 将和值与11取模得到余数进行校验码判断
        String checkCode = getCheckCodeBySum(sum17);
        if (null == checkCode) {
            return false;
        }
        // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
        return idCard18Code.equalsIgnoreCase(checkCode);
    }

    /**
     * 校验15位身份证
     * 只校验省份和出生年月日
     *
     * @param idCard 15位身份证号
     * @return 验证成功返回true
     */
    private static boolean validate15IDCard(String idCard) {
        if (idCard == null) {
            return false;
        }
        // 非15位为假
        if (idCard.length() != 15) {
            return false;
        }

        // 15全部为数字
        if (!isDigital(idCard)) {
            return false;
        }

        String provinceId = idCard.substring(0, 2);

        // 校验省份
        if (!checkProvinceId(provinceId)) {
            return false;
        }
        String birthday = idCard.substring(6, 12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        try {
            Date birthDate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthDate);
            if (!tmpDate.equals(birthday)) {// 身份证日期错误
                return false;
            }
        } catch (ParseException e) {
            logger.error("validate15IDCard, System, ParseException: {}", e);
            return false;
        }
        return true;
    }

    /**
     * 校验省份
     *
     * @param provinceId 省份Id
     * @return 合法返回TRUE，否则返回FALSE
     */
    private static boolean checkProvinceId(String provinceId) {
        for (String id : cityCode) {
            if (id.equals(provinceId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数字验证
     *
     * @param str 待校验字符串
     * @return 校验成功返回true
     */
    private static boolean isDigital(String str) {
        return str.matches("^[0-9]*$");
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     */
    private static int getPowerSum(int[] bit) {
        int sum = 0;
        if (power.length != bit.length) {
            return sum;
        }
        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < power.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     *
     * @return 校验位
     */
    private static String getCheckCodeBySum(int sum17) {
        String checkCode = null;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
            default:
        }
        return checkCode;
    }

    /**
     * 将字符数组转为整型数组
     */
    private static int[] convertCharToInt(char[] c) {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }

}
