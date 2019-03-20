package com.monkeybean.algorithm.pattern.singleton;

/**
 * 双检锁/双重校验锁，线程安全且性能高
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class SingleObject3 {
    private static volatile SingleObject3 singleton;

    private SingleObject3() {
    }

    public static SingleObject3 getInstance() {
        if (singleton == null) {
            synchronized (SingleObject3.class) {
                if (singleton == null) {
                    singleton = new SingleObject3();
                }
            }
        }
        return singleton;
    }
}
