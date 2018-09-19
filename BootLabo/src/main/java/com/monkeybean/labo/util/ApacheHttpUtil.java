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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MonkeyBean on 2018/9/12.
 */
public class ApacheHttpUtil {

    private static Logger logger = LoggerFactory.getLogger(ApacheHttpUtil.class);

    /**
     * Map参数转为Json格式
     */
    private static String mapToUrl(HashMap<String, Object> param) {
        ObjectMapper mapper = new ObjectMapper();
        String url = null;
        try {
            url = URLEncoder.encode(mapper.writeValueAsString(param), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("mapToUrl UnsupportedEncodingException e->{}", e);
        } catch (JsonProcessingException e) {
            logger.error("mapToUrl JsonProcessingException e->{}", e);
        }
        return url;
    }

    /**
     * 发起get请求
     *
     * @param url 请求地址
     */
    private static String doGet(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "{ \"result\" : -1 }";
        HttpGet httpGet = new HttpGet(url);
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            logger.error("doGet Exception ->{}", e);
        } finally {
            try {
                if (response != null)
                    response.close();
                httpclient.close();
            } catch (IOException e) {
                logger.error("doGet IOException ->{}", e);
            }
        }
        return result;
    }

    /**
     * 表单提交
     *
     * @param url    请求url
     * @param params post参数
     */
    private static String doPostForm(String url, Map<String, Object> params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpPost post = new HttpPost(url);
        String result = "-1";
        List<NameValuePair> valuePairs = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            valuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(valuePairs, "UTF-8"));
            response = httpclient.execute(post);
            logger.info("post request, url: {}, params: {}, response: {}", url, params, response.getStatusLine());
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            logger.error("send post error, url: {}, params: {}", url, params);
        } finally {
            try {
                if (response != null)
                    response.close();
                httpclient.close();
            } catch (IOException e) {
                logger.error("doPostForm IOException ->{}", e);
            }
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
    public static String getRequest(HashMap<String, Object> param, String address) {
        String getJsonParam = mapToUrl(param);
        String requestUrl = address + "?" + getJsonParam;
        logger.info("getRequest requestUrl: {}" + requestUrl);
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
    public static String postRequest(HashMap<String, Object> paramGet, HashMap<String, Object> paramPost, String address) {
        String getJsonParam = mapToUrl(paramGet);
        String requestUrl = address + "?" + getJsonParam;
        logger.info("postRequest, requestUrl: {}, paramPost: {}", requestUrl, JSON.toJSONString(paramPost));
        return doPostForm(requestUrl, paramPost);
    }

}
