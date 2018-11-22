package com.monkeybean.flux;

import com.monkeybean.flux.model.MyEvent;
import com.monkeybean.flux.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;

import java.net.URL;
import java.util.Properties;

/**
 * Created by MonkeyBean on 2018/8/23.
 */
@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        //直接启动(默认仅读取resources目录下的application.properties文件 以及 application.properties文件中参数spring.profiles.active指定的配置文件)
        //SpringApplication.run(Application.class, args);

        //读取特定目录下的所有properties配置文件
        URL resourceUrl = Application.class.getClassLoader().getResource("properties");
        if (resourceUrl == null) {
            logger.error("resource dir is null");
            return;
        }
        String resourcePath = resourceUrl.getPath();
        Properties p = PropertiesUtil.loadPropertiesByNio(resourcePath);
        //Properties p = PropertiesUtil.loadPropertiesByStream(resourcePath);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
        builder.properties(p).build();
        builder.run(args);
    }

    @Bean
    public CommandLineRunner initData(MongoOperations mongo) {
        return (String... args) -> {
            //mongo.dropCollection(MyEvent.class);
            if (!mongo.collectionExists(MyEvent.class)) {
                mongo.createCollection(MyEvent.class, CollectionOptions.empty().maxDocuments(200).size(100000).capped());
            }
        };
    }
}
