package com.monkeybean.algorithm.pattern.singleton;

/**
 * 双检锁/双重校验锁，线程安全且性能高
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class SingleObject3 {

    /**
     * 一、若不加volatile, 本例可能会执行多次5.1
     * 原因(以两个线程为例, 可能出现的场景)如下：
     * 1.Thread1进入1, 共享变量singleton值为空, 此时Thread1让出CPU资源给Thread2
     * 2.Thread2进入1, 共享变量singleton值仍为空, 此时Thread2让出CPU资源给Thread1
     * 3.Thread1会依次执行2, 3, 4, 5.1, 最终在Thread1里实例化了singleton; Thread1执行完毕让出CPU资源给Thread2
     * 4.Thread2接着1跑下去，跑到3的时候，由于在1里面拿到的singleton是空(没有及时拿到共享变量最新值)，所以Thread2仍然会执行4, 5.1
     * 5.最后在Thread1和Thread2都实例化了singleton
     * <p>
     * 加了volatile保证线程间共享变量的可见性
     * 某个线程修改共享变量后立即写入主存, 并通过缓存一致性协议通知各线程工作内存中的缓存变量无效(对应缓存行失效)
     * Thread2在执行3时, 重新从主存读取singleton的最新值, 而Thread1中已对singleton初始化, 故singleton不会再次初始化
     * <p>
     * <p>
     * 二、若不加volatile, 可能得到半成品的实例，原因如下
     * 4中，singleton = new SingleObject3() 并不是一个原子性指令, 该指令分为三部分
     * memory = allocate(); //first: 分配对象的内存空间
     * ctorInstance(memory); //second: 初始化对象
     * instance = memory; //third: 设置instance指向刚分配的内存地址
     * 若重排序可能变成：
     * memory = allocate(); //first: 分配对象的内存空间
     * instance = memory; //second: 设置instance指向刚分配的内存地址, 此时对象还没被初始化
     * ctorInstance(memory); //third: 初始化对象
     * Thread1若按上述重排序的指令执行完second: instance=memory, 此时没有初始化完该对象, Thread2获取singleton时不为空, 返回的对象是有问题的
     * <p>
     * 加了volatile保证有序性(禁止指令重排)
     */
    private static volatile SingleObject3 singleton;
    // private static SingleObject3 singleton;

    private SingleObject3() {
    }

    /**
     * 懒汉式加载(节省资源): 双重校验锁, 尽可能降低加锁对性能的影响
     */
    public static SingleObject3 getInstance() {

        //1
        if (singleton == null) {

            //2
            synchronized (SingleObject3.class) {

                //3
                if (singleton == null) {

                    //4
                    singleton = new SingleObject3();

                    //5.1
                    System.out.println(Thread.currentThread().getName() + ": singleton is initialized...");
                } else {

                    //5.2
                    System.out.println(Thread.currentThread().getName() + ": singleton is not null now...");
                }
            }
        }
        return singleton;
    }

    public static void main(final String[] args) throws InterruptedException {
        for (int i = 1; i <= 1000; i++) {
            final Thread t = new Thread(new ThreadSingleton());
            t.setName("thread" + i);
            t.start();
        }
    }

    /**
     * 实现Runnable接口, 调用单例
     */
    private static class ThreadSingleton implements Runnable {
        @Override
        public void run() {
            SingleObject3.getInstance();
        }
    }
}
