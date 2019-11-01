package com.monkeybean.algorithm.pattern.create.proto;

/**
 * 原型(Prototype)模式用于创建重复的对象, 属于创建型模式.
 * 此模式实现了一个原型接口, 该接口用于创建当前对象的克隆, 当创建代价比较大时使用此模式.
 * 如一个对象在一个高代价的数据库操作之后被创建, 可以缓存该对象, 当下一个请求时返回它的克隆.
 * <p>
 * Created by MonkeyBean on 2019/11/1.
 */
public class PrototypePatternDemo {
    public static void main(String[] args) {
        ShapeCache.loadCache();

        Shape clonedShape = ShapeCache.getShape("1");
        System.out.println("Shape: " + clonedShape.getType());

        Shape clonedShape2 = ShapeCache.getShape("2");
        System.out.println("Shape: " + clonedShape2.getType());

        Shape clonedShape3 = ShapeCache.getShape("3");
        System.out.println("Shape: " + clonedShape3.getType());
    }
}
