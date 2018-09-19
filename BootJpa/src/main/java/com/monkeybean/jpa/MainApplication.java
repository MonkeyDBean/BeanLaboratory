package com.monkeybean.jpa;

import com.monkeybean.jpa.config.CommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties({CommonConfig.class})
@EnableJpaAuditing
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}