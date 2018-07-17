package com.monkeybean.labo.component.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
@ConfigurationProperties(prefix = "proxy")
@Component
public class HttpProxyConfig {

    /**
     * 是否启用代理
     */
    private boolean enableProxy;

    /**
     * 代理地址
     */
    private String proxyAddress;

    /**
     * 代理端口号
     */
    private int proxyPort;

    public boolean isEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public String getProxyAddress() {
        return proxyAddress;
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }
}
