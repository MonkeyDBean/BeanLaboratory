package com.monkeybean.labo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信授权(公众号、小程序)相关工具类, 微信后台需配置服务器ip白名单
 * <p>
 * 微信网页授权：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
 * 小程序开发文档：https://developers.weixin.qq.com/miniprogram/dev/api/wx.login.html
 * <p>
 * Created by MonkeyBean on 2019/3/22.
 */
public class WxUtil {
    /**
     * 微信长链接转短链接URL
     * 官方文档: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1443433600
     */
    private static final String LONG_SHORT_CONVERT_URL = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=%s";
    /**
     * 微信公众号授权相关服务域名
     */
    private static final String WEB_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    private static final String WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String WEB_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
    private static final String WEB_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=$appId&secret=$appSecret";
    /**
     * 微信小程序授权相关服务域名
     */
    private static final String MINI_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String MINI_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    /**
     * accessToken的有效期为两小时(7200秒), 此处设置为7100秒
     */
    private static final long ACCESS_TOKEN_VALID_TIME = 7100 * 1000L;
    private static Logger logger = LoggerFactory.getLogger(WxUtil.class);
    /**
     * 微信accessToken
     */
    private static volatile String accessToken = null;
    /**
     * 最近一次accessToken的获取时间
     */
    private static volatile Long accessTokenTime = null;

    private WxUtil() {
    }

    /**
     * 微信公众号：获取微信授权链接(用于访问微信服务器, 即获取code), 也可由前端拼接(微信公众号是拼接后直接访问, 微信小程序是前端调用wx.login方法获取code)
     * 微信公众号服务, 访问授权链接, 前端(包括微信重定向的后端)所在域名必须在微信开发后台配置授权; 微信小程序, 若用到服务端, 服务端域名需在小程序开发后台配置
     * 微信oAuth2认证授权, 获取code有两种模式, 后续获取unionid等用户信息的code, 访问链接需要用户在前端授权确认
     *
     * @param appId       应用Id
     * @param redirectUrl 微信重定向url
     * @param custom      自定义参数(多个参数可用特殊符号如*拼接),可传空; 微信服务器仅作中转，原封返回
     * @param isBase      是否仅获取基础信息如openid,若为true,则为静默授权,用户无感知; 若需获取unionId则传false
     * @return 微信授权链接
     */
    public static String getWxAuthUrl(String appId, String redirectUrl, String custom, boolean isBase) {
        String scopeStr;
        if (isBase) {
            scopeStr = "snsapi_base";
        } else {
            scopeStr = "snsapi_userinfo";
        }
        String authUrl = WEB_AUTHORIZE_URL
                + "?appid=" + appId
                + "&redirect_uri=" + redirectUrl
                + "&response_type=code"
                + "&scope=" + scopeStr
                + "&state=" + custom
                + "#wechat_redirect";
        logger.debug("getWxAuthUrl: [{}]", authUrl);
        return authUrl;
    }

    /**
     * 长链接转短链接
     *
     * @param appId     应用Id
     * @param appSecret 应用密钥
     * @param originUrl 长连接
     * @return 成功返回短链接, 失败返回null
     */
    public static String long2ShortUrl(String appId, String appSecret, String originUrl) {
        String accessToken = getAccessTokenCache(appId, appSecret);
        if (accessToken != null) {
            String url = String.format(LONG_SHORT_CONVERT_URL, accessToken);
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("action", "long2short");
            bodyObject.put("long_url", originUrl);
            JSONObject resObject = requestWxPost(url, bodyObject.toJSONString());
            if (resObject != null) {
                return resObject.getString("short_url");
            }
        }
        return null;
    }

    /**
     * 获取微信访问accessToken, 若缓存有效则直接读取, 否则请求微信获取
     * 若服务为多节点, accessToken可放置Redis等共享缓存或持久化存储中
     *
     * @param appId     应用Id
     * @param appSecret 应用密钥
     * @return 失败返回null, 成功返回accessToken
     */
    public static String getAccessTokenCache(String appId, String appSecret) {
        if (accessToken == null || accessTokenTime == null || System.currentTimeMillis() - accessTokenTime > ACCESS_TOKEN_VALID_TIME) {
            String validAccessToken = getAccessToken(appId, appSecret);
            if (validAccessToken != null) {
                accessToken = validAccessToken;
                accessTokenTime = System.currentTimeMillis();
            }
        }
        return accessToken;
    }

    /**
     * 获取微信访问accessToken, 无需授权码(code)
     * 同一参数限200次/天, accessToken有效期为两小时
     *
     * @param appId     应用Id
     * @param appSecret 应用密钥
     * @return 失败返回null, 成功返回accessToken
     */
    private static String getAccessToken(String appId, String appSecret) {
        String url = ACCESS_TOKEN_URL.replace("$appId", appId).replace("$appSecret", appSecret);
        JSONObject jsonObject = requestWx(url);
        String accessToken = null;
        if (jsonObject != null) {
            accessToken = jsonObject.getString("access_token");
        }
        return accessToken;
    }

    /**
     * 微信公众号: 通过code换取网页授权accessToken
     *
     * @param appId     应用Id
     * @param appSecret 应用密钥
     * @param code      授权码
     * @return 失败返回null, 成功则返回数据，包含accessToken, openid
     */
    public static JSONObject getAccessToken(String appId, String appSecret, String code) {
        String url = WEB_ACCESS_TOKEN_URL
                + "?appid=" + appId
                + "&secret=" + appSecret
                + "&code=" + code
                + "&grant_type=authorization_code";
        return requestWx(url);
    }

    /**
     * 微信公众号：通过accessToken获取用户信息
     *
     * @param accessToken 调用凭证
     * @param openid      公众号下的用户唯一标识
     * @return 失败返回null, 成功则返回数据，包含unionid, 用户昵称等基本信息
     */
    public static JSONObject getUserInfo(String accessToken, String openid) {
        String url = WEB_USER_INFO_URL
                + "?access_token=" + accessToken
                + "&openid=" + openid
                + "&lang=zh_CN";
        return requestWx(url);
    }

    /**
     * 微信公众号：获取公众号用于调用微信JS接口的临时票据, 即jsapi_ticket
     *
     * @param accessToken 调用凭证
     */
    public static JSONObject getJsTicket(String accessToken) {
        String url = WEB_TICKET_URL
                + "?access_token=" + accessToken
                + "&type=jsapi";
        return requestWx(url);
    }

    /**
     * 微信小程序：获取会话密钥(session_key)
     *
     * @param appId     小程序Id
     * @param appSecret 应用密钥
     * @param jsCode    登录时,前端获取的code
     * @return 失败返回null, 成功则返回数据，包含session_key, openid, unionid; 安全性考虑，得到的session_key不可返回给前端，需保存在服务端
     */
    public static JSONObject getCode2Session(String appId, String appSecret, String jsCode) {
        String url = MINI_SESSION_URL
                + "?appid=" + appId
                + "&secret=" + appSecret
                + "&js_code=" + jsCode
                + "&grant_type=authorization_code";
        return requestWx(url);
    }

    /**
     * 微信小程序：获取access_token, 绝大多数后台接口时都需使用access_token
     * access_token, 官方目前设置的有效时间为两小时
     *
     * @return 失败返回null, 成功则返回数据，返回的access_token, 后端需存储到缓存中
     */
    public static JSONObject getMiniAccessToken(String appId, String appSecret) {
        String url = MINI_ACCESS_TOKEN_URL
                + "?grant_type=client_credential&appid=" + appId
                + "&secret=" + appSecret;
        return requestWx(url);
    }

    /**
     * 封装请求微信服务器, get请求
     *
     * @param url 请求url
     * @return 成功为微信返回的json数据，失败为null
     */
    private static JSONObject requestWx(String url) {
        String response = OkHttpUtil.doGet(url);
        JSONObject json = JSONObject.parseObject(response);
        logger.debug("requestWx, request url is: [{}], wx res: [{}]", url, json);
        Integer errCode = json.getInteger("errcode");
        if (errCode != null && errCode != 0) {
            logger.warn("requestWx error, request url is: [{}], wx res: [{}]", url, json);
            return null;
        }
        return json;
    }

    /**
     * 封装请求微信服务器, post请求
     *
     * @param url  请求url
     * @param body body参数, json格式
     * @return 成功为微信返回的json数据，失败为null
     */
    private static JSONObject requestWxPost(String url, String body) {
        String response = OkHttpUtil.doPost(url, body);
        JSONObject json = JSONObject.parseObject(response);
        logger.debug("requestWxPost, request url is: [{}], body: [{}], wx res: [{}]", url, body, json);
        Integer errCode = json.getInteger("errcode");
        if (errCode != null && errCode != 0) {
            logger.warn("requestWxPost error, request url is: [{}], body: [{}], wx res: [{}]", url, body, json);
            return null;
        }
        return json;
    }

    /**
     * 微信小程序：数据签名校验, 保证数据不被篡改, 验证数据完整性
     * 开发文档：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/signature.html
     *
     * @param rawData    原始数据
     * @param sessionKey 用户session_key
     * @param originSign 原始签名
     * @return true为校验通过
     */
    public static boolean checkSign(String rawData, String sessionKey, String originSign) {
        if (StringUtils.isAnyEmpty(rawData, sessionKey, originSign)) {
            return false;
        }
        String originDataStr = rawData + sessionKey;
        String calculateSign = DigestUtils.sha1Hex(originDataStr);
        return originSign.equalsIgnoreCase(calculateSign);
    }

}
