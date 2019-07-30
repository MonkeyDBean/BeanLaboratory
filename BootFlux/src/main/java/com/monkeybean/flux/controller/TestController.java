package com.monkeybean.flux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by MonkeyBean on 2018/9/13.
 */
@RequestMapping(path = "test")
@RestController
public class TestController {

    @GetMapping("/hello/before")
    public String helloBefore() {
        return "Welcome to reactive world ~ (Before)";
    }

    @GetMapping("/hello/after")
    public Mono<String> hello() {
        return Mono.just("Welcome to reactive world ~ (After)");
    }

    @GetMapping("/url/db/get")
    public Mono<String> getDbUrl() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = Object.class.getResourceAsStream("/application.properties");
        properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return Mono.just("db url is:  " + properties.getProperty("mongo.url"));
    }
}
