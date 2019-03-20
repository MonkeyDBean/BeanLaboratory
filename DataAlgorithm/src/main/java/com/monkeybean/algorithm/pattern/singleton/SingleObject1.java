package com.monkeybean.algorithm.pattern.singleton;

/**
 * 饿汉式，线程安全
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class SingleObject1 {

    //初始创建SingleObject对象
    private static SingleObject1 instance = new SingleObject1();

    //设置构造函数为private，这样该类就不会被实例化
    private SingleObject1() {
    }

    //获取唯一可用的对象
    public static SingleObject1 getInstance() {
        return instance;
    }

    public void showMessage() {
        System.out.println("Hello Singleton Pattern!");
    }
}
