package com.monkeybean.algorithm.pattern.observer;

/**
 * Created by MonkeyBean on 2019/3/20.
 */
public abstract class Observer {
    protected Subject subject;

    public abstract void update();
}
