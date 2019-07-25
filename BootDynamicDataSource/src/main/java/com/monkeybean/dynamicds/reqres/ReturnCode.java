package com.monkeybean.dynamicds.reqres;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
public enum ReturnCode {
    SUCCESS(0, "请求成功"),
    AUTHORIZED_FAILED(401, "认证失败"),
    SYSTEM_EXCEPTION(10000, "系统异常");

    int code;
    String description;

    ReturnCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
