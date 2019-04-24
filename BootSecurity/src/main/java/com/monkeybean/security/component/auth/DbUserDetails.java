package com.monkeybean.security.component.auth;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by MonkeyBean on 2019/4/18.
 */
public class DbUserDetails implements UserDetails {
    private Long id;
    private String userName;

    @JSONField(serialize = false)
    private String password;
    private String phone;
    private Boolean enabled;

    /**
     * 是否是数据库账号
     */
    @JSONField(serialize = false)
    private boolean isDatabase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
