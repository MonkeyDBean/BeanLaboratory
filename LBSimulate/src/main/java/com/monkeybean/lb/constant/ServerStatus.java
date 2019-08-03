package com.monkeybean.lb.constant;

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
    }

    public int getCode() {
        return code;
    }
}
