package com.monkeybean.algorithm.pattern.mvc.filter;

/**
 * 模拟请求处理程序
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class Target {
    public void execute(String request) {
        System.out.println("Executing request: " + request);
    }
}
