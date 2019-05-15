package com.monkeybean.security.controller;

import com.monkeybean.security.core.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MonkeyBean on 2019/4/18.
 */
@RestController
@RequestMapping("login")
public class LoginController {
    /**
     * 访问需认证接口时, 重定向到此接口
     */
    @GetMapping
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Integer> login() {
        return new Result<>();
    }
}
