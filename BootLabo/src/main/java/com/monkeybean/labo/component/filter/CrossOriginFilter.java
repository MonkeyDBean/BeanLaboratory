package com.monkeybean.labo.component.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class CrossOriginFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //接收任意域名请求
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");

        //允许任意请求方法
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");

        //允许浏览器发送cookie
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");

        //列出浏览器CORS请求会额外发送的头字段，若用于签名校验的stime或sign放在头部，则需声明
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", "stime,sign");
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }

}
