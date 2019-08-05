package com.monkeybean.lb.constant;

/**
 * 负载均衡策略类型
 * <p>
 * Created by MonkeyBean on 2019/8/2.
 */
public enum RuleType {
    /**
     * 随机
     */
    RANDOM(1),

    /**
     * 加权随机
     */
    WEIGHT_RANDOM(2),

    /**
     * 轮询
     */
    ROUND(3),

    /**
     * 加权轮询
     */
    WEIGHT_ROUND(4),

    /**
     * 源地址哈希
     */
    IP_HASH(5),

    /**
     * Key哈希
     */
    KEY_HASH(6),

    /**
     * 最佳可用策略
     */
    BEST_AVAILABLE(7),

    /**
     * 一致性Hash
     */
    CONSISTENCY_HASH(8);

    /**
     * 标识
     */
    private int code;

    RuleType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
