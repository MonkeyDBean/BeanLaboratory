package com.monkeybean.labo.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by MonkeyBean on 2018/9/12.
 */
public final class ApacheHttpUtil {

    private static Logger logger = LoggerFactory.getLogger(ApacheHttpUtil.class);

    private ApacheHttpUtil() {
    }

    /**
     * Map参数转为Json格式
     */
    private static String mapToUrl(Map<String, Object> param) {
        ObjectMapper mapper = new ObjectMapper();
        String url = null;
        try {
            url = URLEncoder.encode(mapper.writeValueAsString(param), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            logger.error("mapToUrl UnsupportedEncodingException e: [{}]", e);
        } catch (JsonProcessingException e) {
            logger.error("mapToUrl JsonProcessingException e: [{}]", e);
        }
        return url;
    }

    /**
     * 发起get请求
     *
     * @param url 请求地址
     */
    public static String doGet(String url) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String result = "{ \"result\" : -1 }";
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            }
            response.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 表单提交
     *
     * @param url    请求url
     * @param params post参数
     * @return 成功返回响应信息, 失败返回null
     */
    public static String doPostForm(String url, Map<String, Object> params) {
        String result = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            List<NameValuePair> valuePairs = new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                valuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            post.setEntity(new UrlEncodedFormEntity(valuePairs, StandardCharsets.UTF_8));
            CloseableHttpResponse response = httpclient.execute(post);
            logger.info("post request, url: [{}], params: [{}], response: [{}]", url, params, response.getStatusLine());
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                EntityUtils.consume(entity);
            }
            response.close();
        } catch (IOException e) {
            logger.info("doPostForm IOException: [{}]", e);
        }
        return result;
    }

    /**
     * http get请求
     *
     * @param param   请求参数
     * @param address 请求地址, 包含路径
     * @return 请求结果字符串
     */
    public static String getRequest(Map<String, Object> param, String address) {
        String getJsonParam = mapToUrl(param);
        String requestUrl = address + "?" + getJsonParam;
        logger.info("getRequest requestUrl: [{}]", requestUrl);
        return doGet(requestUrl);
    }

    /**
     * http请求，一部分参数通过get传递，一部分参数在post中
     *
     * @param paramGet  get参数
     * @param paramPost post参数
     * @param address   请求地址, 包含路径
     * @return 请求结果字符串
     */
    public static String postRequest(Map<String, Object> paramGet, Map<String, Object> paramPost, String address) {
        String getJsonParam = mapToUrl(paramGet);
        String requestUrl = address + "?" + getJsonParam;
        String postStr = JSON.toJSONString(paramPost);
        logger.info("postRequest, requestUrl: [{}], paramPost: [{}]", requestUrl, postStr);
        return doPostForm(requestUrl, paramPost);
    }

}
