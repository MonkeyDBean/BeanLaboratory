package com.monkeybean.security.component.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by MonkeyBean on 2019/4/20.
 */
@Component
public class UserInfoHolder {

    /**
     * 获取当前用户信息
     */
    public AuthUserDetails getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthUserDetails) {
            return (AuthUserDetails) principal;
        }
        return null;
    }
}
