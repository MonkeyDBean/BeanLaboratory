package com.monkeybean.algorithm.pattern.create.factory;

/**
 * 三角形
 * <p>
 * Created by MonkeyBean on 2019/3/12.
 */
public class Triangle implements Shape {

    @Override
    public void draw() {
        System.out.println("call Triangle::draw() method");
    }
}
