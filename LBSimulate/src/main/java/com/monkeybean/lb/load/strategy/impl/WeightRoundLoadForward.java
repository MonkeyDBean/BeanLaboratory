package com.monkeybean.lb.load.strategy.impl;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 负载均衡: 加权轮询
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public class WeightRoundLoadForward implements LoadForward {

    private final RuleType ruleType;

    /**
     * 处理次数, 用于标记轮询位置
     */
    private Integer pos = 0;

    public WeightRoundLoadForward(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public RuleType getLoadForwardType() {
        return this.ruleType;
    }

    @Override
    public void handler(List<InstanceInfo> instanceList, RequestInfo request) {
        if (instanceList == null || instanceList.isEmpty()) {
            System.out.println("WeightRoundLoadForward handler failed, instanceList is null or empty");
            return;
        }
        List<InstanceInfo> tempList = new ArrayList<>();
        for (InstanceInfo each : instanceList) {
            for (int i = 0; i < each.getHeavy(); i++) {
                tempList.add(each);
            }
        }
        int index;
        synchronized (WeightRoundLoadForward.class) {
            index = this.pos % tempList.size();
            this.pos++;
        }
        InstanceInfo instanceInfo = tempList.get(index);
        instanceInfo.addToQueue(request);
        System.out.println("WeightRoundLoadForward, add request to instanceInfo queue, instanceId is: " + instanceInfo.getInstanceId() + ", origin request is: " + request.getOrigin());
    }
}
