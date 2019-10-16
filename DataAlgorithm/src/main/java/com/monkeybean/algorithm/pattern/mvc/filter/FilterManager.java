package com.monkeybean.algorithm.pattern.mvc.filter;

/**
 * 过滤管理器
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class FilterManager {
    private FilterChain filterChain;

    public FilterManager(Target target) {
        filterChain = new FilterChain();
        filterChain.setTarget(target);
    }

    public void setFilter(Filter filter) {
        filterChain.addFilter(filter);
    }

    public void filterRequest(String request) {
        filterChain.execute(request);
    }
}
