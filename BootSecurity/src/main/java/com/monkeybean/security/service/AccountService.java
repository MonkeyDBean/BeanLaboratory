package com.monkeybean.security.service;

import com.monkeybean.security.core.Service;
import com.monkeybean.security.model.Account;

/**
 * Created by MonkeyBean on 2019/04/18.
 */
public interface AccountService extends Service<Account> {

    /**
     * 通过用户名查找账户信息
     *
     * @param userName 用户名
     */
    Account findByUserName(String userName);
}
