package com.monkeybean.security.controller;

import com.monkeybean.security.component.constant.StatusCode;
import com.monkeybean.security.component.reqres.Result;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangbin on 2019/5/15.
 */
@RestController
@RequestMapping("/testtest")
public class TestController {

    @GetMapping("/encode")
    public Result<String> login(@RequestParam String origin) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return new Result<>(StatusCode.SUCCESS, encoder.encode(origin));
    }

}
