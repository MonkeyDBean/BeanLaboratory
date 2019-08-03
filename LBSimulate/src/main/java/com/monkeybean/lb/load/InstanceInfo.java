package com.monkeybean.lb.load;

import com.monkeybean.lb.constant.ServerStatus;
import com.monkeybean.lb.eas.AppService;
import com.monkeybean.lb.request.RequestInfo;

import java.util.concurrent.*;

/**
 * 实例信息
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class InstanceInfo {

    /**
     * 实例Id
     */
    private final String instanceId;
    /**
     * 消息处理队列
     */
    private final BlockingQueue<RequestInfo> processQueue;
    /**
     * 处理请求的计数器
     */
    private int counter = 0;
    /**
     * 权重
     */
    private int heavy = 1;
    /**
     * 健康分数
     */
    private double healthScore = 100.0;
    /**
     * 心跳执行器
     */
    private ScheduledExecutorService heartExecutor;
    /**
     * 消息处理线程
     */
    private Thread messageThread;
    /**
     * 存储实例服务的引用(模拟服务节点地址)
     */
    private AppService appService;

    public InstanceInfo(String instanceId, AppService appService) {
        this.instanceId = instanceId;
        this.appService = appService;
        this.appService.attachObserver(this);
        this.processQueue = new LinkedBlockingDeque<>();
        this.init();
    }

    public InstanceInfo(String instanceId, int heavy, AppService appService) {
        this(instanceId, appService);
        this.heavy = heavy;
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public int getCounter() {
        return this.counter;
    }

    public int getHeavy() {
        return this.heavy;
    }

    public double getHealthScore() {
        return this.healthScore;
    }

    public void updateHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    /**
     * 请求添加至处理队列, 同时计数器加1
     */
    public void addToQueue(RequestInfo request) {
        this.processQueue.add(request);
        this.counter++;
    }

    /**
     * 启动请求处理队列
     */
    private void init() {
        this.heartExecutor = Executors.newSingleThreadScheduledExecutor();
        this.heartExecutor.scheduleAtFixedRate(this::heartBeat, 200, 1000, TimeUnit.MILLISECONDS);
        messageThread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("instanceInfo processQueue, thread has interrupted");
                    return;
                }
                try {
                    RequestInfo request = this.processQueue.take();
                    this.appService.getProcessQueue().add(request);
                    System.out.println("request forward, from instanceInfo to appService, instanceId is: " + this.instanceId + ", counter is: " + this.counter + ", origin request is" + request.getOrigin());
                } catch (InterruptedException e) {
                    System.out.println("take request from processQueue in instanceInfo, interruptedException, instanceId: " + this.instanceId);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        messageThread.start();
        System.out.println("instanceInfo init finish, instanceId: " + this.instanceId);
    }

    /**
     * 释放资源
     */
    public void destroy() {
        this.heartExecutor.shutdown();
        messageThread.interrupt();
        this.appService.destroy();
        System.out.println("InstanceInfo destroy finish, instanceId: " + this.instanceId);
    }

    /**
     * 模拟心跳
     */
    private void heartBeat() {
        //System.out.println("heartbeat execute between instanceInfo and appService, instanceId: " + this.instanceId + ", curTime is: " + System.currentTimeMillis());
        if (this.appService.getStatus() != ServerStatus.RUNNING) {
            boolean removeResult = LoadBalancer.getInstance().removeService(this.instanceId);
            System.out.println("heartBeat check, appService status is not running, remove from LoadBalancer, instanceId: " + this.instanceId + ", removeResult" + removeResult);
        }
    }

}
