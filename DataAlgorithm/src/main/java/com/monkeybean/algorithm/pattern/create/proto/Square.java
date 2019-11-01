package com.monkeybean.algorithm.pattern.create.proto;

/**
 * 正方形实体类
 * <p>
 * Created by MonkeyBean on 2019/11/1.
 */
public class Square extends Shape {
    public Square() {
        type = "Square";
    }

    @Override
    public void draw() {
        System.out.println("call Square::draw() method");
    }
}
