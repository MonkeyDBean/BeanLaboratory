package com.monkeybean.algorithm.pattern.create.abstractfactory;

import com.monkeybean.algorithm.pattern.create.factory.Shape;

/**
 * Color及Shape的抽象工厂
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public abstract class AbstractFactory {
    public abstract Color getColor(String color);

    public abstract Shape getShape(String shape);
}
