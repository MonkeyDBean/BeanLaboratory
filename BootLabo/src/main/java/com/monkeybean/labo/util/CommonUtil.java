package com.monkeybean.labo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 其他通用工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class CommonUtil {

    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private CommonUtil() {
    }

    /**
     * 获取数字某位的值
     *
     * @param num   整数数字
     * @param index 非负数, 0:个位, 1:十位, 2:百位...
     * @return 返回数字某位，参数不合法则返回0
     */
    public static int getNum(int num, int index) {
        return (num / (int) Math.pow(10, index)) % 10;
    }

    /**
     * 格式化为小数点两位
     */
    public static String getString2(String str) {
        Double d = Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    public static String getString2(int value) {
        return getString2(String.valueOf(value));
    }

    public static String getString2(double value) {
        return getString2(String.valueOf(value));
    }

    /**
     * 检查密码是否过于简单, 6-20位，包含字母和数字，该方法可用正则替换
     */
    public static boolean checkPassword(String str) {
        boolean isDigit = false;
        boolean isLetter = false;
        int length = str.length();
        if (length < 6 || length > 20)
            return false;
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {
                isLetter = true;
            } else {
                return false;
            }
        }
        return isDigit && isLetter;
    }

    /**
     * 读取文件内容
     *
     * @param file 文件对象
     * @return 成功返回字符串, 失败返回null
     */
    public static String getContent(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line = "";
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            return builder.toString();
        } catch (IOException e) {
            logger.error("getReader IOException： {}", e);
            return null;
        }
    }

    /**
     * 生成cron表达式字符串
     *
     * @param hour   时
     * @param minute 分
     * @param week   每周几
     * @param day    每月几号
     * @param type   类型, 1为每天定点执行, 2为每周定点执行, 3为每月定点执行
     * @return cron表达式
     */
    public static String generateCronStr(int hour, int minute, int week, int day, int type) {
        String cronStr;
        switch (type) {
            case 1:
                cronStr = String.format("0 %s %s * * ?", minute, hour);
                break;
            case 2:
                cronStr = String.format("0 %s %s ? * %s", minute, hour, week);
                break;
            case 3:
                cronStr = String.format("0 %s %s %s * ?", minute, hour, day);
                break;
            default:
                cronStr = "0 0 0 * * ?";
        }
        return cronStr;
    }
}
