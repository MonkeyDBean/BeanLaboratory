package com.monkeybean.labo.component.doc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Restful Reference: https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design
 * 访问http://127.0.0.1:8096/monkey/swagger-ui.html
 * 有如下接口请求：
 * http://127.0.0.1:8096/monkey/swagger-resources/configuration/ui
 * http://127.0.0.1:8096/monkey/swagger-resources/configuration/security
 * http://127.0.0.1:8096/monkey/swagger-resources
 * http://127.0.0.1:8096/monkey/v2/api-docs
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    private static Contact contact = new Contact("MonkeyBean", "http://monkeybean.cn/", "monkeybean_zhang@163.com");
    private final Environment env;

    @Autowired
    public Swagger2(Environment env) {
        this.env = env;
    }

    @Bean
    public Docket createRestApi() {
        boolean active = !Boolean.parseBoolean(env.getProperty("filter.swaggerClose"));
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(active)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.monkeybean.labo"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("小系统接口，非严格Rest")
                .description("just for kill time; 若按REST标准，url path is resource, distinct operation uses different http method")
                .termsOfServiceUrl("http://127.0.0.1:8096/monkey/html/code.html")
                .contact(contact)
                .version("1.0.0")
                .build();
    }
}
