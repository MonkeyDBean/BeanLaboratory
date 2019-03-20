package com.monkeybean.algorithm.pattern.factory;

/**
 * Created by MonkeyBean on 2019/3/12.
 */
class Rectangle implements Shape {

    @Override
    public void draw() {
        System.out.println("call Rectangle::draw() method");
    }
}
