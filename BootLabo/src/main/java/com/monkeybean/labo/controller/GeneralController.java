package com.monkeybean.labo.controller;

import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.predefine.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
@Api(value = "通用api")
@RequestMapping(path = "general")
@RestController
public class GeneralController {

    @ApiOperation(value = "嗅探服务器运行是否正常")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "code：0")})
    @GetMapping(path = "sniff/status")
    public Result<Map<String, Object>> sniffStatus() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("SystemTime", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        //系统总内存、最大可用内存、当前剩余可用内存
        double mb2bt = 1024 * 1024.0;
        Runtime rt = Runtime.getRuntime();
        data.put("totalMemorySize", String.format("%.2f", rt.totalMemory() / mb2bt) + "MB");
        data.put("maxMemorySiz", String.format("%.2f", rt.maxMemory() / mb2bt) + "MB");
        data.put("freeMemorySize", String.format("%.2f", rt.freeMemory() / mb2bt) + "MB");

        //操作系统及运行环境信息
        Properties props = System.getProperties();
        data.put("javaVersion", props.getProperty("java.version"));
        data.put("javaVendor", props.getProperty("java.vendor"));
        data.put("javaHome", props.getProperty("java.home"));
        data.put("javaVmSpecificationVersion", props.getProperty("java.vm.specification.version"));
        data.put("javaVmVersion", props.getProperty("java.vm.version"));
        data.put("javaVmName", props.getProperty("java.vm.name"));
        data.put("javaIoTmpDir", props.getProperty("java.io.tmpdir"));
        data.put("osName", props.getProperty("os.name"));
        data.put("osArch", props.getProperty("os.arch"));
        data.put("osVersion", props.getProperty("os.version"));
        data.put("userHome", props.getProperty("user.home"));
        data.put("userDir", props.getProperty("user.dir"));
        return new Result<>(ReturnCode.SUCCESS, data);
    }

    @ApiOperation(value = "获取服务器当前时间,unix时间戳，毫秒")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回码")})
    @GetMapping(path = "sys/time/get")
    public Result<Long> getSystemTime() {
        return new Result<>(ReturnCode.SUCCESS, System.currentTimeMillis());
    }

}
