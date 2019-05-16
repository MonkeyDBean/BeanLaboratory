package com.monkeybean.security.component.auth.ldap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

/**
 * Created by MonkeyBean on 2019/4/20.
 */
@Entry(objectClasses = "inetOrgPerson")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class LdapUserInfo {

    @Id
    private Name name;
    @Attribute(name = "cn")
    private String userName;
    @Attribute(name = "sn")
    private String realName;
    @Attribute(name = "userPassword")
    private String password;
    @Attribute(name = "mail")
    private String email;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
