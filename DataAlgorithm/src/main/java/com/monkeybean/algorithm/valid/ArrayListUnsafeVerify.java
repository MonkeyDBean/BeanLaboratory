package com.monkeybean.algorithm.valid;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证ArrayList线程不安全
 * <p>
 * Created by MonkeyBean on 2019/9/3.
 */
public class ArrayListUnsafeVerify {

    /**
     * 多个线程在执行elementData[size++]=e时, 可能出现数据被覆盖掉的情况
     * 当数组元素已满时, 多个线程同时访问ensureCapacityInternal方法, 可能出现ArrayIndexOutOfBoundsException
     */
    public static void main(String[] args) throws InterruptedException {
        final List<Integer> list = new ArrayList<>();

        //起10个线程, 每个线程每隔一毫秒调用add方法
        for (int i = 0; i < 10; i++) {
            final int k = i;
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    list.add(k * 10000 + j);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        //等待线程执行完成
        Thread.sleep(1000);

        //打印list元素, 预计10000个元素, 由于ArrayList线程不安全, 实际列表元素数量小于预期
        for (int i = 0; i < list.size(); i++) {
            System.out.println("第" + (i + 1) + "个元素为：" + list.get(i));
        }
    }

}
