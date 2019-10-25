package com.monkeybean.algorithm.pattern.create.factory;

/**
 * 正方形
 * <p>
 * Created by MonkeyBean on 2019/3/12.
 */
public class Square implements Shape {

    @Override
    public void draw() {
        System.out.println("call Square::draw() method");
    }
}
