package com.monkeybean.security.core;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    /**
     * 成功
     */
    SUCCESS(200),
    /**
     * 失败
     */
    FAIL(400),
    /**
     * 身份未认证
     */
    UNAUTHORIZED(401),
    /**
     * 请求非法(如签名错误),资源禁止访问
     */
    FORBIDDEN(403),
    /**
     * 接口不存在
     */
    NOT_FOUND(404),
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
