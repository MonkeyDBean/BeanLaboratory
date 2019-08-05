package com.monkeybean.lb.eas;

import com.monkeybean.lb.constant.ServerStatus;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.request.RequestInfo;

import java.util.concurrent.*;

/**
 * Created by MonkeyBean on 2019/8/2.
 */
public class AppService {

    /**
     * 模拟服务运行所需最小CPU
     */
    public static final double minInitCPU = 0.05;

    /**
     * 模拟服务运行所需最小内存
     */
    public static final double minInitMemory = 50.0;

    /**
     * 模拟服务CPU变动
     */
    private static final double modifyCPU = 0.001;

    /**
     * 模拟服务内存变动
     */
    private static final double modifyMemory = 0.5;

    /**
     * 模拟剩余CPU报警值
     */
    private static final double alarmCPU = 0.02;

    /**
     * 模拟剩余内存报警值
     */
    private static final double alarmMemory = 20.0;

    /**
     * 实例Id
     */
    private final String instanceId;

    /**
     * 初始分配CPU
     */
    private final double initCPU;

    /**
     * 初始分配内存
     */
    private final double initMemory;
    /**
     * 消息处理队列
     */
    private final BlockingQueue<RequestInfo> processQueue;
    /**
     * 已使用CPU
     */
    private double usedCPU;
    /**
     * 已使用内存
     */
    private double usedMemory;
    /**
     * 可用CPU
     */
    private double availableCPU;
    /**
     * 可用内存
     */
    private double availableMemory;
    /**
     * 当前服务状态
     */
    private ServerStatus status = ServerStatus.RUNNING;
    /**
     * 资源校验执行器
     */
    private ScheduledExecutorService resourceExecutor;
    /**
     * 消息处理线程
     */
    private Thread messageThread;
    /**
     * 监控当前对象的观察者实例
     */
    private InstanceInfo observerInstance;

    public AppService(String instanceId, double initCPU, double initMemory) {
        this.instanceId = instanceId;
        this.initCPU = initCPU;
        this.initMemory = initMemory;

        //模拟起服后的资源使用
        this.usedCPU = AppService.minInitCPU;
        this.usedMemory = AppService.minInitMemory;

        //剩余资源计算
        this.availableResource();

        //初始化处理队列
        this.processQueue = new LinkedBlockingDeque<>();

        this.init();
    }

    /**
     * 校验分配资源是否达标
     *
     * @param cpu    给定cpu
     * @param memory 给定内存
     * @return 成功返回true, 失败返回false
     */
    public static boolean checkAllocateResource(double cpu, double memory) {
        return cpu > AppService.minInitCPU && memory > AppService.minInitMemory;
    }

    /**
     * 获取服务状态
     */
    public ServerStatus getStatus() {
        return this.status;
    }

    /**
     * 获取处理队列
     */
    public BlockingQueue<RequestInfo> getProcessQueue() {
        return this.processQueue;
    }

    /**
     * 启动消息处理队列
     */
    private void init() {
        this.resourceExecutor = Executors.newSingleThreadScheduledExecutor();
        this.resourceExecutor.scheduleAtFixedRate(this::resourceCheck, 2000, 2000, TimeUnit.MILLISECONDS);
        messageThread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("appService processQueue, thread has interrupted");
                    return;
                }
                try {

                    //模拟请求处理
                    RequestInfo request = this.processQueue.take();
                    System.out.println("request process successful in appService, instanceId: " + this.instanceId
                            + ", request origin: " + request.getOrigin()
                            + ", request ip: " + request.getIp()
                            + ", request key: " + request.getKey()
                            + ", curTime: " + System.currentTimeMillis());

                    //模拟资源使用(仅增多, 不释放)
                    this.usedCPU += AppService.modifyCPU;
                    this.usedMemory += AppService.modifyMemory;
                    this.availableResource();
                } catch (InterruptedException e) {
                    System.out.println("take request from processQueue in appService, interruptedException, instanceId: " + this.instanceId);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        messageThread.start();
        System.out.println("appService init finish, instanceId: " + this.instanceId);
    }

    /**
     * 销毁当前服务, 释放资源
     */
    public void destroy() {
        this.resourceExecutor.shutdown();
        this.setStatusDown();
        messageThread.interrupt();
        this.observerInstance = null;
        System.out.println("AppService destroy finish, instanceId: " + this.instanceId);
    }

    /**
     * 计算剩余资源
     */
    private void availableResource() {
        this.availableCPU = this.initCPU - this.usedCPU;
        this.availableMemory = this.initMemory - this.usedMemory;
    }

    /**
     * 获取资源状态, 计算健康分数, 通知对应InstanceInfo
     */
    private void resourceCheck() {

        //资源过载, 将服务状态设置为不可用
        if (this.availableCPU <= 0 || this.availableMemory <= 0) {
            this.setStatusDown();
            return;
        }

        //资源过低则报警
        if (this.availableCPU <= AppService.alarmCPU || this.availableMemory <= AppService.alarmMemory) {
            System.out.println("AppService resource will be overload, instanceId: " + this.instanceId + ", availableCPU: " + this.availableCPU + ", availableMemory" + this.availableMemory);
        }

        //健康状态通知
        this.notifyObserver(calculateHealthScore());
    }

    /**
     * 模拟计算服务健康分数
     */
    private double calculateHealthScore() {
        double cpuHealthScore = (this.availableCPU + AppService.minInitCPU) / this.initCPU;
        double memoryHealthScore = (this.availableMemory + AppService.minInitMemory) / this.initMemory;
        return (cpuHealthScore + memoryHealthScore) * 100 / 2;
    }

    /**
     * 模拟服务状态变动
     */
    private void setStatusDown() {
        this.status = ServerStatus.DISCONNECT;
    }

    /**
     * 与观察者实例关联
     */
    public void attachObserver(InstanceInfo instanceInfo) {
        this.observerInstance = instanceInfo;
    }

    /**
     * 通知观察者当前服务的健康状态
     */
    private void notifyObserver(double healthScore) {
        if (this.observerInstance != null) {
            this.observerInstance.updateHealthScore(healthScore);
        }
    }

}
