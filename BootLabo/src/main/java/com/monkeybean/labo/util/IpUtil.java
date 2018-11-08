package com.monkeybean.labo.util;

import org.lionsoul.ip2region.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Ip工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class IpUtil {

    private static Logger logger = LoggerFactory.getLogger(WeatherUtil.class);

    private IpUtil() {
    }

    /**
     * 获取请求ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ips = request.getHeader("x-forwarded-for");
        final String unknownStr = "unknown";
        if (ips == null || ips.length() == 0 || ips.equalsIgnoreCase(unknownStr))
            ips = request.getHeader("Proxy-Client-IP");
        if (ips == null || ips.length() == 0 || ips.equalsIgnoreCase(unknownStr))
            ips = request.getHeader("WL-Proxy-Client-IP");
        if (ips == null || ips.length() == 0 || unknownStr.equalsIgnoreCase(ips))
            ips = request.getHeader("HTTP_CLIENT_IP");
        if (ips == null || ips.length() == 0 || "unknown".equalsIgnoreCase(ips))
            ips = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ips == null || ips.length() == 0 || ips.equalsIgnoreCase(unknownStr))
            ips = request.getRemoteAddr();
        if (ips == null || ips.length() == 0 || ips.equalsIgnoreCase(unknownStr))
            ips = "0.0.0.0";
        return ips.split(",")[0];
    }

    /**
     * 通过ip获取区域信息
     * 注：前端也可直接调用阿里免费接口 http://ip.taobao.com/service/getIpInfo.php?ip=$ip
     *
     * @param ip ip地址
     * @return 成功返回信息，失败返回null
     */
    public static String getCityInfo(String ip) {
        if (!Util.isIpAddress(ip)) {
            return null;
        }

        //默认将本地地址设置为北京
        if ("127.0.0.1".equals(ip) || "0.0.0.0".equals(ip)) {
            return "beijing";
        }
        try {
            DbConfig config = new DbConfig();
            String dbFilePath = IpUtil.class.getResource("/ip2region.db").getPath();
            DbSearcher searcher = new DbSearcher(config, dbFilePath);

            //B树搜索
            // DataBlock block = searcher.btreeSearch(ip);

            //二分搜索
            DataBlock block = searcher.binarySearch(ip);
            return block.getRegion();
        } catch (DbMakerConfigException | IOException e) {
            logger.error("ip2region, error: {}", e);
            return null;
        }
    }
}




