package com.monkeybean.jpa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
@ConfigurationProperties(prefix = "monkey.common")
public class CommonConfig {
    private String boss;

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }
}
