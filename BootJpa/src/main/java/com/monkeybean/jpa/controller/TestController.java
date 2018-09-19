package com.monkeybean.jpa.controller;

import com.monkeybean.jpa.config.CommonConfig;
import com.monkeybean.jpa.entity.User;
import com.monkeybean.jpa.res.Result;
import com.monkeybean.jpa.res.ResultGenerator;
import com.monkeybean.jpa.service.UserService;
import com.monkeybean.jpa.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
@RequestMapping(path = "test")
@RestController("com.monkeybean.jpa.controller.TestController")
public class TestController {
    private final UserService userService;

    private final CommonConfig commonConfig;

    @Autowired
    public TestController(UserService userService, CommonConfig commonConfig) {
        this.userService = userService;
        this.commonConfig = commonConfig;
    }

    @PostMapping("/user/new/add")
    @ResponseBody
    public Result addNewUser(@RequestBody UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        user.setDeleted(false);
        userService.saveNewUser(user);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/info/list/user")
    @ResponseBody
    public Result getUserListInfo() {
        List<UserVo> userVoList = new ArrayList<>();
        List<User> useList = userService.queryNotDeleteUser();
        for (User user : useList) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVoList.add(userVo);
        }
        return ResultGenerator.genSuccessResult(userVoList);
    }

    @GetMapping("/config/custom/get")
    public Result getCustomConfig() {
        return ResultGenerator.genSuccessResult(commonConfig.getBoss());
    }

}
