package com.monkeybean.algorithm.model.factory;

/**
 * Created by MonkeyBean on 2019/3/12.
 */
class Triangle implements Shape{

    @Override
    public void draw() {
        System.out.println("call Triangle::draw() method");
    }
}
