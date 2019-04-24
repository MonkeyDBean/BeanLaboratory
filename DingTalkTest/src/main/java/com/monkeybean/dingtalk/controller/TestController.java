package com.monkeybean.dingtalk.controller;

import com.dingtalk.api.response.*;
import com.monkeybean.dingtalk.component.config.CustomInfoConfig;
import com.monkeybean.dingtalk.reqres.Result;
import com.monkeybean.dingtalk.reqres.StatusCode;
import com.monkeybean.dingtalk.util.DingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 接口测试
 * <p>
 * Created by MonkeyBean on 2019/4/19.
 */
@RestController
@RequestMapping("test")
public class TestController {
    private final CustomInfoConfig customInfoConfig;

    @Autowired
    public TestController(CustomInfoConfig customInfoConfig) {
        this.customInfoConfig = customInfoConfig;
    }

    @GetMapping("ticket/get")
    public String getTicket() {
        return DingUtil.getTicket(customInfoConfig.getAppKey(), customInfoConfig.getAppSecret());
    }

    @GetMapping("usr/{id}")
    public OapiUserGetResponse getUserInfo(@PathVariable String id) {
        return DingUtil.getUserInfo(customInfoConfig.getAppKey(), customInfoConfig.getAppSecret(), id);
    }

    @GetMapping("department/usr/list")
    public OapiUserListbypageResponse getDepartmentUserInfo(@RequestParam("id") long id, @RequestParam("size") long size, @RequestParam("offset") long offset) {
        return DingUtil.getDepartmentUserInfo(customInfoConfig.getAppKey(), customInfoConfig.getAppSecret(), id, size, offset);
    }

    @GetMapping("department/list/{id}")
    public OapiDepartmentListResponse getDepartmentList(@PathVariable String id) {
        return DingUtil.getDepartmentList(customInfoConfig.getAppKey(), customInfoConfig.getAppSecret(), id, true);
    }

    @GetMapping("department/info/{id}")
    public OapiDepartmentGetResponse getDepartmentInfo(@PathVariable String id) {
        return DingUtil.getDepartmentInfo(customInfoConfig.getAppKey(), customInfoConfig.getAppSecret(), id);
    }

    @GetMapping("role/list")
    public OapiRoleListResponse getRoleList(@RequestParam("size") long size, @RequestParam("offset") long offset) {
        return DingUtil.getRoleList(customInfoConfig.getAppKey(), customInfoConfig.getAppSecret(), size, offset);
    }

    @GetMapping("role/usr/list")
    public OapiRoleSimplelistResponse getRoleUserList(@RequestParam("id") long id, @RequestParam("size") long size, @RequestParam("offset") long offset) {
        return DingUtil.getRoleUserList(customInfoConfig.getAppKey(), customInfoConfig.getAppSecret(), id, size, offset);
    }

    @PostMapping("chat/text/send")
    public OapiChatSendResponse sendToChatText(@RequestParam("id") String id, @RequestParam("content") String content) {
        return DingUtil.sendTextToChat(customInfoConfig.getAppKey(), customInfoConfig.getAppSecret(), id, content);
    }

    //@GetMapping("config/get")
    public Result<CustomInfoConfig> getConfig() {
        CustomInfoConfig config = new CustomInfoConfig();
        config.setCorpId(customInfoConfig.getCorpId());
        config.setAgentId(customInfoConfig.getAgentId());
        config.setAppKey(customInfoConfig.getAppKey());
        config.setAppSecret(customInfoConfig.getAppSecret());
        config.setSignUrl(customInfoConfig.getSignUrl());
        return new Result<>(StatusCode.SUCCESS, config);
    }

}
