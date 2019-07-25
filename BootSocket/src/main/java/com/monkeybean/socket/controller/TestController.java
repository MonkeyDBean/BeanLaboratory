package com.monkeybean.socket.controller;

import com.monkeybean.socket.core.Result;
import com.monkeybean.socket.core.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/exception")
    public Result testExceptionHandler() {
        int a = 1;
        int b = 0;
        int c = a / b;
        return ResultGenerator.genSuccessResult(c);
    }
}
