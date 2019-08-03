package com.monkeybean.lb.request;

import com.monkeybean.lb.load.LoadBalancer;

import java.util.Random;

/**
 * Created by MonkeyBean on 2019/8/2.
 */
public class RequestFactory {

    private static volatile RequestFactory factory;

    private Random random = new Random(System.currentTimeMillis());

    private String[] keyPool = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "j", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "J", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private RequestFactory() {
    }

    /**
     * 获取生成器实例
     */
    public static RequestFactory getInstance() {
        if (factory == null) {
            synchronized (RequestFactory.class) {
                if (factory == null) {
                    factory = new RequestFactory();
                }
            }
        }
        return factory;
    }

    /**
     * 批量生成请求
     *
     * @param num 生成数量
     */
    public void batchGenerateRequest(int num) {
        for (int i = 0; i < num; i++) {
            randomGenerateRequest();
        }
    }

    /**
     * 随机生成请求
     */
    public void randomGenerateRequest() {

        //随机ip
        String ip = "";
        for (int i = 0; i < 4; i++) {
            ip += random.nextInt(256) + ".";
        }
        ip = ip.substring(0, ip.lastIndexOf("."));

        //随机key
        String key = "";
        for (int i = 0; i < random.nextInt(10) + 4; i++) {
            key += keyPool[random.nextInt(keyPool.length)];
        }
        String origin = "http://" + ip + ":80?key=" + key;
        processRequest(origin, ip, key);
    }

    /**
     * 生成单个请求
     *
     * @param origin 原始请求
     */
    public void generateRequest(String origin) {
        String ip = origin.substring(origin.indexOf("//") + 2, origin.lastIndexOf(":"));
        String key = origin.substring(origin.indexOf("=") + 1);
        processRequest(origin, ip, key);
    }

    /**
     * 单个请求处理
     *
     * @param origin 原始请求
     */
    private void processRequest(String origin, String ip, String key) {
        RequestInfo request = new RequestInfo(origin, ip, key);
        if (LoadBalancer.getInstance().getInstanceMap().isEmpty()) {
            System.out.println("processRequest failed, instanceMap is null, origin request is: " + origin);
            return;
        }
        LoadBalancer.getInstance().process(request);
    }

}
