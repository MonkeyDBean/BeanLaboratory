package com.monkeybean.lb.load.strategy.impl;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.request.RequestInfo;

import java.util.List;

/**
 * 负载均衡: 最佳可用(此处将服务性能及已处理连接作为入参筛选)
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class BestAvailableLoadForward implements LoadForward {

    private final RuleType ruleType;

    public BestAvailableLoadForward(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public RuleType getLoadForwardType() {
        return this.ruleType;
    }

    @Override
    public void handler(List<InstanceInfo> instanceList, RequestInfo request) {
        if (instanceList == null || instanceList.isEmpty()) {
            System.out.println("BestAvailableLoadForward handler failed, instanceList is null or empty");
            return;
        }
        int aimIndex = 0;
        int maxScore = 0;
        for (int i = 0; i < instanceList.size(); i++) {
            InstanceInfo instance = instanceList.get(i);
            int curScore = availableCalculate((int) instance.getHealthScore(), instance.getCounter());
            if (curScore > maxScore) {
                maxScore = curScore;
                aimIndex = i;
            }
        }
        InstanceInfo instanceInfo = instanceList.get(aimIndex);
        instanceInfo.addToQueue(request);
        System.out.println("BestAvailableLoadForward, add request to instanceInfo queue, instanceId is: " + instanceInfo.getInstanceId() + ", origin request is: " + request.getOrigin());
    }

    /**
     * 粗略模拟计算服务性能, 分数越大, 可用性越好
     *
     * @param performance 性能分数
     * @param counter     已处理连接数
     * @return 计算结果
     */
    private int availableCalculate(int performance, int counter) {
        return performance * 10000 + counter;
    }
}
