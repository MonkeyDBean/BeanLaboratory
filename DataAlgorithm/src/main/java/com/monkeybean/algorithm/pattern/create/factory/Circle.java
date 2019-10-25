package com.monkeybean.algorithm.pattern.create.factory;

/**
 * 圆形
 * <p>
 * Created by MonkeyBean on 2019/3/12.
 */
public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("call Circle::draw() method");
    }
}
