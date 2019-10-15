package com.monkeybean.algorithm.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * ForkJoin主要用于并行计算中, 和MapReduce原理类似, 都是把大的计算任务拆分成多个小任务并行计算
 * <p>
 * Created by MonkeyBean on 2019/10/15.
 */
public class ForkJoinTest extends RecursiveTask<Integer> {
    private final int threshold = 5;
    private int first;
    private int last;

    public ForkJoinTest(int first, int last) {
        this.first = first;
        this.last = last;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinTest forkJoinTest = new ForkJoinTest(1, 10000);

        //通过ForkJoinPool(特殊的线程池, 线程数量取决于CPU核数)启动
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Future result = forkJoinPool.submit(forkJoinTest);
        System.out.println(result.get());
    }

    @Override
    protected Integer compute() {
        int result = 0;

        // 任务足够小则直接计算
        if (last - first <= threshold) {
            for (int i = first; i <= last; i++) {
                result += i;
            }
        } else {

            // 拆分成小任务
            int middle = first + (last - first) / 2;
            ForkJoinTest leftTask = new ForkJoinTest(first, middle);
            ForkJoinTest rightTask = new ForkJoinTest(middle + 1, last);
            leftTask.fork();
            rightTask.fork();
            result = leftTask.join() + rightTask.join();
        }
        return result;
    }
}
