package com.monkeybean.algorithm.pattern.proxy;

/**
 * 代理模式：为其他对象提供一种代理以控制对这个对象的访问，即加中间层，如Windows的快捷方式
 * 代理模式属于结构型模式
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class ProxyPatternDemo {

    public static void main(String[] args) {
        Image image = new ProxyImage("test.png");

        //首次调用，图像将从磁盘加载
        image.display();

        //图像不需要从磁盘加载
        image.display();
    }
}
