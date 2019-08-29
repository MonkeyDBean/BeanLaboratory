package com.monkeybean.labo.component.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问控制, 允许跨域
 * 若使用session策略作为用户身份区分，建议前后端为同一域名下，通过nginx路径匹配，反向代理到后端，避免跨域问题的出现
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class CrossOriginFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(CrossOriginFilter.class);

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        logger.info("CrossOriginFilter init, timestamp: {}", System.currentTimeMillis());
    }

    @Override
    public void destroy() {
        logger.info("CrossOriginFilter destroy, timestamp: {}", System.currentTimeMillis());
    }

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
//        chain.doFilter(httpServletRequest, httpServletResponse);
//    }

    /**
     * 允许跨域
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //若前后端为不同域名，会有Session跨域问题，同一用户的SessionId不一致。服务端如下设置，允许跨域；同时前端设置withCredentials为true，解决浏览器的同源限制
        //可参照：https://blog.csdn.net/weixin_40461281/article/details/81196932
        //接收任意域名请求
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));

        //允许任意请求方法
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");

        //允许浏览器发送cookie
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");

        //列出浏览器CORS请求会额外发送的头字段，若用于签名校验的stime或sign放在头部，则需声明
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", "stime,sign");
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

}
