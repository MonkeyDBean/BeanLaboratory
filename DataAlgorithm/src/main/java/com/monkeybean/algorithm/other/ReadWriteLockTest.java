package com.monkeybean.algorithm.other;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock实现ReadWriteLock接口而不是Lock接口
 * ReentrantLock是一种标准的互斥锁, 每次最多只有一个线程能持有锁; 而ReentrantReadWriteLock暴露了两个Lock对象(读锁和写锁), 其中一个用于读操作(共享锁), 而另外一个用于写操作(互斥锁);
 * 读写锁特性: 读与读不互斥, 读与写互斥, 写与写互斥
 * 使用读写锁的目的是提高系统性能, 非常适合资源的读操作远多于写操作的情况
 * <p>
 * 本例演示使用读写锁设计缓存系统
 * <p>
 * Created by MonkeyBean on 2019/6/8.
 */
public class ReadWriteLockTest {
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private Map<String, Object> cache = new HashMap<>();

    public static void main(String[] args) {
        ReadWriteLockTest testObject = new ReadWriteLockTest();
        String key = "test";
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    try {
                        Thread.sleep(random.nextInt(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    testObject.getData(key);
                }
            }).start();
        }
    }

    /**
     * 获取数据: 若缓存中有数据则从缓存读取, 若缓存无数据则从数据库获取并将数据写入缓存
     * <p>
     * 一个线程拥有了对象A的写锁, 在释放写锁前其他线程无法获得A的读锁、写锁, 因此其他线程此时无法读写;
     * 一个线程拥有了对象A的读锁, 在释放前其他线程可以获得A的读锁但无法获得A的写锁, 因此其他线程此时可以读不可以写;
     * 先有写锁可以获取读锁, 先有读锁无法获取写锁, 即锁可以降级不可以升级
     *
     * @param key 查询关键字
     */
    public Object getData(String key) {

        // 上读锁
        rwl.readLock().lock();

        // 从缓存中获取数据
        Object value = cache.get(key);

        // 释放读锁
        rwl.readLock().unlock();

        // 若缓存无此数据，则从数据库查询，并将查询结果写入缓存
        if (value == null) {
            try {

                // 上写锁(上写锁前保证当前线程无该对象的读锁：一个线程要想同时持有写锁和读锁, 必须先获取写锁再获取读锁, 写锁可以"降级"为读锁; 读锁不能"升级"为写锁)
                rwl.writeLock().lock();

                // 上读锁(已持有写锁, 此处无需加读锁, 若不加, 后续也无需释放该读锁; 写锁是互斥的, 当前线程拿到写锁后, 其他线程则挂起, 即当前线程拥有读写权限, 此处加读锁便于理解)
                rwl.readLock().lock();

                // 再次获取目标值, 判断是否为空, 防止写锁释放后，后面获得写锁的线程再次进行取值写入操作
                value = cache.get(key);
                if (value == null) {

                    //模拟DB取值操作
                    value = "testValue";
                    System.out.println("read data from database, ThreadId: " + Thread.currentThread().getId() + ", value: " + value);
                    cache.put(key, value);
                }
            } finally {

                // 缓存设值完成, 释放写锁
                rwl.writeLock().unlock();

                // 释放读锁
                rwl.readLock().unlock();
            }
        } else {
            System.out.println("read data from cache, ThreadId: " + Thread.currentThread().getId() + ", value: " + value);
        }
        return value;
    }
}
