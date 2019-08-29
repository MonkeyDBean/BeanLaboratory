package com.monkeybean.labo.component.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信配置
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@ConfigurationProperties(prefix = "message")
@Component
public class MessageConfig {

    /**
     * 阿里云appId
     */
    private String accessKeyId;

    /**
     * 阿里云appKey
     */
    private String accessKeySecret;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 模板Id
     */
    private String templateId;

    /**
     * 验证码长度
     */
    private int codeLength;

    /**
     * 验证码是否包含字母
     */
    private boolean hasAlpha;

    /**
     * 每日短信申请的最大次数
     */
    private int maxNum;

    /**
     * 短信申请间隔时间，单位为秒
     */
    private int intervalTime;

    /**
     * 验证码超时时间, 单位为分钟
     */
    private int validCodeTimeOut;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public boolean isHasAlpha() {
        return hasAlpha;
    }

    public void setHasAlpha(boolean hasAlpha) {
        this.hasAlpha = hasAlpha;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getValidCodeTimeOut() {
        return validCodeTimeOut;
    }

    public void setValidCodeTimeOut(int validCodeTimeOut) {
        this.validCodeTimeOut = validCodeTimeOut;
    }

}
