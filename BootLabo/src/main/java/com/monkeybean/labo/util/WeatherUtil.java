package com.monkeybean.labo.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.HashMap;

/**
 * 天气工具类
 * <p>
 * Created by MonkeyBean on 2018/7/19.
 */
public class WeatherUtil {

    private static Logger logger = LoggerFactory.getLogger(WeatherUtil.class);

    /**
     * 获取天气信息，soJson免费接口
     *
     * @param city 城市中文名
     * @return 返回null, 则请求异常
     */
    public static String useSoJson(String city) {
        String url = "https://www.sojson.com/open/api/weather/json.shtml";
        HashMap<String, Object> param = new HashMap<>();
        param.put("city", city);
        String res = null;
        try {
            res = OkHttpUtil.doGet(url, param);
        } catch (IOException e) {
            logger.error("getInfoBySoJson, IOException: {}", e);
        }
        return res;
    }

    /**
     * 心知天气，生成签名
     *
     * @param data 签名数据
     * @param key  用户key
     */
    private static String generateSignature(String data, String key) throws SignatureException {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
            result = new sun.misc.BASE64Encoder().encode(rawHmac);
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    /**
     * 心知天气，生成查询日常天气的url
     *
     * @param userId    用户id
     * @param secretKey 用户秘钥
     * @param location  位置
     * @return 成功则返回请求url
     * @throws SignatureException           签名异常
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    private static String generateDiaryWeatherURL(String userId, String secretKey, String location) throws SignatureException, UnsupportedEncodingException {
        String params = "ts=" + System.currentTimeMillis() + "&ttl=30&uid=" + userId;
        String signature = URLEncoder.encode(generateSignature(params, secretKey), "UTF-8");
        return "https://api.seniverse.com/v3/weather/daily.json?" + params + "&sig=" + signature + "&location=" + location + "&language=zh-Hans&&unit=c&&start=0";
    }

    /**
     * 心知天气，获取当前天气状况
     * api文档地址：https://www.seniverse.com/doc
     *
     * @param secretKey 用户秘钥
     * @param location  位置
     * @return 成功则返回数据，失败返回null
     */
    public static String getNowWeather(String secretKey, String location) {
        if (StringUtils.isAllBlank(secretKey, location)) {
            return null;
        }
        String url = "https://api.seniverse.com/v3/weather/now.json?key=" + secretKey + "&location=" + location + "&language=zh-Hans&unit=c";
        String res = null;
        try {
            res = OkHttpUtil.doGet(url);
        } catch (IOException e) {
            logger.error("getNowWeather, IOException: {}", e);
        }
        return res;
    }

    /**
     * 心知天气，获取日常天气
     *
     * @param userId    用户id
     * @param secretKey 用户秘钥
     * @param location  位置
     * @return 成功则返回数据，失败返回null
     */
    public static String getDailyWeather(String userId, String secretKey, String location) {
        if (StringUtils.isAllBlank(userId, secretKey, location)) {
            return null;
        }
        try {
            String url = generateDiaryWeatherURL(userId, secretKey, location);
            String res = null;
            try {
                res = OkHttpUtil.doGet(url);
            } catch (IOException e) {
                logger.error("getDailyWeather, IOException: {}", e);
            }
            return res;
        } catch (SignatureException | UnsupportedEncodingException e) {
            logger.error("getDailyWeather, exception: {}", e);
        }
        return null;
    }
}
