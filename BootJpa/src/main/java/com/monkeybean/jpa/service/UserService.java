package com.monkeybean.jpa.service;

import com.monkeybean.jpa.entity.User;

import java.util.List;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
public interface UserService {
    List<User> queryNotDeleteUser();

    void saveNewUser(User user);
}
