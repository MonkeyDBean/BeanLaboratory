package com.monkeybean.lb.load.strategy.impl;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.request.RequestInfo;

import java.util.List;

/**
 * 负载均衡: 轮询
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class RoundLoadForward implements LoadForward {

    private final RuleType ruleType;

    /**
     * 处理次数, 用于标记轮询位置
     */
    private Integer pos = 0;

    public RoundLoadForward(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public RuleType getLoadForwardType() {
        return this.ruleType;
    }

    @Override
    public void handler(List<InstanceInfo> instanceList, RequestInfo request) {
        if (instanceList == null || instanceList.isEmpty()) {
            System.out.println("RoundLoadForward handler failed, instanceList is null or empty");
            return;
        }
        int index;
        synchronized (RoundLoadForward.class) {
            index = this.pos % instanceList.size();
            this.pos++;
        }
        InstanceInfo instanceInfo = instanceList.get(index);
        instanceInfo.addToQueue(request);
        System.out.println("RoundLoadForward, add request to instanceInfo queue, instanceId is: " + instanceInfo.getInstanceId() + ", origin request is: " + request.getOrigin());
    }
}
