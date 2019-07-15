package com.monkeybean.algorithm.other;

import java.util.concurrent.*;

/**
 * Created by MonkeyBean on 2019/7/11.
 */
public class FutureSimpleTest {

    public static void main(String[] args) {

        //FutureTask
        MyTask task = new MyTask();
        FutureTask<Integer> futureTask = new FutureTask<>(task);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(futureTask);
        executor.shutdown();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException occurs: " + e);
        }
        System.out.println("Main Thread is Running");
        try {
            //System.out.println("task result is: " + futureTask.get());
            System.out.println("task result is: " + futureTask.get(5000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Exception occurs: " + e);
        }
        System.out.println("all task is finished");

        //CompletableFuture
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(s -> s + " World").thenApply(String::toUpperCase);
        try {
            System.out.println("completableFuture test: " + completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception occurs: " + e);
        }
    }
}

