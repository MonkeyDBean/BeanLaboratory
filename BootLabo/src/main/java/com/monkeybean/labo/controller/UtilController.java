package com.monkeybean.labo.controller;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.monkeybean.labo.component.config.MessageConfig;
import com.monkeybean.labo.component.config.OtherConfig;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.component.reqres.req.QRCodeGenerateReq;
import com.monkeybean.labo.predefine.ConstValue;
import com.monkeybean.labo.predefine.ReturnCode;
import com.monkeybean.labo.util.AliYunUtil;
import com.monkeybean.labo.util.Coder;
import com.monkeybean.labo.util.ZXingUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 工具类接口, 协助调试
 * <p>
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
    @GetMapping(path = "message/query")
    public Result<Map<String, Object>> queryMessage(@RequestParam String phone, @RequestParam String bizId) {
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
    @GetMapping(path = "generatePwd")
    public Result<Map<String, Object>> generatePassword(@RequestParam(value = "password") String password) {
        DateTime nowDateTime = new DateTime();
        String nowDataTimeStr = nowDateTime.toString("yyyy-MM-dd HH:mm:ss");
        logger.info("interface \"generatePassword\" is invoked, time: {}, password: {}", nowDataTimeStr, password);

        //检验密码合法性
        if (!Pattern.matches(ConstValue.LEGAL_PASSWORD, password)) {
            return new Result<>(ReturnCode.PWD_SIMPLE);
        }
        final String paramTimeStr = String.valueOf(nowDateTime.getMillis());
        final int loop = Integer.parseInt(paramTimeStr.substring(paramTimeStr.length() - 1));
        final String singleMd5Pwd = DigestUtils.md5Hex(password);
        final String dbPwd = Coder.encryptPassWithSlat(singleMd5Pwd, otherConfig.getSqlSalt());
        String requestPwd = singleMd5Pwd;
        for (int i = 0; i < loop; i++) {
            requestPwd = DigestUtils.md5Hex(requestPwd);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("originPwd", password);
        data.put("singleMd5Pwd", singleMd5Pwd);
        data.put("requestPwd", requestPwd);
        data.put("dbPwd", dbPwd);
        data.put("requestSTime", paramTimeStr);
        return new Result<>(ReturnCode.SUCCESS, data);
    }

    @ApiOperation(value = "生成二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "二维码内容", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "format", value = "生成图片的格式，如png, jpeg, jpg", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "width", value = "图片宽度, 像素", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "height", value = "图片高度，像素", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "hasBlank", value = "是否有白边", defaultValue = "false", required = true, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "hasLogo", value = "是否加logo，无白边可加logo", defaultValue = "false", required = true, dataType = "boolean", paramType = "query")
    })
    @PostMapping(path = "qrcode/generate")
    public void generateQRCode(@Valid @ModelAttribute QRCodeGenerateReq reqModel, @RequestParam(value = "cover") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Boolean isFill;
            BufferedImage logo = null;
            if (reqModel.isHasBlank()) {
                isFill = false;
            } else {
                isFill = true;
                if (reqModel.isHasLogo()) {
                    logo = ImageIO.read(file.getInputStream());
                }
            }
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/" + request.getParameter("format"));
            ServletOutputStream servletOutputStream = response.getOutputStream();
            ZXingUtil.generateQRCode(reqModel.getContent(), reqModel.getFormat(), reqModel.getWidth(), reqModel.getHeight(), servletOutputStream, isFill, logo);
            servletOutputStream.close();
        } catch (IOException e) {
            logger.error("ServletOutputStream, IOException: {}", e);
        }
    }

}
