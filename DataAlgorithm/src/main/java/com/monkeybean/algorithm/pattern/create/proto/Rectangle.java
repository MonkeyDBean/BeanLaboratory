package com.monkeybean.algorithm.pattern.create.proto;

/**
 * 长方形实体类
 * <p>
 * Created by MonkeyBean on 2019/11/1.
 */
public class Rectangle extends Shape {
    public Rectangle() {
        type = "Rectangle";
    }

    @Override
    public void draw() {
        System.out.println("call Rectangle::draw() method");
    }
}
