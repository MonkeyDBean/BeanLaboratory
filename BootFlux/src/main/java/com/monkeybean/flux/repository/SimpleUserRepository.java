package com.monkeybean.flux.repository;

import com.monkeybean.flux.model.SimpleUser;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 存储用户信息
 * <p>
 * {@link SimpleUser} {@link Repository}
 * Created by MonkeyBean on 2018/8/24.
 */
@Repository
public class SimpleUserRepository {

    /**
     * id生成器
     */
    private final static AtomicInteger idGenerator = new AtomicInteger();
    /**
     * 存储用户信息，key为用户Id，value为用户
     */
    private final ConcurrentMap<Integer, SimpleUser> cacheRepository = new ConcurrentHashMap<>();

    /**
     * 存储用户信息
     *
     * @param user {@link SimpleUser} 用户模型
     * @return 成功返回true, 失败返回false
     */
    public boolean save(SimpleUser user) {
        int id = idGenerator.incrementAndGet();
        user.setId(id);
        return cacheRepository.put(id, user) == null;
    }

    /**
     * 获取所有用户信息
     */
    public Collection<SimpleUser> findAll() {
        return cacheRepository.values();
    }

}
