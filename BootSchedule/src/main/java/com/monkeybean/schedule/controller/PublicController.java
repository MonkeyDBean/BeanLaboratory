package com.kodgames.wechatmini.controller;

import com.kodgames.wechatmini.component.reqres.Result;
import com.kodgames.wechatmini.constant.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangbin on 2019/3/25.
 */
@Api(value = "其他公共服务")
@RequestMapping(path = "personal/util")
@RestController
public class PublicController {
    private static Logger logger = LoggerFactory.getLogger(PublicController.class);

    @Autowired
    public PublicController() {
    }

    @ApiOperation(value = "嗅探线上服务器运行是否正常")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "code：0")})
    @GetMapping(path = "sniff/status")
    public Result<Integer> sniffStatus() {
        logger.debug("sniff test ok, now time: {}", System.currentTimeMillis());
        return new Result<>(StatusCode.SUCCESS);
    }

}
