package com.monkeybean.lb.request;

/**
 * 请求信息, 当前模拟处理的请求格式如 http://127.0.0.1:80?key=test12345
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class RequestInfo {

    /**
     * 原始请求
     */
    private String origin;

    /**
     * 请求Ip
     */
    private String ip;

    /**
     * 用于Key Hash分流计算
     */
    private String key;

    RequestInfo(String origin, String ip, String key) {
        this.origin = origin;
        this.ip = ip;
        this.key = key;
    }

    public String getOrigin() {
        return origin;
    }

    public String getIp() {
        return ip;
    }

    public String getKey() {
        return key;
    }
}
