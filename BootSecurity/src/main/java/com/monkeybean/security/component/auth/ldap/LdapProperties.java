package com.monkeybean.security.component.auth.ldap;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by MonkeyBean on 2019/4/20.
 */
@ConfigurationProperties(prefix = "spring.ldap")
public class LdapProperties {
    private String urls;
    private String base;
    private String username;
    private String password;
    private String userDnPatterns;

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserDnPatterns() {
        return userDnPatterns;
    }

    public void setUserDnPatterns(String userDnPatterns) {
        this.userDnPatterns = userDnPatterns;
    }
}

