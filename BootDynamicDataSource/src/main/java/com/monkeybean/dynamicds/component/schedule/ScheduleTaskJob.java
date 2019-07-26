package com.monkeybean.dynamicds.component.schedule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
public class ScheduleTaskJob {
    /**
     * 存储已有定时任务, key为任务标识
     */
    private static final Map<String, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();

    public static Map<String, ScheduledFuture> getScheduledFutureMap() {
        return scheduledFutureMap;
    }
}
