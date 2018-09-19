package com.monkeybean.jpa.service.impl;

import com.monkeybean.jpa.dao.UserDao;
import com.monkeybean.jpa.entity.User;
import com.monkeybean.jpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
@Service("com.monkeybean.jpa.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> queryNotDeleteUser() {
        return userDao.findByIsDeleted(false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNewUser(User user) {
        userDao.save(user);
    }

}
