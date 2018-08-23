package com.monkeybean.flux.controller;

import com.monkeybean.flux.model.User;
import com.monkeybean.flux.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MonkeyBean on 2018/8/24.
 */
@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("user/add")
    public User save(@RequestParam String name) {
        User user = new User();
        user.setName(name);
        if (userRepository.save(user)) {
            System.out.println("用户对象：% 存储成功! \n" + user);
        }
        return user;
    }
}
