package com.monkeybean.algorithm.pattern.mvc.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤器链
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class FilterChain {
    private List<Filter> filters = new ArrayList<>();
    private Target target;

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void execute(String request) {
        for (Filter filter : filters) {
            filter.execute(request);
        }
        target.execute(request);
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
