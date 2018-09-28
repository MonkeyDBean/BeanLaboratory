package com.monkeybean.flux.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Just Test
 * <p>
 * Created by MonkeyBean on 2018/9/26.
 */
@Order(1)
@Component
public class RequestFilter implements WebFilter {
    private static final String REQUEST_KEY = "just_test_request_filter_value666";
    private static Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logger.info("request is: {}", request.getURI());
        if (request.getPath().toString().contains("test")) {
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            String testFilterValue = queryParams.getFirst("test_filter_key");
            if (testFilterValue == null || !testFilterValue.toLowerCase().contains(REQUEST_KEY)) {
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                ServerHttpRequest authErrorReq = request.mutate().path("/error/401").build();
                ServerWebExchange authErrorExchange = exchange.mutate().request(authErrorReq).build();
                return chain.filter(authErrorExchange);
            }
            exchange.getResponse().getHeaders().add("test-request-filer", "pass");
        }
        return chain.filter(exchange);
    }
}
