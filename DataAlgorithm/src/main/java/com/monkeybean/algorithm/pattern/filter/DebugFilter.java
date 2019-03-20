package com.monkeybean.algorithm.pattern.filter;

/**
 * Created by MonkeyBean on 2019/3/20.
 */
public class DebugFilter implements Filter {

    @Override
    public void execute(String request) {
        System.out.println("request log: " + request);
    }
}