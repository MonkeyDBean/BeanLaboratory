package com.monkeybean.labo.predefine;

import java.util.HashSet;

/**
 * 常量值
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class ConstValue {

    /**
     * 合法日期正则（YYYY-MM-DD）
     */
    public static final String LEGAL_DATE_FORMAT = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))$";

    /**
     * 合法时间正则（yyyy-MM-dd HH:mm:ss）
     */
    public static final String LEGAL_TIME_FORMAT = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)) (([01]\\d)|(2[0123])):([0-5]\\d):([0-5]\\d)$";

    /**
     * 合法大陆手机号正则
     */
    public static final String LEGAL_PHONE = "^1[3456789]\\d{9}$";

    /**
     * 合法邮箱正则
     */
    public static final String LEGAL_MAIL = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z]{2,5}$";

    /**
     * 合法密码正则
     * 6-20位,首位为字母，由字母、数字组成
     */
    public static final String LEGAL_PASSWORD = "^[a-zA-Z](?![a-zA-Z]+$)[0-9A-Za-z]{5,19}$";

    /**
     * 合法非负整数正则
     */
    public static final String LEGAL_UNSIGNED_INT = "^[0-9]*$";

    /**
     * 合法正整数正则
     */
    public static final String LEGAL_POSITIVE_INT = "^[1-9][0-9]*$";

    /**
     * 合法boolean类型的正则判断
     */
    public static final String LEGAL_BOOLEAN = "^((?i)(true)|(false))$";

    /**
     * 合法时间戳正则，毫秒, 时间范围为2017.7.15-2030年
     */
    public static final String LEGAL_TIMESTAMP = "^1[5-9]\\d{11}$";

    /**
     * 合法图片名正则
     */
    public static final String LEGAL_IMAGE_NAME = "^(.+?)\\.(png|jpg|gif|jpeg)$";
    /**
     * 发送验证码成功及失败标志
     */
    public static final String SEND_SUCCESS = "success";
    public static final String SEND_FAIL = "fail";
    /**
     * session的expires设置为2小时
     */
    public static final int MAX_SESSION_TIME = 7200;
    /**
     * 图片格式
     */
    public static final String IMAGE_SUPPORT_PATTERN = "png,jpg,jpeg,gif";
    /**
     * 图片名称最小长度, 如: 1.png
     */
    public static final int IMAGE_NAME_MIN_LEN = 5;
    /**
     * 请求的合法时间区间, 毫秒
     */
    public static final long TIME_OUT = 60 * 1000;
    /**
     * 初始账户id
     */
    public static final int ID_START = 600;
    /**
     * 配置表，种子基数配置名称
     */
    public static final String idBaseName = "idBaseSeed";
    /**
     * 预留账户Id，尾数及位数和
     */
    public static final HashSet<Integer> reservedIdTail = new HashSet<Integer>() {{
        add(6);
        add(8);
    }};
    public static final HashSet<Integer> reservedIdSum = new HashSet<Integer>() {{
        add(6);
        add(12);
        add(24);
    }};
    /**
     * 图片访问权限类型，0为仅私有，1为共享, 2为所有
     */
    public static final int IMAGE_ACCESS_PRIVATE = 0;
    public static final int IMAGE_ACCESS_SHARE = 1;
    public static final int IMAGE_ACCESS_ALL = 2;

}
