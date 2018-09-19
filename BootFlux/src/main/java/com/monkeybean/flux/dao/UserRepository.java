package com.monkeybean.flux.dao;

import com.monkeybean.flux.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Created by MonkeyBean on 2018/9/13.
 */
public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUsername(String username);

    Mono<Long> deleteByUsername(String username);
}
