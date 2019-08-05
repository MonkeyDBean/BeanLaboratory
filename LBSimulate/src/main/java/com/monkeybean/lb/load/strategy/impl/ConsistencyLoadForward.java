package com.monkeybean.lb.load.strategy.impl;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.load.strategy.LoadForward;
import com.monkeybean.lb.request.RequestInfo;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 负载均衡: 一致性Hash(通过服务节点ip实现, 数据可分散存储, 某个机器挂掉仅需迁移此机器数据, 低成本的缩扩容)
 * <p>
 * 机制同Redis-Cluster数据存储
 * 此处不考虑Hash环的数据倾斜(通过虚拟节点解决: 每个服务器对应多个虚拟节点)
 * reference:
 * https://mp.weixin.qq.com/s/AYwarOGR7We1WO5u41onGQ
 * https://blog.csdn.net/suifeng629/article/details/81567777
 * <p>
 * Created by MonkeyBean on 2019/8/3.
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
        final TreeMap<Integer, InstanceInfo> instanceTreeMap = new TreeMap<>();
        for (InstanceInfo instanceInfo : instanceList) {
            int ipHash = Math.abs(instanceInfo.getIp().hashCode());
            instanceTreeMap.put(ipHash, instanceInfo);
        }
        int requestKeyHash = Math.abs(request.getKey().hashCode());
        SortedMap<Integer, InstanceInfo> subMap = instanceTreeMap.tailMap(requestKeyHash);

        //如果没有比requestKeyHash值大的节点，则选择第一个节点; 否则使用大于requestKeyHash的第一个节点(Hash环顺时针离requestKeyHash最近的那个节点)
        Integer aimKeyHashCode;
        if (subMap.isEmpty()) {
            aimKeyHashCode = instanceTreeMap.firstKey();
        } else {
            aimKeyHashCode = subMap.firstKey();
        }
        InstanceInfo aimInstance = instanceTreeMap.get(aimKeyHashCode);
        aimInstance.addToQueue(request);
        System.out.println("ConsistencyLoadForward, add request to instanceInfo queue, instanceId is: " + aimInstance.getInstanceId()
                + ", instance ip: " + aimInstance.getIp()
                + ", ip hash is: " + aimKeyHashCode
                + ", request key: " + request.getKey()
                + ", key hash is: " + requestKeyHash
                + ", origin request is: " + request.getOrigin());
    }
}
