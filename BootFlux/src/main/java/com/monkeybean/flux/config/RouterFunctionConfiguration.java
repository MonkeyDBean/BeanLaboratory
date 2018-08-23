package com.monkeybean.flux.config;

import com.monkeybean.flux.model.User;
import com.monkeybean.flux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * 路由配置
 * <p>
 * Created by MonkeyBean on 2018/8/24.
 */
@Configuration
@ComponentScan(basePackages = {"com.monkeybean.flux"})
public class RouterFunctionConfiguration {


    /**
     * Web Flux方法实现接口：相比web MVC好处：异步非阻塞，提高吞吐量
     * <p>
     * Servlet规范中：
     * 请求接口：ServletRequest 或 HttpServletRequest
     * 响应接口：ServletResponse 或 HttpServletResponse
     * 在Spring5.0 重新定义了服务端的请求和响应接口，并支持RouterFunction
     * 请求接口：ServerRequest
     * 响应接口：ServerResponse
     * 优点：既可支持Servlet规范，又可支持自定义，比如Netty（Web Server）
     * <p>
     * Flux是0-N的集合（类似于集合类，最少0个，最多N个） Mono是0-1的集合,他们均是Publisher（发布器）类的子类
     * <p>
     * 为什么不使用集合类和Optional？
     * 因为Reactive中Flux和Mono是异步处理的（非阻塞）,而普通的集合是同步的（阻塞）
     */
    @Bean
    public RouterFunction<ServerResponse> getUsers(UserRepository userRepository) {
        return RouterFunctions.route(RequestPredicates.GET("/users/get"),
                request -> {
                    Collection<User> users = userRepository.findAll();
                    Flux<User> usersFlux = Flux.fromIterable(users);
                    return ServerResponse.ok().body(usersFlux, User.class);
                });
    }
}
