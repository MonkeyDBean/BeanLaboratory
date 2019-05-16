package com.monkeybean.security.component.auth.handler;

import com.alibaba.fastjson.JSONObject;
import com.monkeybean.security.component.auth.UserInfoHolder;
import com.monkeybean.security.component.constant.StatusCode;
import com.monkeybean.security.component.reqres.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证成功处理handler
 * <p>
 * Created by MonkeyBean on 2019/4/20.
 */
@Component
public class SecurityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserInfoHolder userInfoHolder;

    @Autowired
    public SecurityAuthenticationSuccessHandler(UserInfoHolder userInfoHolder) {
        this.userInfoHolder = userInfoHolder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
            throws IOException, ServletException {
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.write(JSONObject.toJSONString(new Result<>(StatusCode.SUCCESS, userInfoHolder.getUser().getUsername())));
        out.flush();
        out.close();
    }
}
