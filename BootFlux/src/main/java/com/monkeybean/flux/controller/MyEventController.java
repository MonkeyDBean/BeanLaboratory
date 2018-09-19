package com.monkeybean.flux.controller;

import com.monkeybean.flux.dao.MyEventRepository;
import com.monkeybean.flux.model.MyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * From: https://blog.csdn.net/get_set/article/details/79480233
 * <p>
 * Created by MonkeyBean on 2018/9/13.
 */
@RestController
@RequestMapping("/events")
public class MyEventController {
    private final MyEventRepository myEventRepository;

    @Autowired
    public MyEventController(MyEventRepository myEventRepository) {
        this.myEventRepository = myEventRepository;
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<Void> loadEvents(@RequestBody Flux<MyEvent> events) {
        return this.myEventRepository.insert(events).then();
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<MyEvent> getEvents() {
        return this.myEventRepository.findBy();
    }
}
