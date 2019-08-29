package com.monkeybean.lb.cache;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.constant.ServerStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存数据
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public final class CacheData {

    /**
     * 参数code与RuleType枚举对象名称的映射
     */
    public final static Map<Integer, String> ruleTypeCodeMap = new ConcurrentHashMap<>();
    /**
     * 参数code与ServerStatus枚举对象名称的映射
     */
    public final static Map<Integer, String> serverStatusCodeMap = new ConcurrentHashMap<>();

    /**
     * 通过标识码获取RuleType枚举对象
     */
    public static RuleType getRuleType(int code) {
        return RuleType.valueOf(ruleTypeCodeMap.get(code));
    }

    /**
     * 通过标识码获取ServerStatus枚举对象
     */
    public static ServerStatus getServerStatus(int code) {
        return ServerStatus.valueOf(serverStatusCodeMap.get(code));
    }
}
