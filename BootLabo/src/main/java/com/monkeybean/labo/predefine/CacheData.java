package com.monkeybean.labo.predefine;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存
 * <p>
 * Created by MonkeyBean on 2018/5/26.
 */
public class CacheData {

    /**
     * 存储枚举常量的映射，key为code, value为枚举常量名称
     */
    public static ConcurrentHashMap<Integer, String> codeName = new ConcurrentHashMap<>();

    /**
     * 存储邮箱激活key
     * mapKey为激活key, value为11位手机号拼接邮箱地址: $phone + $mail
     */
    public static ConcurrentHashMap<String, String> mailKeyMap = new ConcurrentHashMap<>();

    /**
     * 缓存接口请求次数
     * key为接口路径, value为接口请求次数
     */
    public static ConcurrentHashMap<String, Integer> requestCountMap = new ConcurrentHashMap<>();

    /**
     * 缓存邮件发送次数
     * key为邮箱地址，value为发送次数
     */
    public static ConcurrentHashMap<String, Integer> mailSendNumMap = new ConcurrentHashMap<>();
}
