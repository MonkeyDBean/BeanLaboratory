package com.monkeybean.algorithm.other;

/**
 * 简单死锁测试
 * Created by MonkeyBean on 2019/3/7.
 */
public class SimpleDeadLockTest {
    private static String resourceA = "A";
    private static String resourceB = "B";

    public static void main(String[] args) {
        deadLock();
    }

    /**
     * 死锁指多进程(线程)因资源竞争而造成互相等待
     * <p>
     * 产生死锁的四个条件(缺一不可)：
     * 1.互斥：某个资源同一时间只能被一个进程使用，若此时其他进程请求改资源，只能等待资源释放
     * 2.请求与保持：进程已保持了至少一个资源，同时有发起新的资源请求，而该资源已被其他进程占有，此时请求进程被阻塞，但不释放已有的资源
     * 3.不可剥夺：进程锁所获得的资源在未使用完毕之前,不能被其他进程强行剥夺，即只能由拥有该资源的进程主动释放
     * 4.循环等待：若干进程形成首尾相接、循环等待资源的关系
     * <p>
     * 死锁避免的原则：空闲让进，忙着等待，有限等待
     * 死锁的避免：系统对进程发出的每一个系统可满足的资源申请进行动态检查，根据检查结果决定是否分配资源。如果分配后系统可能产生死锁，则不予分配，这是保证系统不进入死锁状态的动态策略
     * 若系统可保证所有进程在有限时间内可以得到所需全部资源，则系统处于安全状态
     * <p>
     * 死锁预防：
     * 1.破坏"不可剥夺"条件：若某个进程不能获取所有所需资源则处于等待状态，等待期间，该进程所占用的资源被隐式释放，重新加入到系统资源列表，可被其他进程使用，而等待的进程只有重新获取自己原有的资源以及新申请的资源才可以重新启动执行
     * 2.破坏"请求与保持"条件：上述破坏不可剥夺条件的方法为静态分配，每个进程在开始执行时即申请到所需的全部资源；还有一种为动态分配，即每个进程再申请资源时，本身不占有资源
     * 3.破坏"循环等待"条件: 采用资源有序分配的策略，即将系统中的所有资源编号，将紧缺、稀有的资源采用较大的编号，在申请资源时必须按编号的顺序执行，一个进程只有获得较小编号的资源才能申请较大编号的资源
     */
    private static void deadLock() {
        Thread threadA = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadA get resourceA");
                try {
                    Thread.sleep(3000);
                    synchronized (resourceB) {
                        System.out.println("threadA get resourceB");
                    }
                    System.out.println("threadA run normal");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        Thread threadB = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println("threadB get resourceB");
                synchronized (resourceA) {
                    System.out.println("threadB get resourceA");
                }
                System.out.println("threadB run normal");
            }
        });
        threadA.start();
        threadB.start();
    }
}
