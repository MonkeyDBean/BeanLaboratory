package com.monkeybean.security.controller;

import com.monkeybean.security.component.constant.StatusCode;
import com.monkeybean.security.component.reqres.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MonkeyBean on 2019/4/18.
 */
@RestController
public class PageController {

    /**
     * 错误码列表
     */
    @GetMapping("/code")
    public Result<Object> code() {
        List<Object[]> list = new ArrayList<>();
        for (StatusCode code : StatusCode.values()) {
            Object[] arr = new Object[4];
            arr[0] = code.getCode();
            arr[1] = code.name();
            arr[2] = code.getMsg();
            arr[3] = code.getDescription();
            list.add(arr);
        }
        return new Result<>(StatusCode.SUCCESS, list);
    }
}
