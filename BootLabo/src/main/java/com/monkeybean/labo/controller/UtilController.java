package com.monkeybean.labo.controller;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.monkeybean.labo.component.config.MessageConfig;
import com.monkeybean.labo.component.config.OtherConfig;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.predefine.ConstValue;
import com.monkeybean.labo.predefine.ReturnCode;
import com.monkeybean.labo.util.AliYunUtil;
import com.monkeybean.labo.util.Coder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Created by MonkeyBean on 2018/5/26.
 */
@Api(value = "工具类相关api")
@RequestMapping(path = "util/use")
@RestController
public class UtilController {

    private static Logger logger = LoggerFactory.getLogger(UtilController.class);

    private final MessageConfig messageConfig;

    private final OtherConfig otherConfig;

    @Autowired
    public UtilController(MessageConfig messageConfig, OtherConfig otherConfig) {
        this.messageConfig = messageConfig;
        this.otherConfig = otherConfig;
    }

    /**
     * 短信明细查询
     */
    @ApiOperation(value = "请求阿里云短信查询服务：短信明细查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "bizId", value = "发送流水号", required = true, dataType = "string", paramType = "query")
    })
    @RequestMapping(path = "message/query", method = RequestMethod.GET)
    public Result<HashMap<String, Object>> queryMessage(@RequestParam String phone, @RequestParam String bizId) {
        try {
            QuerySendDetailsResponse querySendDetailsResponse = AliYunUtil.querySendDetails(messageConfig.getAccessKeyId(), messageConfig.getAccessKeySecret(), phone, bizId);
            return new Result<>(ReturnCode.SUCCESS, AliYunUtil.printSendDetails(querySendDetailsResponse));
        } catch (ClientException e) {
            logger.error("queryMessage ClientException: {}", e);
            return new Result<>(ReturnCode.SERVER_EXCEPTION);
        }
    }

    /**
     * 明文密码生成登录密码和数据库密码
     */
    @ApiOperation(value = "密码生成")
    @ApiImplicitParam(name = "password", value = "明文密码", required = true, dataType = "string", paramType = "query")
    @RequestMapping(path = "generatePwd", method = RequestMethod.GET)
    public Result<LinkedHashMap<String, Object>> generatePassword(@RequestParam(value = "password") String password) {
        DateTime nowDateTime = new DateTime();
        logger.info("interface \"generatePassword\" is invoked, time: {}, password: {}", nowDateTime.toString("yyyy-MM-dd HH:mm:ss"), password);

        //检验密码合法性
        if (!Pattern.matches(ConstValue.LEGAL_PASSWORD, password)) {
            return new Result<>(ReturnCode.PWD_SIMPLE);
        }
        String sTimeStr = String.valueOf(nowDateTime.getMillis());
        int loop = Integer.parseInt(sTimeStr.substring(sTimeStr.length() - 1));
        String singleMd5Pwd = DigestUtils.md5Hex(password);
        String dbPwd = Coder.encryptPassWithSlat(singleMd5Pwd, otherConfig.getSqlSalt());
        String requestPwd = singleMd5Pwd;
        for (int i = 0; i < loop; i++) {
            requestPwd = DigestUtils.md5Hex(requestPwd);
        }
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("originPwd", password);
        data.put("singleMd5Pwd", singleMd5Pwd);
        data.put("requestSTime", sTimeStr);
        data.put("requestPwd", requestPwd);
        data.put("dbPwd", dbPwd);
        return new Result<>(ReturnCode.SUCCESS, data);
    }

}
