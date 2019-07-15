package com.monkeybean.algorithm.other;

import java.util.concurrent.Callable;

/**
 * * Created by MonkeyBean on 2019/7/11.
 */
public class MyTask implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("myTask is called");

        //模拟耗时计算
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum += i;
        }
        return sum;
    }
}