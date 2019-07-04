package com.monkeybean.labo.component.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by MonkeyBean on 2019/6/26.
 */
@ConfigurationProperties(prefix = "ftp")
@Component
public class FtpConfig {

    /**
     * ftp地址
     */
    private String address;

    /**
     * 用户名
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    /**
     * 缓存区大小
     */
    private int bufferSize;

    /**
     * 初始连接数
     */
    private int initSize = 1;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getInitSize() {
        return initSize;
    }

    public void setInitSize(int initSize) {
        this.initSize = initSize;
    }
}
