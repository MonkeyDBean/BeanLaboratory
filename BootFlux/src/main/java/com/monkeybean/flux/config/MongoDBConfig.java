package com.monkeybean.flux.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by MonkeyBean on 2018/9/13.
 */
@Configuration
@ConfigurationProperties(prefix = "mongo")
@EnableMongoRepositories
@Getter
@Setter
public class MongoDBConfig extends AbstractMongoConfiguration {
    private String url;
    private Integer maxConnectionIdleTime = 6000;

    @Override
    @Bean
    public MongoClient mongoClient() {
        MongoClientURI mongoClientURI = new MongoClientURI(url);
        return new MongoClient(mongoClientURI);
    }

    @Override
    @Bean
    protected String getDatabaseName() {
        String arr[] = url.split("/");
        return arr[arr.length - 1];
    }

    @Bean
    public MongoClientOptions mongoClientOptions() {
        return MongoClientOptions.builder().maxConnectionIdleTime(maxConnectionIdleTime).connectTimeout(10000).build();
    }

}

