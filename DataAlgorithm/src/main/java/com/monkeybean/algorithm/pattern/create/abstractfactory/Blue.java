package com.monkeybean.algorithm.pattern.create.abstractfactory;

/**
 * 蓝色
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class Blue implements Color {

    @Override
    public void fill() {
        System.out.println("call Blue::fill() method");
    }
}

