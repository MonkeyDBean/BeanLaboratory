package com.monkeybean.algorithm.pattern.create.abstractfactory;

/**
 * 工厂生成器
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class FactoryProducer {
    public static AbstractFactory getFactory(String choice) {
        if (choice.equalsIgnoreCase("shape")) {
            return new ShapeFactory();
        } else if (choice.equalsIgnoreCase("color")) {
            return new ColorFactory();
        }
        return null;
    }
}
