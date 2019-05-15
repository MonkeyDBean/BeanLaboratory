package com.monkeybean.security.component.auth.ldap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * Created by MonkeyBean on 2019/4/20.
 */
@Configuration
public class LdapConfiguration {

    @Bean
    public LdapProperties ldapProperties() {
        return new LdapProperties();
    }

    @Bean
    public LdapContextSource ldapContextSource(LdapProperties ldapProperties) {
        LdapContextSource source = new LdapContextSource();
        source.setUserDn(ldapProperties.getUsername());
        source.setPassword(ldapProperties.getPassword());
        source.setBase(ldapProperties.getBase());
        source.setUrl(ldapProperties.getUrls());
        return source;
    }
}
