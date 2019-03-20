package com.monkeybean.algorithm.pattern.singleton;

/**
 * 懒汉式，加锁，线程安全
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class SingleObject2 {
    private static SingleObject2 instance;

    private SingleObject2() {
    }

    public static synchronized SingleObject2 getInstance() {
        if (instance == null) {
            instance = new SingleObject2();
        }
        return instance;
    }
}
