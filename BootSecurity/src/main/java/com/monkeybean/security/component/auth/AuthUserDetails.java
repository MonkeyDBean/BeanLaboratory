package com.monkeybean.security.component.auth;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 自定义UserDetails类
 * <p>
 * Created by MonkeyBean on 2019/4/18.
 */
public class AuthUserDetails implements UserDetails {
    private String userName;

    @JSONField(serialize = false)
    private String password;
    private String phone;
    private String email;

    private Boolean enabled;

    /**
     * 是否是数据库账号
     */
    private boolean isDatabase;

    @Override
    public String getUsername() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getIsDatabase() {
        return isDatabase;
    }

    public void setIsDatabase(boolean isDatabase) {
        this.isDatabase = isDatabase;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //所有用户默认为user角色
        return Collections.singletonList(() -> "role_user");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
