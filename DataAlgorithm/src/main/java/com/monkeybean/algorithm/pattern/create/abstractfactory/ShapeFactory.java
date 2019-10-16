package com.monkeybean.algorithm.pattern.create.abstractfactory;

import com.monkeybean.algorithm.pattern.create.factory.*;

/**
 * 形状工厂
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class ShapeFactory extends AbstractFactory {

    @Override
    public Shape getShape(String shapeName) {
        if (shapeName == null) {
            return null;
        }
        if (shapeName.equalsIgnoreCase("circle")) {
            return new Circle();
        } else if (shapeName.equalsIgnoreCase("square")) {
            return new Square();
        } else if (shapeName.equalsIgnoreCase("rectangle")) {
            return new Rectangle();
        } else if (shapeName.equalsIgnoreCase("triangle")) {
            return new Triangle();
        }
        return null;
    }

    @Override
    public Color getColor(String color) {
        return null;
    }
}
