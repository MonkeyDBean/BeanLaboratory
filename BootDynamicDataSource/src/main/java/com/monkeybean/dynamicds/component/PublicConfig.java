package com.monkeybean.dynamicds.component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@ConfigurationProperties(prefix = "public")
@Component
@Getter
@Setter
@Slf4j
public class PublicConfig {

    /**
     * 密钥
     */
    private String secretKey;

    @PostConstruct
    public void constructFinish() {
        log.info("PublicConfig Construct Finish, secretKey is [{}]", secretKey);
    }

}
