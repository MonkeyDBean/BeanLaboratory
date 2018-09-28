package com.monkeybean.flux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by MonkeyBean on 2018/9/26.
 */
@RequestMapping(path = "error")
@Controller
public class ViewController {

    @GetMapping("/401")
    public String unAuthorized() {
        return "401";
    }
}
