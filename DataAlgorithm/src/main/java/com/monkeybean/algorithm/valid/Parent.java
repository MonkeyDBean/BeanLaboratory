package com.monkeybean.algorithm.valid;

/**
 * review类初始化顺序，基类
 * <p>
 * Created by MonkeyBean on 2018/10/17.
 */
public class Parent {

    /**
     * 静态变量
     */
    public static String pStaticField = "父类静态变量";

    /**
     * 静态初始化块
     */
    static {
        System.out.println("pStaticField: " + pStaticField);
        System.out.println("父类静态初始化块");
    }

    /**
     * 成员变量
     */
    public String pField = "父类变量";
    protected int i = 1;
    protected int j = 2;

    /**
     * 初始化块
     */ {
        System.out.println("pField：" + pField);
        System.out.println("父类初始化块");
    }

    /**
     * 构造函数
     */
    public Parent() {
        System.out.println("父类构造器构造");
        System.out.println("i=" + i + ", j=" + j);
        j = 20;
    }

}
