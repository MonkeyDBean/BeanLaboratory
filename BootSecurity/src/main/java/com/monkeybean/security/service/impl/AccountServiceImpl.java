package com.monkeybean.security.service.impl;

import com.monkeybean.security.core.AbstractService;
import com.monkeybean.security.dao.AccountMapper;
import com.monkeybean.security.model.Account;
import com.monkeybean.security.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by MonkeyBean on 2019/04/18.
 */
@Service
@Transactional
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {
    @Resource
    private AccountMapper accountMapper;

    @Override
    public Account findByUserName(String userName) {
        Condition condition = new Condition(Account.class);
        condition.createCriteria().andEqualTo("enabled", true);
        condition.createCriteria().andEqualTo("userName", userName);
        List<Account> users = accountMapper.selectByCondition(condition);
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

}
