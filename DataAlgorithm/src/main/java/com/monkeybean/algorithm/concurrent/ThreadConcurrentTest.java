package com.monkeybean.algorithm.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程测试
 * <p>
 * 内存模型三大特性为原子性、可见性、有序性
 * 内存模型交互操作如下:
 * read: 把一个变量的值从主内存传输到工作内存中
 * load: 在read之后执行，把read得到的值放入工作内存的变量副本中
 * use: 把工作内存中一个变量的值传递给执行引擎
 * assign: 把一个从执行引擎接收到的值赋给工作内存的变量
 * store: 把工作内存的一个变量的值传送到主内存中
 * write: 在store之后执行, 把store得到的值放入主内存的变量中
 * lock & unlock: 作用于主内存的变量
 * <p>
 * Created by MonkeyBean on 2019/10/15.
 */
public class ThreadConcurrentTest {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        //三种使用线程的方法：1.实现Runnable接口; 2.实现Callable接口; 3.继承Thread类
        //与Runnable相比, Callable可以有返回值，返回值通过FutureTask进行封装
        //推荐实现接口而不是继承Thread: Java不支持多重继承, 因此继承了Thread类就无法继承其它类, 但是可以实现多个接口; 类可能只要求可执行，继承整个Thread类开销较大
        MyRunnable myRunnable = new MyRunnable();
        Thread thread1 = new Thread(myRunnable);
        thread1.start();
        MyCallable myCallable = new MyCallable();
        FutureTask<Integer> ft = new FutureTask<>(myCallable);
        Thread thread2 = new Thread(ft);
        thread2.start();
        System.out.println(ft.get());
        MyThread myThread = new MyThread();
        myThread.start();

        //wait, notify; await, signal
        ExecutorService executorService1 = Executors.newCachedThreadPool();
        ThreadConcurrentTest testObject1 = new ThreadConcurrentTest();
        executorService1.execute(testObject1::after1);
        executorService1.execute(testObject1::before1);
        ThreadConcurrentTest testObject2 = new ThreadConcurrentTest();
        executorService1.execute(testObject2::after2);
        executorService1.execute(testObject2::before2);

        //join
        ThreadConcurrentTest testObject3 = new ThreadConcurrentTest();
        testObject3.testJoin();

        //CountDownLatch用来控制一个或者多个线程等待多个线程
        //维护了一个计数器, 每次调用countDown方法使计数器的值减1, 减到0时, 因调用await方法而等待的线程就会被唤醒
        final int totalThreadNum = 10;
        CountDownLatch countDownLatch = new CountDownLatch(totalThreadNum);
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        for (int i = 0; i < totalThreadNum; i++) {
            final int temp = i;
            executorService2.execute(() -> {
                System.out.println("thread " + temp + " run");
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("end");
        executorService2.shutdown();

        //CyclicBarrier用来控制多个线程互相等待, 只有多个线程都到达时, 这些线程才会继续执行
        //与CountdownLatch类似, 都是通过计数器维护, 区别是CyclicBarrier计数器可以通过调用reset方法循环使用
        CyclicBarrier cyclicBarrier = new CyclicBarrier(totalThreadNum);
        ExecutorService executorService3 = Executors.newCachedThreadPool();
        for (int i = 0; i < totalThreadNum; i++) {
            final int temp = i;
            executorService3.execute(() -> {
                System.out.println("thread " + temp + " before");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("thread " + temp + " after");
            });
        }
        executorService3.shutdown();

        //Semaphore类似操作系统中的信号量, 可以控制对互斥资源的访问线程数
        //本例为模拟对某个服务的并发请求, 每次只能由3个客户端同时访问, 请求总数为10
        final int clientCount = 3;
        final int totalRequestCount = 10;
        Semaphore semaphore = new Semaphore(clientCount);
        ExecutorService executorService4 = Executors.newCachedThreadPool();
        for (int i = 0; i < totalRequestCount; i++) {
            final int temp = i;
            executorService4.execute(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("thread: " + temp + ", availablePermits: " + semaphore.availablePermits());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            });
        }
        executorService4.shutdown();

        //FutureTask可用于异步获取执行结果或取消执行任务的场景
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            int result = 0;
            for (int i = 0; i < 100; i++) {
                Thread.sleep(10);
                result += i;
            }
            return result;
        });
        Thread computeThread = new Thread(futureTask);
        computeThread.start();
        Thread otherThread = new Thread(() -> {
            System.out.println("other task is running...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        otherThread.start();
        System.out.println(futureTask.get());
    }

    /**
     * wait(), notify(), notifyAll()
     */
    private synchronized void before1() {
        System.out.println("before1");
        notifyAll();
    }

    /**
     * wait(), notify(), notifyAll()
     */
    private synchronized void after1() {
        try {

            //wait与sleep区别: wait是Object的方法, sleep是Thread的静态方法; wait会释放锁, sleep不会
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("after1");
    }

    /**
     * await(), signal(), signalAll()
     */
    private void before2() {
        lock.lock();
        try {
            System.out.println("before2");
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * await(), signal(), signalAll()
     */
    private void after2() {
        lock.lock();
        try {

            //相比wait的等待方式, await可以指定等待的条件, 更加灵活
            //condition.await(1, TimeUnit.MILLISECONDS);
            condition.await();
            System.out.println("after2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 线程中调用另一个线程的join方法, 会将当前线程挂起, 而不是忙等待, 直到目标线程结束
     */
    private void testJoin() {
        A a = new A();
        B b = new B(a);
        b.start();
        a.start();
    }

    private class A extends Thread {
        @Override
        public void run() {
            System.out.println("A");
        }
    }

    private class B extends Thread {
        private A a;

        B(A a) {
            this.a = a;
        }

        @Override
        public void run() {
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("B");
        }
    }

}
