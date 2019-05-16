package com.monkeybean.security.component.auth.handler;

import com.alibaba.fastjson.JSONObject;
import com.monkeybean.security.component.constant.StatusCode;
import com.monkeybean.security.component.reqres.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证失败处理handler
 * <p>
 * Created by MonkeyBean on 2019/4/20.
 */
@Component
public class SecurityAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException, ServletException {
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter out = httpServletResponse.getWriter();
        out.write(JSONObject.toJSONString(new Result<>(StatusCode.USER_NAME_PASSWORD_ERROR)));
        out.flush();
        out.close();
    }
}
