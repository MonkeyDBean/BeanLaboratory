package com.monkeybean.lb.constant;

import com.monkeybean.lb.cache.CacheData;

/**
 * 服务状态
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public enum ServerStatus {

    /**
     * 运行中
     */
    RUNNING(1),

    /**
     * 断开
     */
    DISCONNECT(2);

    private int code;

    ServerStatus(int code) {
        this.code = code;
        CacheData.serverStatusCodeMap.put(code, this.name());
    }

    /**
     * 获取标识码参数
     */
    public int getCode() {
        return code;
    }
}
