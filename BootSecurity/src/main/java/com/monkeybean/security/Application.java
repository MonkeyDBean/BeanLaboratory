package com.monkeybean.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by MonkeyBean on 2019/3/25.
 */
//启用web安全
@EnableWebSecurity
//启用方法级别的安全
@EnableGlobalMethodSecurity(prePostEnabled = true)
//启用swagger
@EnableSwagger2
//启用ldap
@EnableLdapRepositories
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
