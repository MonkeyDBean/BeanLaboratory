package com.monkeybean.algorithm.concurrent;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 阻塞队列模拟生产者消费者
 * <p>
 * 两种阻塞队列:
 * FIFO队列: LinkedBlockingQueue, ArrayBlockingQueue(固定长度)
 * 优先级队列: PriorityBlockingQueue
 * <p>
 * Created by MonkeyBean on 2019/10/15.
 */
public class ProducerConsumer {
    /**
     * 如果队列为空, take()将阻塞, 直到队列中有内容; 如果队列为满, put()将阻塞, 直到队列有空闲位置
     */
    private static BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            Producer producer = new Producer();
            producer.start();
        }
        for (int i = 0; i < 5; i++) {
            Consumer consumer = new Consumer();
            consumer.start();
        }
        for (int i = 0; i < 3; i++) {
            Producer producer = new Producer();
            producer.start();
        }
    }

    private static class Producer extends Thread {
        @Override
        public void run() {
            try {
                String seq = "product" + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID();
                queue.put(seq);
                System.out.println("produce one, seq: " + seq + ", the count of product in queue is: " + queue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Consumer extends Thread {

        @Override
        public void run() {
            try {
                String product = queue.take();
                System.out.println("consume one, seq is: " + product + ", the count of product in queue is: " + queue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
