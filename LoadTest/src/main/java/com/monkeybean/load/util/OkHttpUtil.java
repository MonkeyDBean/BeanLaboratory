package com.monkeybean.load.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.concurrent.TimeUnit;

/**
 * OkHttp工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class OkHttpUtil {
    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 30;
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)   //设置连接超时时间
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)   //设置写的超时时间
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)    //设置读取超时时间
            .build();

    private OkHttpUtil() {
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
        client.newCall(request).enqueue(callback);
    }

}
