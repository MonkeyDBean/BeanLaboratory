package com.monkeybean.labo.component.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Properties;
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

    /**
     * 仅测试构建传参
     */
    private String testStr;

    /**
     * 新浪应用key
     */
    private String sinaAppKey;

    /**
     * 微信appId
     */
    private String wxAppId;

    /**
     * 微信appKey
     */
    private String wxAppKey;

    /**
     * 百度token
     */
    private String baiduToken;

    /**
     * 短链接域名
     */
    private String shortDomian;

    /**
     * 短链接密钥
     */
    private String shortSecret;

    /**
     * 重新加载配置的密钥
     * 此方案为不使用SpringCloud配置中心及Apollo重载配置(仅测试, 不推荐)
     */
    private String reloadKey;

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

    public String getTestStr() {
        return testStr;
    }

    public void setTestStr(String testStr) {
        this.testStr = testStr;
    }

    public String getSinaAppKey() {
        return sinaAppKey;
    }

    public void setSinaAppKey(String sinaAppKey) {
        this.sinaAppKey = sinaAppKey;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getWxAppKey() {
        return wxAppKey;
    }

    public void setWxAppKey(String wxAppKey) {
        this.wxAppKey = wxAppKey;
    }

    public String getBaiduToken() {
        return baiduToken;
    }

    public void setBaiduToken(String baiduToken) {
        this.baiduToken = baiduToken;
    }

    public String getShortDomian() {
        return shortDomian;
    }

    public void setShortDomian(String shortDomian) {
        this.shortDomian = shortDomian;
    }

    public String getShortSecret() {
        return shortSecret;
    }

    public void setShortSecret(String shortSecret) {
        this.shortSecret = shortSecret;
    }

    public String getReloadKey() {
        return reloadKey;
    }

    public void setReloadKey(String reloadKey) {
        this.reloadKey = reloadKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        //对正则预编译以提高性能
        if (!StringUtils.isEmpty(imageRegex)) {
            this.imagePattern = Pattern.compile(imageRegex);
        }
        logger.info("OtherConfig properties init or refresh");
    }

    /**
     * 重新加载配置
     */
    public void reloadConfig(Properties properties) throws Exception {
        final String prefix = "other.";
        this.sqlSalt = properties.getProperty(prefix + "sqlSalt");
        this.reCaptchaSecretKey = properties.getProperty(prefix + "reCaptchaSecretKey");
        this.loginTempKey = properties.getProperty(prefix + "loginTempKey");
        this.simulate = Boolean.parseBoolean((properties.getProperty(prefix + "simulate")));
        this.imageMaxSize = Integer.parseInt(properties.getProperty(prefix + "imageMaxSize"));
        this.baseStorePath = properties.getProperty(prefix + "baseStorePath");
        this.baseAccessPath = properties.getProperty(prefix + "baseAccessPath");
        this.mailActiveAddress = properties.getProperty(prefix + "mailActiveAddress");
        this.dailyRequestMaxNum = Integer.parseInt(properties.getProperty(prefix + "dailyRequestMaxNum"));
        this.mailSendMaxNum = Integer.parseInt(properties.getProperty(prefix + "mailSendMaxNum"));
        this.weatherUserId = properties.getProperty(prefix + "weatherUserId");
        this.weatherApiKey = properties.getProperty(prefix + "weatherApiKey");
        this.imageRegex = properties.getProperty(prefix + "imageRegex");
        this.testStr = properties.getProperty(prefix + "testStr");
        this.sinaAppKey = properties.getProperty(prefix + "sinaAppKey");
        this.wxAppId = properties.getProperty(prefix + "wxAppId");
        this.wxAppKey = properties.getProperty(prefix + "wxAppKey");
        this.baiduToken = properties.getProperty(prefix + "baiduToken");
        this.shortDomian = properties.getProperty(prefix + "shortDomian");
        this.shortSecret = properties.getProperty(prefix + "shortSecret");
        this.reloadKey = properties.getProperty(prefix + "reloadKey");
        this.afterPropertiesSet();
        logger.info("otherConfig reload finish, curTime: [{}]", System.currentTimeMillis());
    }
}
