package com.monkeybean.security.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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


    /**
     * 认证管理配置，可配置多种数据源，如内存，数据库，ldap
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("testAccount1").password("123456").roles("VIP1")
                .and()
                .withUser("testAccount2").password("abc").roles("VIP2");

//        //ldap配置
//        auth.ldapAuthentication()
//                .userDnPatterns(ldapProperties.getUserDnPatterns())
//                .contextSource(ldapContextSource);
//
//        //数据库配置
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }


    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

//    /**
//     * 忽略静态资源, 无需安全校验
//     **/
//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().antMatchers("/resources/static/**");
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
