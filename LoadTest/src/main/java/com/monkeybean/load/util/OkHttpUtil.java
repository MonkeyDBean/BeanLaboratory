package com.monkeybean.load.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class OkHttpUtil {
    private final static int CONNECT_TIMEOUT = 10;
    private final static int WRITE_TIMEOUT = 10;
    private final static int READ_TIMEOUT = 30;
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)   //设置连接超时时间
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)   //设置写的超时时间
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)    //设置读取超时时间
            .build();

    /**
     * Http Get 异步请求
     *
     * @param url      请求地址
     * @param callback 回调
     * @throws IOException IO异常
     */
    public static void doGetAsync(String url, Callback callback) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
