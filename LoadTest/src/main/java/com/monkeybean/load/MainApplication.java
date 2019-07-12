package com.monkeybean.load;

import com.monkeybean.load.util.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.Util;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by MonkeyBean on 2018/6/13.
 */
public class MainApplication {

    private static void callRequest(String url) {
        OkHttpUtil.doGetAsync(url, new Callback() {
            public void onFailure(Call call, IOException e) {
                System.out.println("request failed, call: " + call.toString() + ", exception is: " + e.toString() + ", details: " + e);
            }

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response != null && response.isSuccessful()) {
                        String responseStr = response.body().string();
                        System.out.println("request success, responseStr is: " + responseStr);
                    }
                } catch (Exception e) {
                    Util.closeQuietly(response);
                    e.printStackTrace();
                    System.out.println("process server response error: " + e.toString());
                }
            }
        });
    }

    public static void main(String[] args) {
        ClassLoader classLoader = MainApplication.class.getClassLoader();
        Properties props = new Properties();
        try {
            props.load(classLoader.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("getResourceAsStream IOException, e: {}" + e.toString());
        }
        final String requestUrl = props.getProperty("requestUrl");
        ExecutorService pool = Executors.newCachedThreadPool();
//        ExecutorService pool = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            final int index = i;
            pool.execute(() -> {
                while (true) {
                    System.out.println("Thread: " + index + "execute");
                    callRequest(requestUrl);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.print("Thread InterruptedException, e: {}" + e.toString());
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }

}
