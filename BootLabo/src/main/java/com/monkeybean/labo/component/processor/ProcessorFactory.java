package com.monkeybean.labo.component.processor;

import com.monkeybean.labo.util.OkHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 简单队列测试, 用于异步处理
 * <p>
 * Created by MonkeyBean on 2019/7/11.
 */
public class ProcessorFactory {
    /**
     * 阻塞队列
     */
    private static final BlockingQueue<ProcessUnit> blockQueue = new LinkedBlockingDeque<>();
    /**
     * 失败处理的重试最大次数
     */
    private static final int MAX_RETRY_TIME = 3;
    /**
     * 延时队列
     */
    private static final DelayQueue<ProcessDelayUnit> delayQueue = new DelayQueue<>();
    private static Logger logger = LoggerFactory.getLogger(ProcessorFactory.class);
    /**
     * 阻塞队列初始化状态
     */
    private static volatile boolean isBlockQueueInit = false;
    /**
     * 延时队列初始化状态
     */
    private static volatile boolean isDelayQueueInit = false;

    /**
     * 获取阻塞队列
     */
    public static BlockingQueue<ProcessUnit> getBlockQueue() {
        return blockQueue;
    }

    /**
     * 添加数据到阻塞队列
     */
    public static void addToBlockQueue(ProcessUnit unit) {
        blockQueue.add(unit);
    }

    /**
     * 开启阻塞队列线程, 消费数据
     */
    public static synchronized void startBlockQueueConsumer() {
        if (!isBlockQueueInit) {
            ExecutorService pool = Executors.newFixedThreadPool(1);
            pool.execute(() -> {
                while (true) {
                    try {
                        ProcessUnit unit = blockQueue.take();
                        doProcessUnit(unit);
                    } catch (InterruptedException e) {
                        logger.error("startBlockQueueConsumer, Thread InterruptedException: [{}]", e);
                        Thread.currentThread().interrupt();
                    }
                }
            });
            isBlockQueueInit = true;
        }
    }

    /**
     * 处理阻塞队列单元数据, 仅测试http请求处理
     */
    private static void doProcessUnit(ProcessUnit unit) {
        String url = unit.getCustom();
        String res = OkHttpUtil.doGet(url);
        logger.info("doProcessUnit, uid:[{}], initTime: [{}], url: [{}], response: [{}]", unit.getUid(), unit.getInitTimeStamp(), unit.getCustom(), res);
    }

    /**
     * 获延时队列
     */
    public static DelayQueue<ProcessDelayUnit> getDelayQueue() {
        return delayQueue;
    }

    /**
     * 添加数据到延时队列
     */
    public static void addToDelayQueue(ProcessDelayUnit unit) {
        delayQueue.add(unit);
    }

    /**
     * 开启延时队列线程, 消费数据
     */
    public static synchronized void startDelayQueueConsumer() {
        if (!isDelayQueueInit) {
            new Thread(() -> {
                while (true) {
                    try {
                        ProcessDelayUnit model = delayQueue.take();
                        doProcessDelayUnit(model);
                    } catch (InterruptedException e) {
                        logger.error("startDelayQueueConsumer, Thread InterruptedException: [{}]", e);
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
            isDelayQueueInit = true;
        }
    }

    /**
     * 处理延时队列单元数据, 仅测试http请求处理
     */
    private static void doProcessDelayUnit(ProcessDelayUnit unit) {
        String url = unit.getCustom();
        String res = OkHttpUtil.doGet(url);
        logger.info("doProcessDelayUnit, uid:[{}], initTime: [{}], url: [{}], retry: [{}], response: [{}]",
                unit.getUid(), unit.getInitTimeStamp(), unit.getCustom(), unit.getRetryNumber(), res);

        //失败重试
        if (res == null && unit.getRetryNumber() < MAX_RETRY_TIME && unit.getCustom() != null) {
            unit.setRetryNumber(unit.getRetryNumber() + 1);
            unit.setExpireTime(unit.getDelayTime() + System.currentTimeMillis());
            addToDelayQueue(unit);
        }
    }

}
