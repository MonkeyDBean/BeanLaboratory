package com.monkeybean.algorithm.pattern.filter;

/**
 * 拦截过滤器模式用于对应用程序的请求或响应做一些预处理/后处理
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class InterceptingFilterDemo {
    public static void main(String[] args) {
        FilterManager filterManager = new FilterManager(new Target());
        filterManager.setFilter(new AuthenticationFilter());
        filterManager.setFilter(new DebugFilter());

        Client client = new Client();
        client.setFilterManager(filterManager);
        client.sendRequest("testGetResource");
    }
}
