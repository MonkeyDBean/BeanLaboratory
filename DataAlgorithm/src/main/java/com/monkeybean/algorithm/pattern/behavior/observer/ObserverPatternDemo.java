package com.monkeybean.algorithm.pattern.behavior.observer;

/**
 * 观察者模式：定义对象间一对多的依赖关系，当一个对象的状态发生改变时，广播通知(所有依赖于它的对象都能得到通知并自动更新)。
 * 观察者模式关键代码为在抽象类中有一个ArrayList存在观察者们。
 * 观察者模式属于行为型模式。
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class ObserverPatternDemo {
    public static void main(String[] args) {
        Subject subject = new Subject();

        new HexObserver(subject);
        new OctalObserver(subject);
        new BinaryObserver(subject);

        System.out.println("First state change: 15");
        subject.setState(15);
        System.out.println("Second state change: 10");
        subject.setState(10);
    }
}
