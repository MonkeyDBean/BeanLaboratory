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
                fg = new WeightRandomLoadForward(rule);
                break;
            case ROUND:
                fg = new RoundLoadForward(rule);
                break;
            case WEIGHT_ROUND:
                fg = new WeightRoundLoadForward(rule);
                break;
            case IP_HASH:
                fg = new IpHashLoadForward(rule);
                break;
            case KEY_HASH:
                fg = new KeyHashLoadForward(rule);
                break;
            case BEST_AVAILABLE:
                fg = new BestAvailableLoadForward(rule);
                break;
            case CONSISTENCY_HASH:
                fg = new ConsistencyLoadForward(rule);
                break;
            default:
                fg = new RandomLoadForward(RuleType.RANDOM);
        }
        return fg;
    }

}
