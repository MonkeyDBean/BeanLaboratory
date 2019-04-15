package com.monkeybean.labo.component.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 调用InitializingBean的afterPropertiesSet方法设置参数, 效果与构造函数内设置相同
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@ConfigurationProperties(prefix = "other")
@Component
public class OtherConfig implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(OtherConfig.class);

    /**
     * 数据库，账户密码盐，配置后不可更新
     */
    private String sqlSalt;

    /**
     * Google reCaptcha, secret key
     */
    private String reCaptchaSecretKey;

    /**
     * 临时登录key
     */
    private String loginTempKey;

    /**
     * 是否模拟, 如模拟则不请求Google进行reCaptcha身份认证
     */
    private boolean simulate;

    /**
     * 上传图片最大尺寸, 单位MB
     */
    private int imageMaxSize;

    /**
     * 图片存放基础路径
     */
    private String baseStorePath;

    /**
     * 图片访问基础路径
     */
    private String baseAccessPath;

    /**
     * 邮箱激活地址
     */
    private String mailActiveAddress;

    /**
     * 每个接口每日请求最大次数
     */
    private int dailyRequestMaxNum;

    /**
     * 每日邮件发送的最大次数
     */
    private int mailSendMaxNum;

    /**
     * 心知天气 user id
     */
    private String weatherUserId;

    /**
     * 心知天气，api key
     */
    private String weatherApiKey;

    /**
     * 图片正则
     */
    private String imageRegex;

    private Pattern imagePattern;

    public String getSqlSalt() {
        return sqlSalt;
    }

    public void setSqlSalt(String sqlSalt) {
        this.sqlSalt = sqlSalt;
    }

    public String getReCaptchaSecretKey() {
        return reCaptchaSecretKey;
    }

    public void setReCaptchaSecretKey(String reCaptchaSecretKey) {
        this.reCaptchaSecretKey = reCaptchaSecretKey;
    }

    public String getLoginTempKey() {
        return loginTempKey;
    }

    public void setLoginTempKey(String loginTempKey) {
        this.loginTempKey = loginTempKey;
    }

    public boolean isSimulate() {
        return simulate;
    }

    public void setSimulate(boolean simulate) {
        this.simulate = simulate;
    }

    public int getImageMaxSize() {
        return imageMaxSize;
    }

    public void setImageMaxSize(int imageMaxSize) {
        this.imageMaxSize = imageMaxSize;
    }

    public String getBaseStorePath() {
        return baseStorePath;
    }

    public void setBaseStorePath(String baseStorePath) {
        this.baseStorePath = baseStorePath;
    }

    public String getBaseAccessPath() {
        return baseAccessPath;
    }

    public void setBaseAccessPath(String baseAccessPath) {
        this.baseAccessPath = baseAccessPath;
    }

    public String getMailActiveAddress() {
        return mailActiveAddress;
    }

    public void setMailActiveAddress(String mailActiveAddress) {
        this.mailActiveAddress = mailActiveAddress;
    }

    public int getDailyRequestMaxNum() {
        return dailyRequestMaxNum;
    }

    public void setDailyRequestMaxNum(int dailyRequestMaxNum) {
        this.dailyRequestMaxNum = dailyRequestMaxNum;
    }

    public int getMailSendMaxNum() {
        return mailSendMaxNum;
    }

    public void setMailSendMaxNum(int mailSendMaxNum) {
        this.mailSendMaxNum = mailSendMaxNum;
    }

    public String getWeatherUserId() {
        return weatherUserId;
    }

    public void setWeatherUserId(String weatherUserId) {
        this.weatherUserId = weatherUserId;
    }

    public String getWeatherApiKey() {
        return weatherApiKey;
    }

    public void setWeatherApiKey(String weatherApiKey) {
        this.weatherApiKey = weatherApiKey;
    }

    public String getImageRegex() {
        return imageRegex;
    }

    public void setImageRegex(String imageRegex) {
        this.imageRegex = imageRegex;
    }

    public Pattern getImagePattern() {
        return imagePattern;
    }

    public void setImagePattern(Pattern imagePattern) {
        this.imagePattern = imagePattern;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        //对正则预编译以提高性能
        if (!StringUtils.isEmpty(imageRegex)) {
            this.imagePattern = Pattern.compile(imageRegex);
        }
        logger.info("OtherConfig properties init or refresh");
    }
}
