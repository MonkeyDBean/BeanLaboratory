package com.monkeybean.lb.load.strategy.impl;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.request.RequestInfo;

import java.util.List;

/**
 * 负载均衡: 源地址哈希
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class IpHashLoadForward implements LoadForward {

    private final RuleType ruleType;

    public IpHashLoadForward(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public RuleType getLoadForwardType() {
        return this.ruleType;
    }

    @Override
    public void handler(List<InstanceInfo> instanceList, RequestInfo request) {
        if (instanceList == null || instanceList.isEmpty()) {
            System.out.println("IpHashLoadForward handler failed, instanceList is null or empty");
            return;
        }
        InstanceInfo instanceInfo = instanceList.get(Math.abs(request.getIp().hashCode()) % instanceList.size());
        instanceInfo.addToQueue(request);
        System.out.println("IpHashLoadForward, add request to instanceInfo queue, instanceId is: " + instanceInfo.getInstanceId() + ", origin request is: " + request.getOrigin());
    }
}
