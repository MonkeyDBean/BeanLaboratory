package com.monkeybean.algorithm.pattern.create.abstractfactory;

/**
 * 红色
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class Red implements Color {

    @Override
    public void fill() {
        System.out.println("call Red::fill() method");
    }
}
