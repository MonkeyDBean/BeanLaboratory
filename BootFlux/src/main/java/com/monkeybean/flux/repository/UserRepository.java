package com.monkeybean.flux.repository;

import com.monkeybean.flux.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 存储用户信息
 * <p>
 * {@link User} {@link Repository}
 * Created by MonkeyBean on 2018/8/24.
 */
@Repository
public class UserRepository {

    /**
     * 存储用户信息，key为用户Id，value为用户
     */
    private final ConcurrentMap<Integer, User> repository = new ConcurrentHashMap<>();

    /**
     * id生成器
     */
    private final static AtomicInteger idGenerator = new AtomicInteger();

    /**
     * 存储用户信息
     *
     * @param user {@link User} 用户模型
     * @return 成功返回true, 失败返回false
     */
    public boolean save(User user) {
        int id = idGenerator.incrementAndGet();
        user.setId(id);
        return repository.put(id, user) == null;
    }

    /**
     * 获取所有用户信息
     */
    public Collection<User> findAll() {
        return repository.values();
    }

}
