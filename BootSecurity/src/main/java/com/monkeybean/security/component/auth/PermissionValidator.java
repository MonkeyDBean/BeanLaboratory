package com.monkeybean.security.component.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by MonkeyBean on 2019/4/20.
 */
@Component
public class PermissionValidator {
    @Value("${custom.superAdmins}")
    private String superAdmins;

    @Autowired
    private UserInfoHolder userInfoHolder;

    /**
     * 是否是超级管理员
     */
    public boolean isSuperAdmin() {
        return superAdmins != null && superAdmins.contains(userInfoHolder.getUser().getUsername());
    }

    /**
     * 是否为当前用户
     */
    public boolean isUserSelf(String userName) {
        return userInfoHolder.getUser().getUsername().equals(userName);
    }
}
