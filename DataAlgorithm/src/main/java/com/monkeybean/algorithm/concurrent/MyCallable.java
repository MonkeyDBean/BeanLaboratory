package com.monkeybean.algorithm.concurrent;

import java.util.concurrent.Callable;

/**
 * 实现Callable接口
 * <p>
 * Created by MonkeyBean on 2019/10/15.
 */
public class MyCallable implements Callable<Integer> {
    public Integer call() {
        System.out.println("MyCallable run");
        return 100;
    }
}
