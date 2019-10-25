package com.monkeybean.algorithm.pattern.create.factory;

/**
 * 长方形
 * <p>
 * Created by MonkeyBean on 2019/3/12.
 */
public class Rectangle implements Shape {

    @Override
    public void draw() {
        System.out.println("call Rectangle::draw() method");
    }
}
