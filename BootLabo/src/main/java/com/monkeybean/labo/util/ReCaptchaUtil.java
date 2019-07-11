package com.monkeybean.labo.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Google验证码，验证not robot
 * 文档地址：https://developers.google.com/recaptcha/docs/display
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class ReCaptchaUtil {
    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static Logger logger = LoggerFactory.getLogger(ReCaptchaUtil.class);

    private ReCaptchaUtil() {
    }

    /**
     * 验证reCaptcha
     *
     * @param secret   密钥
     * @param response user response token provided by reCaptcha
     * @param ip       用户ip
     * @return 验证通过返回true, 失败返回false
     */
    public static boolean verifyReCaptcha(String secret, String response, String ip) {
        HashMap<String, Object> requestParam = new HashMap<>();
        requestParam.put("secret", secret);
        requestParam.put("response", response);
        requestParam.put("remoteip", ip);
        String resultStr = OkHttpUtil.doGet(VERIFY_URL, requestParam);
        return resultStr != null && JSONObject.parseObject(resultStr).getBooleanValue("success");
    }

}
