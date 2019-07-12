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

    /**
     * 注解@Transactional
     * https://www.jianshu.com/p/5687e2a38fbc
     * https://blog.csdn.net/zheng0518/article/details/52214310
     * https://www.cnblogs.com/shellj/p/spring-transaction-bu-sheng-xiao-de-yi-xie-yuan-yi.html
     */
    @Override
    @Transactional(readOnly = true)
    public Account findByUserName(String userName) {
        Condition condition = new Condition(Account.class);
        condition.createCriteria().andEqualTo("enabled", true)
                .andEqualTo("userName", userName);
        List<Account> users = accountMapper.selectByCondition(condition);
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

}
