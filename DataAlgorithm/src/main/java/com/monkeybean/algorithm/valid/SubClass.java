package com.monkeybean.algorithm.valid;

/**
 * review类初始化顺序，派生类
 * <p>
 * Created by MonkeyBean on 2018/10/17.
 */
public class SubClass extends Parent {
    /**
     * 静态变量
     */
    public static final String sStaticField = "子类静态变量";

    static {
        System.out.println("子类静态初始化块1");
    }

    /**
     * 静态初始化块
     */
    static {
        System.out.println("sStaticField: " + sStaticField);
        System.out.println("子类静态初始化块2");
    }

    /**
     * 成员变量
     */
    public String sField = "子类变量";

    /**
     * 初始化块
     */ {
        System.out.println("sField：" + sField);
        System.out.println("子类初始化块");
    }

    /**
     * 构造函数
     */
    public SubClass() {
        System.out.println("子类构造器构造");
        System.out.println("i=" + i + ", j=" + j);
    }
}
