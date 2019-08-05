package com.monkeybean.lb.load.strategy.impl;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 负载均衡: 加权随机
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class WeightRandomLoadForward implements LoadForward {

    private final RuleType ruleType;

    public WeightRandomLoadForward(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public RuleType getLoadForwardType() {
        return this.ruleType;
    }

    @Override
    public void handler(List<InstanceInfo> instanceList, RequestInfo request) {
        if (instanceList == null || instanceList.isEmpty()) {
            System.out.println("WeightRandomLoadForward handler failed, instanceList is null or empty");
            return;
        }
        List<Integer> heavySumList = new ArrayList<>();
        int heavySum = 0;
        for (InstanceInfo each : instanceList) {
            heavySum += each.getHeavy();
            heavySumList.add(heavySum);
        }
        final Random random = new Random();
        int randomPosition = random.nextInt(heavySum);
        for (int i = 0; i < heavySumList.size(); i++) {
            if (randomPosition < heavySumList.get(i)) {
                InstanceInfo instanceInfo = instanceList.get(i);
                instanceInfo.addToQueue(request);
                System.out.println("WeightRandomLoadForward, add request to instanceInfo queue, instanceId is: " + instanceInfo.getInstanceId() + ", origin request is: " + request.getOrigin());
                break;
            }
        }
    }
}
