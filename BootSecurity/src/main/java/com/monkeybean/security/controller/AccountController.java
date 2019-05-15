package com.monkeybean.security.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.monkeybean.security.component.constant.StatusCode;
import com.monkeybean.security.core.Result;
import com.monkeybean.security.model.Account;
import com.monkeybean.security.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by MonkeyBean on 2019/04/18.
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    private AccountService accountService;

    @PostMapping("/add")
    public Result add(Account account) {
        accountService.save(account);
        return new Result<>(StatusCode.SUCCESS);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        accountService.deleteById(id);
        return new Result<>(StatusCode.SUCCESS);
    }

    @PostMapping("/update")
    public Result update(Account account) {
        accountService.update(account);
        return new Result<>(StatusCode.SUCCESS);
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Account account = accountService.findById(id);
        return new Result<>(StatusCode.SUCCESS, account);
    }

    @PostMapping("/list")
    @SuppressWarnings("unchecked")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Account> list = accountService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new Result<>(StatusCode.SUCCESS, pageInfo);
    }
}
