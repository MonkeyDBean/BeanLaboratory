package com.monkeybean.security.component.auth;

import com.monkeybean.security.component.auth.handler.SecurityAccessDeniedHandler;
import com.monkeybean.security.component.auth.handler.SecurityAuthenticationFailureHandler;
import com.monkeybean.security.component.auth.handler.SecurityAuthenticationSuccessHandler;
import com.monkeybean.security.component.auth.ldap.AuthLdapUserDetailsMapper;
import com.monkeybean.security.component.auth.ldap.LdapProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by MonkeyBean on 2019/4/18.
 */
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private LdapProperties ldapProperties;

    @Autowired
    private LdapContextSource ldapContextSource;

    @Autowired
    private AuthLdapUserDetailsMapper authLdapUserDetailsMapper;

    @Autowired
    private DbUserDetailsService dbUserDetailsService;

    @Autowired
    private SecurityAccessDeniedHandler securityAccessDeniedHandler;

    @Autowired
    private SecurityAuthenticationFailureHandler securityAuthenticationFailureHandler;

    @Autowired
    private SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler;

    @Value("${custom.accountAdmin}")
    private String accountAdmin;

    @Value("${custom.accountPwd}")
    private String accountPwd;

    /**
     * 认证管理配置，可配置多种数据源，如内存，数据库，ldap
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //本地配置
        auth.inMemoryAuthentication()
                .withUser("justSimpleRoot").password("123456_abc")
                .and()
                .withUser(accountAdmin).password(accountPwd);

        //ldap配置
        auth.ldapAuthentication()
                .userDnPatterns(ldapProperties.getUserDnPatterns())
                .contextSource(ldapContextSource);

        //数据库配置
        auth.userDetailsService(dbUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 拦截器配置
     */
    protected void configure(HttpSecurity http) throws Exception {

        //关闭csrf
        http.csrf().disable()

                //允许跨域
                .cors();

        //指定登录url
        http.formLogin().loginPage("/login")

                //认证成功处理Handler
                .successHandler(securityAuthenticationSuccessHandler)

                //认证失败处理Handler
                .failureHandler(securityAuthenticationFailureHandler);

        //权限不足时处理handler
        http.exceptionHandling().accessDeniedHandler(securityAccessDeniedHandler);

        //定义登录消息
        http.authorizeRequests().antMatchers("/login", "/code",

                //swagger放行
                "/swagger*/**", "/v2/api-docs", "/webjars/**",

                //资源文件放行
                "/html/**")
                .permitAll()

                //其他请求需要认证
                .anyRequest().authenticated();
    }

//    /**
//     * 配置Spring Security的Filter链
//     */
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
