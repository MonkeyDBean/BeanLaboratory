package com.monkeybean.algorithm.pattern.create.proto;

/**
 * 圆形实体类
 * <p>
 * Created by MonkeyBean on 2019/11/1.
 */
public class Circle extends Shape {
    public Circle() {
        type = "Circle";
    }

    @Override
    public void draw() {
        System.out.println("call Circle::draw() method");
    }
}
