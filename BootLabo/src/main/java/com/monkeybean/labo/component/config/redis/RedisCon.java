package com.monkeybean.labo.component.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by MonkeyBean on 2019/7/28.
 */
@ConfigurationProperties(prefix = "redis")
@Component
public class RedisCon {

    /**
     * 连接地址
     */
    private String address;

    /**
     * 连接端口号
     */
    private int port;

    /**
     * 授权密码
     */
    private String password;

    /**
     * 数据库索引
     */
    private int dbIndex;

    /**
     * 连接超时时间，单位毫秒
     */
    private int connectTimeout;

    /**
     * 读超时时间，单位毫秒
     */
    private int readTimeout;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
