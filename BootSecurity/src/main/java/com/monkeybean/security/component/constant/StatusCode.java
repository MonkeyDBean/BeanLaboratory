package com.monkeybean.security.component.constant;

/**
 * 响应状态码
 * <p>
 * Created by MonkeyBean on 2019/4/20.
 */
public enum StatusCode {
    SUCCESS(0, "success", "请求成功"),

    USER_NAME_PASSWORD_ERROR(1000, "user name or password error", "用户名或密码错误"),

    INVALID_PARAM(400, "invalid param", "参数非法"),
    UNAUTHORIZED(401, "unauthorized", "请先进行登录"),
    FORBIDDEN(403, "forbidden", "权限不足"),
    NOT_FOUND(404, "not found", "接口不存在"),

    INTERNAL_SERVER_ERROR(500, "internal server error", "服务器内部错误");

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
