package com.monkeybean.algorithm.pattern.mvc.filter;

/**
 * 过滤接口
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public interface Filter {
    void execute(String request);
}
