package com.monkeybean.lb.load.strategy.impl;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.request.RequestInfo;
import com.monkeybean.lb.util.HashUtil;

import java.util.List;

/**
 * 负载均衡: 一致性哈希(此处将请求Key作为Hash分流)
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class ConsistencyLoadForward implements LoadForward {

    private final RuleType ruleType;

    public ConsistencyLoadForward(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public RuleType getLoadForwardType() {
        return this.ruleType;
    }

    @Override
    public void handler(List<InstanceInfo> instanceList, RequestInfo request) {
        if (instanceList == null || instanceList.isEmpty()) {
            System.out.println("ConsistencyLoadForward handler failed, instanceList is null or empty");
            return;
        }
        String keyHash = HashUtil.checkMd5(request.getKey());
        if (keyHash == null) {
            System.out.println("ConsistencyLoadForward handler failed, checkMd5 occurs exception, origin request is: " + request.getOrigin()
                    + ", request key is: " + request.getKey());
            return;
        }
        InstanceInfo instanceInfo = instanceList.get(Math.abs(keyHash.hashCode()) % instanceList.size());
        instanceInfo.addToQueue(request);
        System.out.println("ConsistencyLoadForward, add request to instanceInfo queue, instanceId is: " + instanceInfo.getInstanceId() + ", origin request is: " + request.getOrigin());
    }
}
