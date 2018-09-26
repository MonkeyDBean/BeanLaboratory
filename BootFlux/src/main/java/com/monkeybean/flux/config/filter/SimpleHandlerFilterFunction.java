package com.monkeybean.flux.config.filter;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Just Test, 特定的简单拦截器, 查询字符串包含合法key
 *
 * Created by MonkeyBean on 2018/9/21.
 */
public class SimpleHandlerFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    private static final String REQUEST_KEY = "hwdgrmyxbwstl123";

    @Override
    public Mono<ServerResponse> filter(ServerRequest serverRequest, HandlerFunction<ServerResponse> handlerFunction) {
        String queryString = serverRequest.uri().getQuery();
        if (queryString == null || !queryString.contains(REQUEST_KEY)) {
            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        }
        return handlerFunction.handle(serverRequest);
    }
}
