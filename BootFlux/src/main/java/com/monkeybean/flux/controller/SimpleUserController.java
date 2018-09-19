package com.monkeybean.flux.controller;

import com.monkeybean.flux.model.SimpleUser;
import com.monkeybean.flux.repository.SimpleUserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MonkeyBean on 2018/8/24.
 */
@RequestMapping(path = "user/simple")
@RestController
public class SimpleUserController {

    private final SimpleUserRepository simpleUserRepository;

    public SimpleUserController(SimpleUserRepository userRepository) {
        this.simpleUserRepository = userRepository;
    }

    @PostMapping("/add")
    public SimpleUser save(@RequestParam String name) {
        SimpleUser user = new SimpleUser();
        user.setName(name);
        if (simpleUserRepository.save(user)) {
            System.out.printf("用户对象：%s 存储成功!\n", user);
        }
        return user;
    }

}
