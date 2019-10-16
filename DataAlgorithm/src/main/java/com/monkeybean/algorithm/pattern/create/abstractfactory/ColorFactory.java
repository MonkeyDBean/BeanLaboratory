package com.monkeybean.algorithm.pattern.create.abstractfactory;

import com.monkeybean.algorithm.pattern.create.factory.Shape;

/**
 * 颜色工厂
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class ColorFactory extends AbstractFactory {

    @Override
    public Shape getShape(String shapeType) {
        return null;
    }

    @Override
    public Color getColor(String color) {
        if (color == null) {
            return null;
        }
        if (color.equalsIgnoreCase("red")) {
            return new Red();
        } else if (color.equalsIgnoreCase("green")) {
            return new Green();
        } else if (color.equalsIgnoreCase("blue")) {
            return new Blue();
        }
        return null;
    }
}
