package com.monkeybean.lb.load.strategy.impl;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.request.RequestInfo;

import java.util.List;
import java.util.Random;

/**
 * 负载均衡: 随机
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class RandomLoadForward implements LoadForward {

    private final RuleType ruleType;

    private final Random random = new Random(System.currentTimeMillis());

    public RandomLoadForward(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public RuleType getLoadForwardType() {
        return this.ruleType;
    }

    @Override
    public void handler(List<InstanceInfo> instanceList, RequestInfo request) {
        if (instanceList == null || instanceList.isEmpty()) {
            System.out.println("RandomLoadForward handler failed, instanceList is null or empty");
            return;
        }
        InstanceInfo instanceInfo = instanceList.get(this.random.nextInt(instanceList.size()));
        instanceInfo.addToQueue(request);
        System.out.println("RandomLoadForward, add request to instanceInfo queue, instanceId is: " + instanceInfo.getInstanceId() + ", origin request is: " + request.getOrigin());
    }
}
