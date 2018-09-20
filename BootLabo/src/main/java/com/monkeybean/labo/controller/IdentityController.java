package com.monkeybean.labo.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.component.reqres.req.*;
import com.monkeybean.labo.component.reqres.res.AccountInfoRes;
import com.monkeybean.labo.predefine.ReturnCode;
import com.monkeybean.labo.service.IdentityService;
import com.monkeybean.labo.util.RandomStringUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by MonkeyBean on 2018/5/26.
 */
@RequestMapping(path = "identity")
@RestController
public class IdentityController {

    private static Logger logger = LoggerFactory.getLogger(IdentityController.class);

    private final DefaultKaptcha gKapcha;

    private final IdentityService identityService;

    @Autowired
    public IdentityController(DefaultKaptcha gKapcha, IdentityService identityService) {
        this.gKapcha = gKapcha;
        this.identityService = identityService;
    }

    @ApiOperation(value = "获取用户图形验证码")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "png格式的图片")})
    @RequestMapping(path = "/kapcha", method = RequestMethod.GET)
    public void getKapcha(HttpSession session, HttpServletResponse response) {
        RandomStringUtil randomStr = new RandomStringUtil(4);//生成四位
        String code = randomStr.nextString(false);//字母数字混合
        BufferedImage image = gKapcha.createImage(code);
        session.setAttribute(gKapcha.getConfig().getSessionKey(), code);
        try {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            ServletOutputStream sOutputStream = response.getOutputStream();
            ImageIO.write(image, "png", sOutputStream);
            sOutputStream.close();
        } catch (IOException e) {
            logger.error("getKapcha IOException: {}", e);
        }
    }

    @ApiOperation(value = "申请发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "图形验证码", required = true, dataType = "string", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "code: 无特殊处理的返回码")})
    @RequestMapping(path = "message/apply", method = RequestMethod.GET)
    public Result<String> getValidCode(@Valid MessageApplyReq reqModel, HttpServletRequest request) {
        String codeKey = gKapcha.getConfig().getSessionKey();
        String verifyCode = request.getSession().getAttribute(codeKey) == null ? ""
                : request.getSession().getAttribute(codeKey).toString();
        if (verifyCode.equals(reqModel.getCode())) {
            request.getSession().removeAttribute(codeKey);
            return identityService.getValidCode(reqModel.getPhone());
        }
        return new Result<>(ReturnCode.IMAGE_CODE_INCORRECT);
    }

    @ApiOperation(value = "账号登录,允许多终端/浏览器登录(当前账户若已在手机端登录，登录pc端后，与手机端同时在线)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "账号(手机号或邮箱)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码, 明文密码的n+1次md5摘要", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "response", value = "reCaptcha response", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "stay", value = "是否保持登录", required = true, dataType = "boolean", paramType = "query", defaultValue = "false"),
            @ApiImplicitParam(name = "stime", value = "访问时间,unix时间戳，毫秒级", required = true, dataType = "long", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "code: 无特殊处理的返回码")})
    @RequestMapping(path = "user/login", method = RequestMethod.POST)
    public Result<Integer> userLogin(@Valid @ModelAttribute UserLoginReq reqModel, HttpServletRequest request) {
        return identityService.userLogin(reqModel.getUser(), reqModel.getPwd(), reqModel.getResponse(), reqModel.getStayBoolean(), reqModel.getStime(), request);
    }

    @ApiOperation(value = "注册账户", notes = "注册成功后，自动登录，跳转到首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "短信验证码", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码, 单次md5", required = true, dataType = "string", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "code: 无特殊处理的返回码")})
    @RequestMapping(path = "user/register", method = RequestMethod.POST)
    public Result<Integer> userRegister(@Valid @ModelAttribute RegisterReq reqModel, HttpServletRequest request) {
        return identityService.userRegister(reqModel.getPhone(), reqModel.getCode(), reqModel.getName(), reqModel.getPwd(), request);
    }

    @ApiOperation(value = "浏览器中，退出登录")
    @RequestMapping(path = "logout", method = RequestMethod.GET)
    public Result<Integer> logout(HttpServletRequest request) {
        if (request.getSession().getAttribute("accountId") != null) {
            request.getSession().invalidate();
        }
        return new Result<>(ReturnCode.SUCCESS);
    }

    @ApiOperation(value = "获取用户信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "code: 无特殊处理的返回码")})
    @RequestMapping(path = "info/account", method = RequestMethod.GET)
    public Result<AccountInfoRes> getAccountInfo(HttpSession session) {
        return identityService.getAccountInfo(Integer.parseInt(session.getAttribute("accountId").toString()));
    }

    @ApiOperation(value = "上传头像")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @PostMapping(value = "avatar/upload")
    public Result<String> avatarUpload(@RequestParam(value = "cover") MultipartFile file, HttpSession session) {
        return identityService.avatarUpload(Integer.parseInt(session.getAttribute("accountId").toString()), file);
    }

    @ApiOperation(value = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldpwd", value = "旧密码, n+1次md5", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "newpwd", value = "新密码，单次md5", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "stime", value = "访问时间", required = true, dataType = "long", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @RequestMapping(path = "password/update", method = RequestMethod.POST)
    public Result<Integer> updatePassword(@Valid @ModelAttribute PwdUpdateReq reqModel, HttpSession session) {
        return identityService.updatePassword(Integer.parseInt(session.getAttribute("accountId").toString()), reqModel.getOldpwd(), reqModel.getNewpwd(), reqModel.getStime());
    }

    @ApiOperation(value = "忘记密码，设置新密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "短信验证码", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "新密码(明文密码单次Md5摘要)", required = true, dataType = "string", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @RequestMapping(path = "password/reset", method = RequestMethod.POST)
    public Result<Integer> resetPwd(@Valid @ModelAttribute PwdResetReq reqModel) {
        return identityService.resetPassword(reqModel.getPhone(), reqModel.getCode(), reqModel.getPwd());
    }

    @ApiOperation(value = "绑定邮箱")
    @ApiImplicitParam(name = "mail", value = "邮箱", required = true, dataType = "string", paramType = "query")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @PostMapping(path = "mail/bind")
    public Result<String> bindMail(@Valid @ModelAttribute BindMailReq reqModel, HttpSession session) {
        return identityService.bindMail(Integer.parseInt(session.getAttribute("accountId").toString()), reqModel.getMail());
    }

}
