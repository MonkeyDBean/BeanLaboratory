package com.monkeybean.algorithm.other;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 如果锁具备可重入性, 则称作为可重入锁, 如synchronized和ReentrantLock都是可重入锁(递归锁)
 * 在调用含有锁的函数时，若相邻的调用都在同一个线程中，就不会阻塞，若相邻的调用不再同一个线程中，就会使其中一个线程执行阻塞等待操作
 * <p>
 * 锁介绍参考：http://blog.itpub.net/31545684/viewspace-2375117/
 * <p>
 * Created by MonkeyBean on 2019/6/8.
 */
public class ReentrantLockTest {
    /**
     * 本例测试锁的重入性, 关于锁的其他特性介绍如下：
     * 关于公平锁: 尽量以请求锁的顺序来获取锁. 比如有多个线程在等待一个锁, 当这个锁被释放时, 等待时间最久的线程(最先请求的线程)会获得该锁, 这种就是公平锁.
     * 非公平锁无法保证锁的获取是按照请求锁的顺序, 这样可能导致某个或者某些线程永远获取不到锁.
     * <p>
     * synchronized为非公平锁
     * ReentrantLock和ReentrantReadWriteLock, 默认情况下是非公平锁, 但可以设置为公平锁;
     * 源码中NonfairSync及FairSync内部静态类, 分别用来实现非公平锁和公平锁
     * 创建ReentrantLock对象时, 通过构造函数传参fair=true设置锁的公平性: new ReentrantLock(true);
     * <p>
     * ReentrantLock定义了多种方法, 如：
     * isFair()：判断锁是否是公平锁
     * isLocked(): 判断锁是否被某个线程获取
     * isHeldByCurrentThread()：判断锁是否被当前线程获取了
     * hasQueuedThreads()：判断是否有线程在等待该锁
     * <p>
     * 大多数情况下, 非公平锁的性能高于公平锁的性能, 这是由于线程挂起和线程恢复时开销较大, 影响性能;
     * 考虑如下情况: A线程持有锁, B线程请求这个锁, 因此B线程被挂起: A线程释放这个锁时, B线程将被唤醒, 因此再次尝试获取锁;
     * 与此同时, C线程也请求获取这个锁, 那么C线程很可能在B线程被完全唤醒之前获得, 使用以及释放这个锁；
     * 这是种双赢的局面，B获取锁的时刻(B被唤醒后才能获取锁)并没有推迟, C更早地获取了锁, 并且吞吐量也获得了提高.
     * <p>
     * synchronized和ReentrantLock等独占锁就是悲观锁思想的实现
     * 1.悲观锁: 总是假设最坏的情况, 每次去拿数据的时候都认为别人会修改, 所以每次在拿数据的时候都会上锁, 这样别人想拿这个数据就会阻塞直到它拿到锁(共享资源每次只给一个线程使用, 其它线程阻塞, 用完后再把资源转让给其它线程);
     * 传统的关系型数据库里边就用到了很多这种锁机制, 比如行锁、表锁等, 读锁, 写锁等, 都是在做操作之前先上锁;
     * 2.乐观锁: 总是假设最好的情况, 每次去拿数据的时候都认为别人不会修改, 所以不会上锁, 但是在更新的时候会判断在此期间别人有没有去更新这个数据,可以使用版本号机制和CAS算法实现;
     * 乐观锁适用于多读的应用类型, 这样可以提高吞吐量; 像数据库提供的类似于write_condition机制, 其实都是提供的乐观锁; 在java.util.concurrent.atomic包下面的原子变量类就是使用了乐观锁的一种实现方式CAS来实现的.
     */
    private Lock reentrantLock = new ReentrantLock();
    private CustomReentrantLock customReentrantLock = new CustomReentrantLock();
    private CustomUnReentrantLock customUnReentrantLock = new CustomUnReentrantLock();

    public static void main(String[] args) {
        ReentrantLockTest testObject = new ReentrantLockTest();

        //synchronized可重入性
        testObject.method1Syn();

        //JDK ReentrantLock
        testObject.method1ReentrantLock();

        //Custom ReentrantLock
        testObject.method1CustomReentrantLock();

        //Custom UnReentrantLock：Dead Lock
        testObject.method1CustomUnReentrantLock();
    }

    /**
     * 如果synchronized不具备可重入性, 则会出现如下问题：
     * 线程A执行method1Syn方法前, 获取了这个对象的锁，开始执行, 而method2Syn也是synchronized方法, 那么线程A需要重新申请锁,
     * 但是这就会造成一个问题，因为线程A已经持有了该对象的锁，而又在申请获取该对象的锁，这样就会线程A一直等待永远不会获取到的锁
     */
    private synchronized void method1Syn() {
        System.out.println("111: call method1Syn");
        method2Syn();
    }

    private synchronized void method2Syn() {
        System.out.println("111: call method2Syn");
    }

    private void method1ReentrantLock() {
        reentrantLock.lock();
        System.out.println("222: call method1ReentrantLock");
        method2ReentrantLock();
        reentrantLock.unlock();
    }

    private void method2ReentrantLock() {
        reentrantLock.lock();
        System.out.println("222: call method2ReentrantLock");
        reentrantLock.unlock();
    }

    private void method1CustomReentrantLock() {
        customReentrantLock.lock();
        System.out.println("333: call method1CustomReentrantLock");
        method2CustomReentrantLock();
        customReentrantLock.unlock();
    }

    private void method2CustomReentrantLock() {
        customReentrantLock.lock();
        System.out.println("333: call method2CustomReentrantLock");
        customReentrantLock.unlock();
    }

    private void method1CustomUnReentrantLock() {
        customUnReentrantLock.lock();
        System.out.println("444: call method1CustomUnReentrantLock");
        method2CustomUnReentrantLock();
        customUnReentrantLock.unlock();
    }

    private void method2CustomUnReentrantLock() {
        customUnReentrantLock.lock();
        System.out.println("444: call method2CustomUnReentrantLock");
        customUnReentrantLock.unlock();
    }

    /**
     * 自定义可重入锁
     */
    private class CustomReentrantLock {
        private boolean isLocked = false;
        private Thread lockedBy = null;
        private int lockedCount = 0;

        public synchronized void lock() {
            Thread thread = Thread.currentThread();
            while (isLocked && lockedBy != thread) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isLocked = true;
            lockedCount++;
            lockedBy = thread;
        }

        public synchronized void unlock() {
            if (Thread.currentThread() == lockedBy) {
                lockedCount--;
            }
            if (lockedCount == 0) {
                isLocked = false;
                notify();
            }

        }
    }

    /**
     * 自定义不可重入锁
     */
    private class CustomUnReentrantLock {
        private boolean isLocked = false;

        public synchronized void lock() {
            while (isLocked) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isLocked = true;
        }

        public synchronized void unlock() {
            isLocked = false;
            notify();
        }
    }
}
