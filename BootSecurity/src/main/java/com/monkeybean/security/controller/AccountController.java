package com.monkeybean.security.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.monkeybean.security.component.auth.ldap.LdapUserInfo;
import com.monkeybean.security.component.auth.ldap.LdapUserInfoRepository;
import com.monkeybean.security.component.constant.StatusCode;
import com.monkeybean.security.component.reqres.Result;
import com.monkeybean.security.component.reqres.UserRes;
import com.monkeybean.security.model.Account;
import com.monkeybean.security.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MonkeyBean on 2019/04/18.
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private LdapUserInfoRepository ldapUserInfoRepository;

    /**
     * 新增账户到数据库
     */
    @PostMapping("/add/db")
    public Result add(Account account) {
        accountService.save(account);
        return new Result<>(StatusCode.SUCCESS);
    }

    /**
     * 删除数据库中某个账户
     */
    @PostMapping("/delete/db")
    public Result delete(@RequestParam Integer id) {
        accountService.deleteById(id);
        return new Result<>(StatusCode.SUCCESS);
    }

    /**
     * 删除数据库某个账户
     */
    @PostMapping("/update/db")
    public Result update(Account account) {
        accountService.update(account);
        return new Result<>(StatusCode.SUCCESS);
    }

    /**
     * 根据名称返回账户信息，注:仅数据库及ldap,不包含本地配置用户
     */
    @PreAuthorize(value = "@permissionValidator.isSuperAdmin()")
    @PostMapping("/detail")
    public Result detail(@RequestParam String name) {
        UserRes userRes = new UserRes();
        LdapUserInfo ldapUserInfo = ldapUserInfoRepository.findByUserName(name);
        Account account = accountService.findBy("userName", name);
        if (ldapUserInfo != null) {
            BeanUtils.copyProperties(ldapUserInfo, userRes);
            userRes.setDatabase(false);
            userRes.setEnabled(true);
        } else if (account != null) {
            BeanUtils.copyProperties(account, userRes);
            userRes.setDatabase(true);
        }
        return new Result<>(StatusCode.SUCCESS, userRes);
    }

    /**
     * 列出数据库账户信息,分页
     */
    @PreAuthorize(value = "@permissionValidator.isSuperAdmin()")
    @PostMapping("/list/db")
    @SuppressWarnings("unchecked")
    public Result dbAccountList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        PageHelper.startPage(page, size);
        List<Account> list = accountService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return new Result<>(StatusCode.SUCCESS, pageInfo);
    }

    /**
     * 列出所有账户信息，注:仅数据库及ldap,不包含本地配置用户
     */
    @PreAuthorize(value = "@permissionValidator.isSuperAdmin()")
    @PostMapping("/list/all")
    @SuppressWarnings("unchecked")
    public Result list() {
        List<UserRes> userResList = new ArrayList<>();
        List<Account> dbList = accountService.findAll();
        for (Account account : dbList) {
            UserRes userRes = new UserRes();
            BeanUtils.copyProperties(account, userRes);
            userRes.setDatabase(true);
            userResList.add(userRes);
        }
        Iterable<LdapUserInfo> ldapUserList = ldapUserInfoRepository.findAll();
        for (LdapUserInfo ldapUserInfo : ldapUserList) {
            UserRes userRes = new UserRes();
            BeanUtils.copyProperties(ldapUserInfo, userRes);
            userRes.setDatabase(false);
            userRes.setEnabled(true);
            userResList.add(userRes);
        }
        return new Result<>(StatusCode.SUCCESS, userResList);
    }
}
