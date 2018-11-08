package com.monkeybean.labo.util;

import com.monkeybean.labo.component.config.HttpProxyConfig;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@Component
public class OkHttpUtil {
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 20;
    private static OkHttpClient client = null;

    public OkHttpUtil(@Autowired HttpProxyConfig httpProxyConfig) {

        //OkHttpClient, 默认连接、读写超时时间均为10s
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)  //设置连接超时时间
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)       //设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);   //设置写的超时时间

        //设置本地代理，开发环境使用
        if (httpProxyConfig.isEnableProxy()) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpProxyConfig.getProxyAddress(), httpProxyConfig.getProxyPort())));
        }

        //自定义Dispatcher(内部调度机制为final, 维护三个线程队列：readyAsyncCalls, runningAsyncCalls, runningSyncCalls), 设置maxRequests及maxRequestsPerHost
//        Dispatcher dispatcher = new Dispatcher();
//        dispatcher.setMaxRequestsPerHost(10);
//        dispatcher.setMaxRequests(100);
//        builder.dispatcher(dispatcher);

        OkHttpUtil.client = builder.build();
    }

    /**
     * Map参数转为url参数
     * 如{"a":1, "b":"test"}转换后为"?a=1&b=test"
     */
    private static String mapToUrlParam(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue());
            builder.append("&");
        }
        String str = builder.toString();
        return str.substring(0, str.length() - 1);
    }

    /**
     * 用于单元测试调用，初始化OkHttpClient
     */
    public static void initClient() {
        if (OkHttpUtil.client == null) {
            OkHttpUtil.client = new OkHttpClient.Builder().build();
        }
    }

    /**
     * Http Get 同步请求
     *
     * @param url 请求地址
     * @throws IOException IO异常
     */
    public static String doGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = OkHttpUtil.client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return null;
        }
    }

    /**
     * Http Get 同步请求
     *
     * @param url   请求地址
     * @param param 参数
     * @throws IOException IO异常
     */
    public static String doGet(String url, Map<String, Object> param) throws IOException {
        String requestUrl = url + mapToUrlParam(param);
        return doGet(requestUrl);
    }

    /**
     * Http Post 同步请求
     *
     * @param url  请求地址
     * @param json post body为json的数据
     * @throws IOException IO异常
     */
    public static String doPost(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = OkHttpUtil.client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return null;
        }
    }

    /**
     * Http Post 同步请求
     *
     * @param url  请求地址
     * @param req  body数据
     * @param type body数据类型
     * @throws IOException IO异常
     */
    public static String doPost(String url, String req, MediaType type) throws IOException {
        RequestBody body = RequestBody.create(type, req);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = OkHttpUtil.client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return null;
        }
    }

    /**
     * Http Get 异步请求
     *
     * @param url      请求地址
     * @param callback 回调
     */
    public static void doGetAsync(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpUtil.client.newCall(request).enqueue(callback);
    }

    /**
     * Http Post 异步请求
     *
     * @param url      请求地址
     * @param json     json类型的body数据
     * @param callback 回调
     */
    public static void doPostAsyn(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        OkHttpUtil.client.newCall(request).enqueue(callback);
    }

    /**
     * Http Post 异步请求
     *
     * @param url      请求地址
     * @param req      body数据
     * @param type     body数据类型
     * @param callback 回调
     */
    public static void doPostAsyn(String url, String req, MediaType type, Callback callback) {
        RequestBody body = RequestBody.create(type, req);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        OkHttpUtil.client.newCall(request).enqueue(callback);
    }

}
