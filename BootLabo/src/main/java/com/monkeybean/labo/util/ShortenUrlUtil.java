package com.monkeybean.labo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
public class ShortenUrlUtil {
    private static Logger logger = LoggerFactory.getLogger(ShortenUrlUtil.class);

    /**
     * 生成短链接标记数组
     *
     * @param originUrl 需要转换的长链接
     * @param secretKey 用于拼接url作为混合密钥
     * @return 短链接标记数组
     */
    public static String[] generateShortUrl(String originUrl, String secretKey) {

        //生成短链接的字符池
        final String[] charPool = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        String md5Str = DigestUtils.md5Hex(secretKey + originUrl);
        String[] flagArray = new String[4];
        for (int i = 0; i < 4; i++) {

            //分组, 8位一组
            String tempSubString = md5Str.substring(i * 8, i * 8 + 8);
            long tempLong = Long.parseLong(tempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {

                //16进制0x0000003D对应10进制61, 61为charPool数组的索引最大值, 在线进制转换: http://tool.oschina.net/hexconvert/
                long index = 0x0000003D & tempLong;

                //把取得的字符相加
                outChars += charPool[(int) index];

                //每次循环按位右移5位
                tempLong = tempLong >> 5;
            }

            // 把字符串存入对应索引的输出数组
            flagArray[i] = outChars;
            logger.info("originUrl is [{}], generate shortFlag is [{}]", originUrl, outChars);
        }
        return flagArray;
    }

    /**
     * 将长链接转为短链接(调用新浪生成短链接的API)
     * reference: https://open.weibo.com/wiki/index.php?title=Short_url/shorten&oldid=3305
     *
     * @param originUrl 需要转换的长链接
     * @param appKey    申请应用时分配的应用唯一身份
     * @return 成功返回转换后的短链接, 失败返回null
     */
    public static String convertShortUrlBySina(String originUrl, String appKey) {
        final String apiPrefix = "http://api.t.sina.com.cn/short_url/shorten.json";
        String longUrl;
        try {
            longUrl = URLEncoder.encode(originUrl, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            logger.error("convertShortUrlBySina UnsupportedEncodingException: [{}]", e);
            return null;
        }
        String requestUrl = apiPrefix + "?source=" + appKey + "&url_long=" + longUrl;
        return ApacheHttpUtil.doGet(requestUrl);
    }

    /**
     * 将长链接转为短链接(调用百度短网址API)
     * reference: https://dwz.cn/console/apidoc
     *
     * @param originUrl 需要转换的长链接, 仅支持域名, 不支持ip
     * @param token     密钥
     * @return 返回转换后的短链接
     */
    public static String convertShortUrlByBaidu(String originUrl, String token) {
        final String apiUrl = "https://dwz.cn/admin/v2/create";
        JSONObject body = new JSONObject();
        body.put("Url", originUrl);
        //body.put("TermOfValidity", "long-term");
        body.put("TermOfValidity", "1-year");
        return OkHttpUtil.doPost(apiUrl, body.toJSONString(), token);
    }

}

