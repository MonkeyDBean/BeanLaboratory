package com.monkeybean.algorithm.pattern.create.abstractfactory;

/**
 * 绿色
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class Green implements Color {

    @Override
    public void fill() {
        System.out.println("call Green::fill() method");
    }
}
