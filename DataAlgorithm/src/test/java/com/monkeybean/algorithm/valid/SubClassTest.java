package com.monkeybean.algorithm.valid;

import org.junit.Test;

/**
 * Created by MonkeyBean on 2018/10/17.
 */
public class SubClassTest {

    /**
     * 通过new 新建对象，首先是类的装载，然后是对象实例化
     * 类的装载：1.先装载父类，再装载子类 2.装载类时，执行静态动作：初始化静态变量和执行静态代码程序（根据代码顺序执行）
     * 对象实例化：1.先实例化父类，再实例化子类 2. 实例化类时，先完成实例变量的初始化，再调用构造函数
     * 执行顺序：父类的静态代码-->子类的静态代码-->父类的非静态实例变量(或代码块)-->父类的构造方法-->子类的非静态实例变量(或代码块)-->子类的构造方法
     */
    @Test
    public void testInitSequence() {
        System.out.println("类初始化顺序测试");
        new SubClass();
    }
}