package com.monkeybean.labo.component.filter;

import com.monkeybean.labo.predefine.CacheData;
import com.monkeybean.labo.predefine.ConstValue;
import com.monkeybean.labo.util.LegalUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class TokenKeyFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(TokenKeyFilter.class);

    private boolean isFilter;

    private String sniffKey;

    private int dailyRequestMaxNum;

    private int dailyRequestWarnNum;

    public TokenKeyFilter(boolean isFilter, String sniffKey, int dailyRequestMaxNum) {
        this.isFilter = isFilter;
        this.sniffKey = sniffKey;
        this.dailyRequestMaxNum = dailyRequestMaxNum;
        this.dailyRequestWarnNum = (int) (dailyRequestMaxNum * 0.8);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        logger.info("TokenKeyFilter init, timestamp: {}", System.currentTimeMillis());
    }

    @Override
    public void destroy() {
        logger.info("TokenKeyFilter destroy, timestamp: {}", System.currentTimeMillis());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String servletPath = httpServletRequest.getServletPath().toLowerCase();

        //嗅探接口和健康检查接口，外部脚本定时请求判断服务状态，日志级别单独设为debug
        if (servletPath.contains("sniff/status") || servletPath.contains("health")) {
            logger.debug("request param url: {}", httpServletRequest.getRequestURL());
            logger.debug("request param queryString: {}", httpServletRequest.getQueryString());
        } else {
            logger.info("request param url: {}", httpServletRequest.getRequestURL());
            logger.info("request param, as follows");
            Enumeration<String> requestParamNames = httpServletRequest.getParameterNames();
            while (requestParamNames.hasMoreElements()) {
                String eachParam = requestParamNames.nextElement();
                logger.info("{} = {}", eachParam, httpServletRequest.getParameter(eachParam));
            }
        }

        //是否开启安全校验(若跨域，不拦截OPTIONS方法)
        // if (!isFilter || "OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
        if (!isFilter) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //判断请求是否已达最大，请求次数写入缓存
        int dailyCount = CacheData.REQUEST_COUNT_MAP.getOrDefault(servletPath, 0);
        if (dailyCount < dailyRequestMaxNum) {
            if (dailyCount > dailyRequestWarnNum) {
                logger.warn("today request: {}  is beyond the guard line, count is: {}, dailyRequestWarnNum: {}", servletPath, dailyCount, dailyRequestWarnNum);
            }
        } else {
            logger.error("today request: {} is up to peak, count is: {}, dailyRequestMaxNum: {}", servletPath, dailyCount, dailyRequestMaxNum);
            httpServletResponse.setStatus(401);
            return;
        }
        CacheData.REQUEST_COUNT_MAP.put(servletPath, CacheData.REQUEST_COUNT_MAP.getOrDefault(servletPath, 0) + 1);

        //无需安全校验的接口
        if (servletPath.contains("swagger") || servletPath.contains("api-docs")
                || servletPath.contains(".txt") || servletPath.contains(".html") || servletPath.contains("favicon.ico")
                || servletPath.contains("code/status") || servletPath.contains("kapcha") || servletPath.contains("sys/time/get")
                || servletPath.contains("view/display")) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        } else if (servletPath.contains("sniff/status") || servletPath.contains("util/use")
                || servletPath.contains("testtest") || servletPath.contains("monitor/actuator")) { //校验嗅探等特殊接口token
            if (httpServletRequest.getQueryString() != null && httpServletRequest.getQueryString().toLowerCase().contains(sniffKey)) {
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                logger.warn("sniff key is wrong");
                httpServletResponse.setStatus(401);
            }
            return;
        } else { //安全校验

            //是否校验签名及session(文件上传接口不校验签名，未登录的接口不校验session)
            boolean checkSign = true;
            boolean checkSession = true;
            if (servletPath.contains("message/apply") || servletPath.contains("user/login") || servletPath.contains("user/register")
                    || servletPath.contains("password/reset")) {
                checkSession = false;
            } else if (servletPath.contains("avatar/upload") || servletPath.contains("image/multi/upload")) {
                checkSign = false;
            }
            if (checkSign && !checkSignLegal(httpServletRequest, httpServletResponse)) {
                return;
            }
            if (checkSession) {
                logger.debug("filter sessionId: {}", httpServletRequest.getSession().getId());
                if (httpServletRequest.getSession().getAttribute(ConstValue.ACCOUNT_IDENTITY) == null) {
                    logger.debug("session filter 403, param accountId is null");
                    httpServletResponse.setStatus(403);
                    return;
                }
            }
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 签名合法性校验
     *
     * @param request  请求
     * @param response 响应
     * @return 校验通过返回true, 失败返回false
     */
    private boolean checkSignLegal(HttpServletRequest request, HttpServletResponse response) {

        //访问时间校验
        String timeParamStr = request.getParameter("stime");
        String sign = request.getParameter("sign");
        if (timeParamStr == null || sign == null || !LegalUtil.isLegalTimestamp(timeParamStr)) {
            logger.warn("safe param lack or illegal");
            response.setStatus(401);
            return false;
        }
        long internalTime = Math.abs(System.currentTimeMillis() - Long.parseLong(timeParamStr));
        if (internalTime > ConstValue.TIME_OUT) {
            logger.warn("stime is illegal, stime is: {}, internalTime is: {}", timeParamStr, internalTime);
            response.setStatus(401);
            return false;
        }

        //签名比对
        StringBuilder signDataBuilder = new StringBuilder();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String eachParam = paramNames.nextElement();
            if (!eachParam.equals("sign")) {
                signDataBuilder.append(request.getParameter(eachParam));
            }
        }
        String signOriginData = signDataBuilder.toString();
        String paramSign = DigestUtils.md5Hex(signOriginData);
        logger.debug("signOriginData is :{}, right sign is :{}", signOriginData, paramSign);
        if (!paramSign.equalsIgnoreCase(sign)) {
            logger.warn("sign is wrong, originSign is :{}, right sign is :{}", sign, paramSign);
            response.setStatus(401);
            return false;
        }
        return true;
    }

}
