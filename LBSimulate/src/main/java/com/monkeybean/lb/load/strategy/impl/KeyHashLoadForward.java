package com.monkeybean.lb.load.strategy.impl;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.request.RequestInfo;

import java.util.List;

/**
 * 负载均衡: 请求Key作为Hash分流
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class KeyHashLoadForward implements LoadForward {

    private final RuleType ruleType;

    public KeyHashLoadForward(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public RuleType getLoadForwardType() {
        return this.ruleType;
    }

    @Override
    public void handler(List<InstanceInfo> instanceList, RequestInfo request) {
        if (instanceList == null || instanceList.isEmpty()) {
            System.out.println("KeyHashLoadForward handler failed, instanceList is null or empty");
            return;
        }
        InstanceInfo instanceInfo = instanceList.get(Math.abs(request.getKey().hashCode()) % instanceList.size());
        instanceInfo.addToQueue(request);
        System.out.println("KeyHashLoadForward, add request to instanceInfo queue, instanceId is: " + instanceInfo.getInstanceId()
                + ", request key: " + request.getKey()
                + ", origin request is: " + request.getOrigin());
    }
}
