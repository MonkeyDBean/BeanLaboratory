package com.monkeybean.labo.predefine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存数据
 * <p>
 * Created by MonkeyBean on 2018/5/26.
 */
public final class CacheData {

    /**
     * 存储邮箱激活key
     * mapKey为激活key, value为11位手机号拼接邮箱地址: $phone + $mail
     */
    private static final ConcurrentMap<String, String> MAIL_KEY_MAP = new ConcurrentHashMap<>();
    /**
     * 缓存接口请求次数
     * key为接口路径, value为接口请求次数
     */
    private static final ConcurrentMap<String, Integer> REQUEST_COUNT_MAP = new ConcurrentHashMap<>();
    /**
     * 缓存邮件发送次数
     * key为邮箱地址，value为发送次数
     */
    private static final ConcurrentMap<String, Integer> MAIL_SEND_NUM_MAP = new ConcurrentHashMap<>();
    /**
     * 存储枚举常量的映射，key为code, value为枚举常量名称
     */
    private static final ConcurrentMap<Integer, String> CODE_NAME = new ConcurrentHashMap<>();

    private CacheData() {
    }

    public static ConcurrentMap<String, String> getMailKeyMap() {
        return MAIL_KEY_MAP;
    }

    public static ConcurrentMap<String, Integer> getRequestCountMap() {
        return REQUEST_COUNT_MAP;
    }

    public static ConcurrentMap<String, Integer> getMailSendNumMap() {
        return MAIL_SEND_NUM_MAP;
    }

    public static ConcurrentMap<Integer, String> getCodeName() {
        return CODE_NAME;
    }
}
