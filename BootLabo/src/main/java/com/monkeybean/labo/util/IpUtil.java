package com.monkeybean.labo.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Ip工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class IpUtil {

    /**
     * 获取请求ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ips = request.getHeader("x-forwarded-for");
        if (ips == null || ips.length() == 0 || ips.equalsIgnoreCase("unknown"))
            ips = request.getHeader("Proxy-Client-IP");
        if (ips == null || ips.length() == 0 || ips.equalsIgnoreCase("unknown"))
            ips = request.getHeader("WL-Proxy-Client-IP");
        if (ips == null || ips.length() == 0 || "unknown".equalsIgnoreCase(ips))
            ips = request.getHeader("HTTP_CLIENT_IP");
        if (ips == null || ips.length() == 0 || "unknown".equalsIgnoreCase(ips))
            ips = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ips == null || ips.length() == 0 || ips.equalsIgnoreCase("unknown"))
            ips = request.getRemoteAddr();
        if (ips == null || ips.length() == 0 || ips.equalsIgnoreCase("unknown"))
            ips = "0.0.0.0";
        return ips.split(",")[0];
    }
}
