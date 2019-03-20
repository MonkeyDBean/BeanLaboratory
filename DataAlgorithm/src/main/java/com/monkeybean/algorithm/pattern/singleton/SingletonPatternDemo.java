package com.monkeybean.algorithm.pattern.singleton;

/**
 * 单例模式为单个类仅有一个实例, 该类创建该唯一实例，并给其他所有对象提供这一实例
 * 单例模式属于创建型模式
 * 单例模式实现方式包括以下几种：
 * 饿汉式：类装载时实例化，优点为无需加锁，执行效率高，缺点为类加载时实例化，浪费内存
 * 懒汉式：首次调用时实例化，多线程是否安全取决于getInstance方法是否加synchronized锁
 * 双检锁：synchronized + volatile, 线程安全且高性能
 * 登记式/静态内部类：功效同双检锁，利用classloader机制来保证初始化instance时只有一个线程
 * 其他：枚举方式
 * <p>
 * 本例属于饿汉式
 * 引用自：http://www.runoob.com/design-pattern/singleton-pattern.html
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class SingletonPatternDemo {

    public static void main(String[] args) {

        //不合法的构造函数
        //编译时错误：构造函数 SingleObject1() 是不可见的
        //SingleObject1 object = new SingleObject1();

        //获取唯一可用的对象
        SingleObject1 object = SingleObject1.getInstance();

        //显示消息
        object.showMessage();
    }
}
