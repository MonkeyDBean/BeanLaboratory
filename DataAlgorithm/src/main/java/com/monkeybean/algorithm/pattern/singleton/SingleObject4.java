package com.monkeybean.algorithm.pattern.singleton;

/**
 * 静态内部类方式
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class SingleObject4 {
    private SingleObject4() {
    }

    public static final SingleObject4 getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * SingleObject4类装载，instance不一定被初始化。因为SingletonHolder类没有被主动使用.
     * 只有通过显式调用getInstance方法，才会显式装载SingletonHolder类, 从而实例化instance
     */
    private static class SingletonHolder {
        private static final SingleObject4 INSTANCE = new SingleObject4();
    }
}
