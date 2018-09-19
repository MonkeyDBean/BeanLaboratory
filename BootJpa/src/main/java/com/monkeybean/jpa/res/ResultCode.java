package com.monkeybean.jpa.res;

/**
 * 响应码枚举
 */
public enum ResultCode {
    SUCCESS(0, "请求成功"),
    ARGS_WRONG(10001, "缺少参数"),
    SERVER_ERROR(10002, "内部错误");

    public int code;
    public String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
