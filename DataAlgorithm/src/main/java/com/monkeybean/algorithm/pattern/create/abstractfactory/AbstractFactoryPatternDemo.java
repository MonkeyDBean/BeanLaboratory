package com.monkeybean.algorithm.pattern.create.abstractfactory;

import com.monkeybean.algorithm.pattern.create.factory.Shape;

/**
 * 抽象工厂模式是围绕一个超级工厂创建其他工厂, 该超级工厂被称为其他工厂的工厂。
 * 关键代码为在一个工厂里聚合多个同类产品。
 * 优点为当一个产品族的多个对象被设计成一起工作时, 可以保证客户端始终只使用同一个产品族中的对象。
 * 缺点为产品族扩展非常困难, 增加一个系列的某个产品, 即要在抽象的Creator里增加代码, 也要在具体的实现里增加代码。
 * 使用场景如QQ换整套皮肤、生成不同操作系统的程序。
 * <p>
 * Created by MonkeyBean on 2019/10/16.
 */
public class AbstractFactoryPatternDemo {
    public static void main(String[] args) {

        //获取形状工厂
        AbstractFactory shapeFactory = FactoryProducer.getFactory("shape");

        //获取形状为Circle的对象
        Shape shape1 = shapeFactory.getShape("circle");

        //调用Circle的draw方法
        shape1.draw();

        //获取形状为Rectangle的对象
        Shape shape2 = shapeFactory.getShape("rectangle");

        //调用Rectangle的draw方法
        shape2.draw();

        //获取形状为Square的对象
        Shape shape3 = shapeFactory.getShape("square");

        //调用Square的draw方法
        shape3.draw();

        //获取形状为Triangle的对象
        Shape shape4 = shapeFactory.getShape("triangle");

        //调用Triangle的draw方法
        shape4.draw();

        //获取颜色工厂
        AbstractFactory colorFactory = FactoryProducer.getFactory("color");

        //获取颜色为Red的对象
        Color color1 = colorFactory.getColor("red");

        //调用Red的fill方法
        color1.fill();

        //获取颜色为Green的对象
        Color color2 = colorFactory.getColor("green");

        //调用Green的fill方法
        color2.fill();

        //获取颜色为Blue的对象
        Color color3 = colorFactory.getColor("blue");

        //调用Blue的fill方法
        color3.fill();
    }
}
