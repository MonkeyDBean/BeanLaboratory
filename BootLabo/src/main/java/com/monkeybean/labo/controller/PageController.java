package com.monkeybean.labo.controller;

import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.predefine.ReturnCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回码文档接口
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@RestController
public class PageController {

    /**
     * 获取状态码信息
     */
    @GetMapping("/code/status")
    public Result<Object> code() {
        List list = new ArrayList();
        for (ReturnCode code : ReturnCode.values()) {
            Object[] arr = new Object[4];
            arr[0] = code.getCode();
            arr[1] = code.name();
            arr[2] = code.getMsg();
            arr[3] = code.getDescription();
            list.add(arr);
        }
        return new Result<>(ReturnCode.SUCCESS, list);
    }

}
