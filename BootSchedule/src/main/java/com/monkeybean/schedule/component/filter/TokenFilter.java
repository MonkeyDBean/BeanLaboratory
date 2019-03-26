package com.monkeybean.schedule.component.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by MonkeyBean on 2019/3/.
 */
@WebFilter(filterName = "TokenFilter", urlPatterns = "/*")
public class TokenFilter implements Filter {
    private static final String KEY_SNIFF = "wydsxlyrgjmwtsysqq";
    private static Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    /**
     * 是否开启拦截器
     */
    private boolean isFilter = false;

    public TokenFilter() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        String openStr = System.getenv("filterOpen");
        if (openStr != null && Boolean.parseBoolean(openStr)) {
            isFilter = true;
        }
        logger.info("TokenFilter init");
    }

    @Override
    public void destroy() {
        logger.info("TokenFilter destroy");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String servletPath = httpServletRequest.getServletPath().toLowerCase();

        //状态嗅探接口，脚本定时请求，日志单设为debug级别
        if (servletPath.contains("sniff/status")) {
            logger.debug("request param url: {}", httpServletRequest.getRequestURL());
            logger.debug("request param queryString: {}", httpServletRequest.getQueryString());
        } else {
            logger.info("request param url: {}", httpServletRequest.getRequestURL());
            logger.info("request param queryString: {}", httpServletRequest.getQueryString());
        }

        //是否开启安全校验
        if (!isFilter) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //安全校验
        if (servletPath.contains("sniff/status")) {
            if (httpServletRequest.getQueryString() == null || !httpServletRequest.getQueryString().toLowerCase().contains(KEY_SNIFF)) {
                logger.warn("sniff key is wrong");
                httpServletResponse.setStatus(401);
                return;
            }
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

}
