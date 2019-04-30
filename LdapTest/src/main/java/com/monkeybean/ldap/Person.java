package com.monkeybean.ldap;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

/**
 * Created by MonkeyBean on 2019/4/25.
 */
@Entry(objectClasses = "inetOrgPerson")
public class Person {
    @Id
    private Name name;

    @Attribute(name = "cn")
    private String commonName;

    @Attribute(name = "sn")
    private String suerName;

    @Attribute(name = "mobile")
    private String mobile;

    @Attribute(name = "mail")
    private String email;

    @Attribute(name = "userPassword")
    private String userPassword;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSuerName() {
        return suerName;
    }

    public void setSuerName(String suerName) {
        this.suerName = suerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
