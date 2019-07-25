package com.monkeybean.socket.core;

import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Created by MonkeyBean on 2019/5/6.
 */
public abstract class ExtendAbstractService<T> extends AbstractService<T> {

    /**
     * 查找未删除的记录, 用于表中有is_deleted字段的数据处理
     * 此方法低频使用, 实际多为联表查询, 在mapper.xml中手写sql更加便捷
     */
    public List<T> findNotDeletedByCondition(Condition condition) {
        if (condition.getOredCriteria().isEmpty()) {
            condition.createCriteria().andEqualTo("is_deleted", false);
        } else {
            condition.getOredCriteria().get(0).andEqualTo("is_deleted", false);
        }
        return mapper.selectByCondition(condition);
    }
}
