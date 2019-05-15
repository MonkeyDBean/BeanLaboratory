package com.monkeybean.security.component.auth.handler;

import com.alibaba.fastjson.JSONObject;
import com.monkeybean.security.component.constant.StatusCode;
import com.monkeybean.security.core.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证被拒绝处理handler
 * <p>
 * Created by MonkeyBean on 2019/4/20.
 */
@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e)
            throws IOException, ServletException {
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter out = httpServletResponse.getWriter();
        out.write(JSONObject.toJSONString(new Result<>(StatusCode.FORBIDDEN)));
        out.flush();
        out.close();
    }
}
