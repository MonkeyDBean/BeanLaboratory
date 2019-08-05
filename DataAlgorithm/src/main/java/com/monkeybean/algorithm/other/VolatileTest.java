package com.monkeybean.algorithm.other;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * volatile关键字修饰成员变量, 用于线程同步, 保证可见性和有序性(禁止指令重排), 不保证原子性
 * 可见性：
 * 1.volatile关键字会强制将修改的值立即写入主存
 * 2.当某个线程进行共享变量修改时, 会导致所有线程工作内存中的缓存变量无效(反映到硬件层, 是CPU的L1或者L2缓存中对应的缓存行无效)
 * 3.某个线程由于工作内存中的缓存变量无效，所以该线程再次读取变量值时会去主存读取
 * <p>
 * 有序性：禁止指令重排
 * 关于指令重排: JVM为了提高执行效率会将指令进行重新排序，但是这种重新排序不会对单线程程序产生影响
 * JVM如何保证单线程下的指令在重新排序后执行结果不受影响, 需了解happens-before机制
 * 对于多线程, 某些情况下的指令重排会对结果造成影响
 * <p>
 * happens-before：
 * 1.如果一个操作happens-before另一个操作，那么第一个操作的执行结果将对第二个操作可见，而且第一个操作的执行顺序排在第二个操作之前。
 * 2.两个操作之间存在happens-before关系，并不意味着Java平台的具体实现必须要按照happens-before关系指定的顺序来执行。如果重排序之后的执行结果，与按happens-before关系来执行的结果一致，那么这种重排序并不非法，编译器和处理器怎么优化都行。
 * 规则：
 * 1.程序次序规则：一个线程内，按照代码顺序，书写在前面的操作先行发生于书写在后面的操作，前提是后一个指令依赖前一个指令的执行结果
 * 2.锁定规则：一个unLock操作先行发生于后面对同一个锁的lock操作
 * 3.volatile变量规则：对一个变量的写操作先行发生于后面对这个变量的读操作
 * 4.传递规则：如果操作A先行发生于操作B，而操作B又先行发生于操作C，则可以得出操作A先行发生于操作C
 * 4.线程启动规则：Thread对象的start()方法先行发生于此线程的每个一个动作
 * 5.线程中断规则：对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生
 * 6.线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行
 * 7.对象终结规则：一个对象的初始化完成先行发生于他的finalize()方法的开始
 * <p>
 * 参考：https://www.cnblogs.com/dolphin0520/p/3920373.html
 * <p>
 * Created by MonkeyBean on 2019/6/8.
 */
public class VolatileTest {
    private static final int THREAD_NUM = 10;
    private static CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
    private volatile int incVolatile = 0;
    private int incSyn = 0;
    private int incLock = 0;
    private Lock lock = new ReentrantLock();

    /**
     * 循环CAS(compare and swap)方式, 比对当前值与期望值, 若相等则设为新值
     * 另外，jdk1.5之后, atomic包提供AtomicStampedReference解决ABA问题, 此类内部不仅维护对象值, 而且维护了一个时间戳(可以是任意的一个整数来表示状态值),
     * 当设置对象值时, 对象值和状态值都必须满足期望值才会写入成功; 因此即使对象被反复读写, 写回原值, 只要状态值发生变化, 就能防止不恰当的写入.
     * <p>
     * 关于ABA问题(CAS虽然能高效的解决原子问题，但是CAS也会带来1个经典问题即ABA问题):
     * 因为CAS需要在操作值的时候首先检查下值是否发生变化, 如果没有发生变化则更新, 但是如果一个值原来是A, 变成了B, 又变成了A, 那么使用CAS进行检查时会发现它的值没有发生变化, 但是实际上却变化了;
     * ABA问题的解决思路就是使用版本号. 在变量前面追加上版本号，每次变量更新的时候把版本号加一，那么A－B－A就会变成1A-2B－3A。
     * <p>
     * 关于自旋锁与互斥锁
     * 1.自旋锁与互斥锁都是为了实现保护资源共享的机制
     * 2.无论是自旋锁还是互斥锁, 在任意时刻, 都最多只能有一个保持者
     * 3.获取互斥锁的线程, 如果锁已经被占用, 则该线程将进入睡眠状态; 获取自旋锁的线程则不会睡眠, 而是一直循环等待锁释放(线程的状态不会改变, 线程一直是用户态并且是活跃的)
     * 4.自旋锁如果持有锁的时间太长，则会导致其它等待获取锁的线程耗尽CPU
     * 5.自旋锁本身无法保证公平性(会有线程饥饿的问题)，同时也无法保证可重入性; 基于自旋锁，可以实现具备公平性和可重入性质的锁
     */
    private AtomicInteger incAtomic = new AtomicInteger(0);

    /**
     * 测试自增(包含三个操作: 读取, 计算, 写入)
     * volatile不可保证原子性
     * AtomicInteger并发策略使用CAS(Compare And Swap)算法, 保证原子性
     * 采用synchronized或lock保证原子性
     */
    private static void testIncrease() throws InterruptedException {
        System.out.println("testIncrease start");
        final VolatileTest test = new VolatileTest();
        Random random = new Random();
        for (int i = 0; i < THREAD_NUM; i++) {
            int threadFlag = i;
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {

                    //非原子性自增
                    test.increaseNormal();

                    //原子性的自增,通过循环CAS方式
                    test.incAtomic.incrementAndGet();

                    //synchronized
                    test.increaseSyn();

                    //lock
                    test.increaseLock();

                    System.out.println("Thread" + threadFlag + ", incVolatile: " + test.incVolatile + ", incAtomic: " + test.incAtomic + ", incSyn: " + test.incSyn + ", incLock: " + test.incLock);
                    try {
                        Thread.sleep(random.nextInt(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
                countDownLatch.countDown();
            }).start();
        }

        //等待所有线程执行完毕
        countDownLatch.await();
        System.out.println("final result, incVolatile: " + test.incVolatile + ", incAtomic: " + test.incAtomic + ", incSyn: " + test.incSyn + ", incLock: " + test.incLock);
    }

    public static void main(String[] args) throws InterruptedException {
        testIncrease();
    }

    private void increaseNormal() {
        incVolatile++;
    }

    /**
     * synchronized保证cpu分配的时间片里线程独占, 线程调度有序执行
     */
    private synchronized void increaseSyn() {
        incSyn++;
    }

    /**
     * 获取lock的线程继续执行, 其他线程挂起, 等待lock的释放
     * 锁具有排他性(即一个锁一次只能被一个线程持有)
     * synchronized及Lock均为互斥锁(排他锁)
     */
    private void increaseLock() {
        lock.lock();
        try {
            incLock++;
        } finally {
            lock.unlock();
        }
    }
}
