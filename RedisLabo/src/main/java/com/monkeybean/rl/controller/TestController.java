package com.monkeybean.rl.controller;

import com.monkeybean.rl.component.config.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MonkeyBean on 2019/7/28.
 */
@Api(value = "测试接口")
@RequestMapping(path = "test")
@RestController
public class TestController {

    @ApiOperation(value = "Redis存数据")
    @PostMapping("redis/save")
    public String saveDataToRedis(@RequestParam String key, @RequestParam String value, @RequestParam long expire) {
        RedisUtil.setValue(key, value, expire);
        return "success";
    }

    @ApiOperation(value = "Redis读数据")
    @PostMapping("redis/get")
    public Object getDataFromRedis(@RequestParam String key) {
        return RedisUtil.getValue(key);
    }
}
