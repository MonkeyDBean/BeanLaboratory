package com.monkeybean.labo;

import com.monkeybean.labo.component.filter.CrossOriginFilter;
import com.monkeybean.labo.component.filter.TokenKeyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
@SpringBootApplication
public class MainApplication {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    private final Environment env;

    @Autowired
    public MainApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MainApplication.class);
        builder.bannerMode(Banner.Mode.LOG).run(args);
    }

    // 配置filter实现前端的跨域访问
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CrossOriginFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean filterTokenKeyBean() {
        logger.info("filter token is:-> {}, sniff token is:-> {}, dailyRequestMaxNum is:-> {}", env.getProperty("filter.token"), env.getProperty("sniff.token"), env.getProperty("other.dailyRequestMaxNum"));
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new TokenKeyFilter(Boolean.parseBoolean(env.getProperty("filter.token")), env.getProperty("sniff.token"), Integer.parseInt(env.getProperty("other.dailyRequestMaxNum"))));
        registration.addUrlPatterns("/*");
        return registration;
    }

//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//
//        //单个文件最大
//        factory.setMaxFileSize("10240KB"); //KB,MB
//
//        /// 设置总上传数据总大小
//        factory.setMaxRequestSize("102400KB");
//        return factory.createMultipartConfig();
//    }

}
