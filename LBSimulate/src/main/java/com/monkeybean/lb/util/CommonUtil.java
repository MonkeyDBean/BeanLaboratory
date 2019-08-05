package com.monkeybean.lb.util;

import java.util.Random;

/**
 * Created by MonkeyBean on 2019/8/2.
 */
public final class CommonUtil {
    private CommonUtil() {
    }

    /**
     * 生成随机ip地址
     */
    public static String randomIp() {
        String ip = "";
        final Random random = new Random();
        for (int i = 0; i < 4; i++) {
            ip += random.nextInt(256) + ".";
        }
        ip = ip.substring(0, ip.lastIndexOf("."));
        return ip;
    }
}
