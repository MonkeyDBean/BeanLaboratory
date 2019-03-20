package com.monkeybean.algorithm.pattern.filter;

/**
 * Created by MonkeyBean on 2019/3/20.
 */
public class Client {
    FilterManager filterManager;

    public void setFilterManager(FilterManager filterManager) {
        this.filterManager = filterManager;
    }

    public void sendRequest(String request) {
        filterManager.filterRequest(request);
    }
}
