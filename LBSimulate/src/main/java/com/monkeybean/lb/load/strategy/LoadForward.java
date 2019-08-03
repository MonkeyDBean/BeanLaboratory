package com.monkeybean.lb.load.strategy;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.InstanceInfo;
import com.monkeybean.lb.request.RequestInfo;

import java.util.List;

/**
 * 负载转发
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public interface LoadForward {

    /**
     * 获取负载策略类型
     */
    RuleType getLoadForwardType();

    /**
     * 通过负载均衡算法筛选出实例, 并将请求转发到目标实例(此处为模拟, 将请求添加到目标实例的处理队列)
     *
     * @param instanceList 实例列表
     * @param request      请求
     */
    void handler(List<InstanceInfo> instanceList, RequestInfo request);
}
