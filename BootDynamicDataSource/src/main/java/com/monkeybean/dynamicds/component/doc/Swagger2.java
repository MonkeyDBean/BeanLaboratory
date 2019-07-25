package com.monkeybean.dynamicds.component.doc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    private static Contact contact = new Contact("MonkeyBean", "http://monkeybean.cn/", "monkeybean_zhang@163.com");

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.monkeybean.dynamicds.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("动态数据源测试")
                .description("just simple test")
                .termsOfServiceUrl("http://127.0.0.1:8098/monkey_d_bean/swagger-ui.html")
                .contact(contact)
                .version("1.0.0")
                .build();
    }
}
