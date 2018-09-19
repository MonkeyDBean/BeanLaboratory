package com.monkeybean.flux.controller;

import com.monkeybean.flux.model.User;
import com.monkeybean.flux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by MonkeyBean on 2018/9/13.
 */
@RestController
@RequestMapping("/user/repo")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    @ResponseBody
    public Mono<User> save(@RequestBody User user) {
        return this.userService.save(user);
    }

    @DeleteMapping("remove/{username}")
    public Mono<Long> deleteByUsername(@PathVariable String username) {
        return this.userService.deleteByUsername(username);
    }

    @GetMapping("/get/{username}")
    public Mono<User> findByUsername(@PathVariable String username) {
        return this.userService.findByUsername(username);
    }

    @GetMapping("/all")
    public Flux<User> findAll() {
        return this.userService.findAll().log();
    }
}
