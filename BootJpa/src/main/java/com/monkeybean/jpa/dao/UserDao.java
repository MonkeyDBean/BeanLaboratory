package com.monkeybean.jpa.dao;

import com.monkeybean.jpa.entity.User;

import java.util.List;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
@SuppressWarnings("all")
public interface UserDao extends BaseDao<User, Integer> {
    List<User> findByIsDeleted(boolean isDeleted);
}
