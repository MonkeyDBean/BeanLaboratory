package com.monkeybean.algorithm.pattern.factory;

/**
 * Created by MonkeyBean on 2019/3/12.
 */
class Square implements Shape {

    @Override
    public void draw() {
        System.out.println("call Square::draw() method");
    }
}
