package com.monkeybean.dingtalk.reqres;

/**
 * Created by MonkeyBean on 2019/4/19.
 */
public enum StatusCode {
    SUCCESS(0, "Success", "请求成功");
    int code;
    String msg;
    String description;

    StatusCode(int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }
}
