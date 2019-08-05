package com.monkeybean.lb.load;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.eas.AppService;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.load.strategy.LoadForwardFactory;
import com.monkeybean.lb.request.RequestInfo;
import com.monkeybean.lb.util.CommonUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Elastic Load Balancer
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public final class LoadBalancer {

    /**
     * 负载器
     */
    private static volatile LoadBalancer elb;

    /**
     * Map存储实例信息, key为实例Id, value为实例信息
     */
    private final Map<String, InstanceInfo> instanceMap = new ConcurrentHashMap<>();

    /**
     * 负载算法
     */
    private volatile LoadForward lf;

    private LoadBalancer() {
    }

    /**
     * 获取负载器实例
     */
    public static LoadBalancer getInstance() {
        if (elb == null) {
            synchronized (LoadBalancer.class) {
                if (elb == null) {
                    elb = new LoadBalancer();
                }
            }
        }
        return elb;
    }

    /**
     * 处理请求
     */
    public void process(RequestInfo request) {
        if (instanceMap.isEmpty()) {
            System.out.println("LoadBalancer process error, instanceMap is empty");
            return;
        }
        if (lf == null) {
            lf = LoadForwardFactory.createLoadForward(RuleType.RANDOM);
            System.out.println("LoadBalancer process, LoadForward isn't initialize, use random strategy");
        }
        List<InstanceInfo> instanceList = new ArrayList<>(instanceMap.values());
        instanceList.sort(Comparator.comparingInt(o -> Math.abs(o.getInstanceId().hashCode())));
        System.out.println("start to process request, origin request is: " + request.getOrigin());
        lf.handler(instanceList, request);
    }

    /**
     * 打印实例列表信息
     */
    public void printInstanceList() {
        System.out.println("load forward strategy is: " + getLfName());
        for (InstanceInfo instance : instanceMap.values()) {
            System.out.println("instanceId: " + instance.getInstanceId()
                    + ", ip: " + instance.getIp()
                    + ", heavy: " + instance.getHeavy()
                    + ", healthScore: " + instance.getHealthScore()
                    + ", counter: " + instance.getCounter());
        }
    }

    /**
     * 生成新应用服务
     *
     * @return 成功返回实例Id, 失败返回null
     */
    public String generateService() {
        final Random random = new Random();
        final double cpu = AppService.minInitCPU * 2 + random.nextInt(4) * 0.1;
        final double memory = AppService.minInitMemory * 2 + random.nextInt(2000) * 0.1;
        final int heavy = random.nextInt(10) + 1;
        final String instanceId = UUID.randomUUID().toString();

        //此处限制ip最多随机3次, 若均重复, 则不创建实例
        boolean ipSame;
        int loop = 0;
        String ip = "";
        while (loop < 3) {
            ip = CommonUtil.randomIp();
            ipSame = false;
            for (InstanceInfo instance : instanceMap.values()) {
                if (ip.equals(instance.getIp())) {
                    System.out.println("generateService, random the same ip: " + ip);
                    ipSame = true;
                    break;
                }
            }
            if (!ipSame) {
                break;
            }
            loop++;
        }
        if (loop < 3) {
            boolean createResult = createService(instanceId, ip, cpu, memory, heavy);
            System.out.println("generateService, instanceId: " + instanceId + ", ip: " + ip + ", createResult: " + createResult);
            return createResult ? instanceId : null;
        }
        System.out.println("create service failed in ip random process, instanceId: " + instanceId);
        return null;
    }

    /**
     * 新增应用服务
     *
     * @param instanceId 实例Id
     * @param initCPU    初始CPU
     * @param ip         节点ip
     * @param initMemory 初始内存
     * @param heavy      服务权重
     * @return 成功返回true, 失败返回false
     */
    public boolean createService(String instanceId, String ip, double initCPU, double initMemory, int heavy) {
        if (!AppService.checkAllocateResource(initCPU, initMemory)) {
            System.out.println("createService error, checkAllocateResource isn't pass, instanceId is: " + instanceId + ", initCPU: " + initCPU + ", initMemory: " + initMemory);
            return false;
        }
        AppService appService = new AppService(instanceId, initCPU, initMemory);
        return addInstanceToMap(instanceId, new InstanceInfo(instanceId, ip, heavy, appService));
    }

    /**
     * 随机下线一个应用服务
     */
    public void randomRemoveService() {
        final Random random = new Random();
        int index = random.nextInt(instanceMap.size());
        String instanceId = new ArrayList<>(instanceMap.keySet()).get(index);
        removeService(instanceId);
    }

    /**
     * 下线应用服务
     *
     * @param instanceId 实例Id
     * @return 成功返回true, 失败返回false
     */
    public boolean removeService(String instanceId) {
        InstanceInfo instanceInfo = instanceMap.get(instanceId);
        if (instanceInfo == null) {
            System.out.println("removeService error, instanceMap isn't contains the instance: " + instanceId);
            return false;
        }
        instanceInfo.destroy();
        return removeInstanceFromMap(instanceId);
    }

    /**
     * 添加实例到map
     *
     * @param instanceId   实例Id
     * @param instanceInfo 实例信息
     * @return 成功返回true, 失败返回false
     */
    private boolean addInstanceToMap(String instanceId, InstanceInfo instanceInfo) {
        if (!instanceMap.containsKey(instanceId)) {
            instanceMap.put(instanceId, instanceInfo);
            return true;
        }
        System.out.println("addInstanceToMap error, instanceId has existed before, instanceId is: " + instanceId);
        return false;
    }

    /**
     * 从map中移除实例
     *
     * @param instanceId 实例Id
     * @return 成功返回true, 失败返回false
     */
    private boolean removeInstanceFromMap(String instanceId) {
        if (instanceMap.containsKey(instanceId)) {
            instanceMap.remove(instanceId);
            return true;
        }
        System.out.println("removeInstanceFromMap error, instanceId isn't exist before, instanceId is: " + instanceId);
        return false;
    }

    /**
     * 获取当前负载策略名称
     */
    private String getLfName() {
        if (lf != null) {
            return lf.getLoadForwardType().name();
        }
        return "unset";
    }

    /**
     * 随机更换负载策略
     */
    public void randomChangeLf() {
        final Random random = new Random();
        RuleType[] types = RuleType.values();
        int index = random.nextInt(types.length);
        RuleType newRule = types[index];
        if (setLf(newRule.getCode())) {
            System.out.println("randomChangeLf, new Strategy is: " + newRule);
        }
    }

    /**
     * 设置负载策略
     *
     * @param code 策略标识
     * @return 成功返回true, 失败返回false
     */
    public boolean setLf(int code) {
        EnumSet<RuleType> rules = EnumSet.allOf(RuleType.class);
        for (RuleType rule : rules) {
            if (rule.getCode() == code) {
                lf = LoadForwardFactory.createLoadForward(rule);
                return true;
            }
        }
        System.out.println("LoadBalancer setLf error, code is: " + code);
        return false;
    }

    /**
     * 设置负载策略
     *
     * @param name 策略名称
     * @return 成功返回true, 失败返回false
     */
    public boolean setLf(String name) {
        if (name != null && !"".equals(name)) {
            String aimName = name.toUpperCase();
            for (RuleType rule : RuleType.values()) {
                if (rule.name().equals(aimName)) {
                    lf = LoadForwardFactory.createLoadForward(rule);
                    return true;
                }
            }
        }
        System.out.println("LoadBalancer setLf error, name is: " + name);
        return false;
    }

    /**
     * 获取存储实例信息的map
     */
    public Map<String, InstanceInfo> getInstanceMap() {
        return instanceMap;
    }
}

