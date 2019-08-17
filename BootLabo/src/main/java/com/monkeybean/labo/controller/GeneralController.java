package com.monkeybean.labo.controller;

import com.monkeybean.labo.component.config.OtherConfig;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.predefine.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 若严格RestFul标准(Representational State Transfer)，请求路径为resource,名词。
 * 通过请求方法区分不同操作，增删改查分别对应Http Method：POST、DELETE、PUT(or PATCH)、GET
 * 参考文档：https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design
 * 实际使用可灵活变换
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@Api(value = "通用api")
@RequestMapping(path = "general")
@RestController
public class GeneralController {
    private static Logger logger = LoggerFactory.getLogger(GeneralController.class);
    private final OtherConfig otherConfig;
    @Value("${spring.profiles.active}")
    private String activeEnv;

    @Autowired
    public GeneralController(OtherConfig otherConfig) {
        this.otherConfig = otherConfig;
    }

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

    /**
     * 请求格式如 http://ip:port/monkey/general/config/reload?123
     */
    @ApiOperation(value = "重新加载OtherConfig配置, 实现动态更新")
    @GetMapping(path = "config/reload")
    public Result<String> reloadConfig() throws Exception {
        Properties properties = new Properties();
        String filePath = "application-" + activeEnv + ".properties";
        Resource resource = new ClassPathResource(filePath);
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(resource.getFile()))) {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            logger.error("reloadConfig, read file, IOException: [{}]", e);
            return new Result<>(ReturnCode.SERVER_EXCEPTION);
        }
        otherConfig.reloadConfig(properties);
        return new Result<>(ReturnCode.SUCCESS);
    }

}
