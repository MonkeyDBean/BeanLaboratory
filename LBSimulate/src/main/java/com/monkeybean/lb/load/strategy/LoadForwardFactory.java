package com.monkeybean.lb.load.strategy;

import com.monkeybean.lb.constant.RuleType;
import com.monkeybean.lb.load.strategy.impl.*;

/**
 * Created by MonkeyBean on 2019/8/2.
 */
public class LoadForwardFactory {

    /**
     * 创建负载策略(默认使用随机策略)
     *
     * @param rule 策略类型
     * @return 实例化的负载策略
     */
    public static LoadForward createLoadForward(RuleType rule) {
        LoadForward fg;
        switch (rule) {
            case WEIGHT_RANDOM:
                fg = new WeightRandomLoadForward(RuleType.WEIGHT_RANDOM);
                break;
            case ROUND:
                fg = new RoundLoadForward(RuleType.ROUND);
                break;
            case WEIGHT_ROUND:
                fg = new WeightRoundLoadForward(RuleType.WEIGHT_ROUND);
                break;
            case IP_HASH:
                fg = new IpHashLoadForward(RuleType.IP_HASH);
                break;
            case CONSISTENCY:
                fg = new ConsistencyLoadForward(RuleType.CONSISTENCY);
                break;
            case BEST_AVAILABLE:
                fg = new BestAvailableLoadForward(RuleType.BEST_AVAILABLE);
                break;
            default:
                fg = new RandomLoadForward(RuleType.RANDOM);
        }
        return fg;
    }

}
