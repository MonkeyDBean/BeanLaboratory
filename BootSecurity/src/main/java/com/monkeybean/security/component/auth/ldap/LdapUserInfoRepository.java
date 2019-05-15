package com.monkeybean.security.component.auth.ldap;

import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

/**
 * Created by MonkeyBean on 2019/4/20.
 */
public interface LdapUserInfoRepository extends CrudRepository<LdapUserInfo, Name> {
    LdapUserInfo findByUserName(String userName);
}