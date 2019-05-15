package com.monkeybean.security.component.auth;

import com.monkeybean.security.model.Account;
import com.monkeybean.security.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Created by MonkeyBean on 2019/4/18.
 */
@Component
public class DbUserDetailsService implements UserDetailsService {
    private final AccountService accountService;

    @Autowired
    public DbUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        AuthUserDetails userDetails = new AuthUserDetails();
        Account account = accountService.findByUserName(userName);
        if (account != null) {
            BeanUtils.copyProperties(account, userDetails);
            userDetails.setIsDatabase(true);
            return userDetails;
        }
        return null;
    }
}
