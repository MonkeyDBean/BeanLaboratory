package com.monkeybean.dingtalk.component.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by MonkeyBean on 2019/4/19.
 */
@ConfigurationProperties(prefix = "custominfo")
@Component
public class CustomInfoConfig {
    /**
     * 企业Id
     */
    private String corpId;
    /**
     * 应用key
     */
    private String appKey;
    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 微应用Id
     */
    private String agentId;

    /**
     * 请求签名的接口url
     */
    private String signUrl;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }
}
