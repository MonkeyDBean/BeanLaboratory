package com.monkeybean.flux.dao;

import com.monkeybean.flux.model.MyEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

/**
 * Created by MonkeyBean on 2018/9/13.
 */
public interface MyEventRepository extends ReactiveMongoRepository<MyEvent, Long> {
    @Tailable
    Flux<MyEvent> findBy();

}
