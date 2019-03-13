package com.monkeybean.algorithm.model.factory;

/**
 * 工厂模式中，创建对象时不暴露创建过程，通过共同的接口指向新创建的对象，主要解决了接口选择问题
 * 优点为只需知道对象的名称即可创建对象，拓展性高，增加新产品只需扩展工厂类，便于管理
 * 引用自：http://www.runoob.com/design-pattern/factory-pattern.html
 *
 * Created by MonkeyBean on 2019/3/12.
 */
public class ShapeFactory {

    public Shape createShape(String shapeName){
        if(shapeName == null){
            return null;
        }
        if(shapeName.equalsIgnoreCase("circle")){
            return new Circle();
        }else if(shapeName.equalsIgnoreCase("square")){
            return new Square();
        }else if(shapeName.equalsIgnoreCase("rectangle")) {
            return new Rectangle();
        }else if(shapeName.equalsIgnoreCase("triangle")){
            return new Triangle();
        }
        return null;
    }

    public static void main(String[] args){

        //设计模式中的工厂模式，简单示例，基于面向对象的多态特性
        ShapeFactory shapeFactory = new ShapeFactory();
        Shape shape1 = shapeFactory.createShape("circle");
        shape1.draw();

        Shape shape2 = shapeFactory.createShape("square");
        shape2.draw();

        Shape shape3 = shapeFactory.createShape("rectangle");
        shape3.draw();

        Shape shape4 = shapeFactory.createShape("triangle");
        shape4.draw();
    }

}
