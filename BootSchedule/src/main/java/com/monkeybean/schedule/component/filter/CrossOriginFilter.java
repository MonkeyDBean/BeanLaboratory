package com.monkeybean.schedule.component.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by MonkeyBean on 2019/3/26.
 */
@WebFilter(filterName = "CrossOriginFilter", urlPatterns = "/*")
public class CrossOriginFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(CrossOriginFilter.class);

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        logger.info("CrossOriginFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");

        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {
        logger.info("CrossOriginFilter destroy");
    }

}
