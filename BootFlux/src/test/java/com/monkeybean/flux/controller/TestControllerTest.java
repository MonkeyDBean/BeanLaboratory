package com.monkeybean.flux.controller;

import com.monkeybean.flux.model.MyEvent;
import com.monkeybean.flux.model.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Created by MonkeyBean on 2018/9/13.
 */
public class TestControllerTest {

    //    @Test
    public void helloAfterTest() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");
        Mono<String> resp = webClient
                .get().uri("/test/hello/after")
                .retrieve()
                .bodyToMono(String.class);
        resp.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(1);
    }

    //    @Test
    public void userInfoTest() throws InterruptedException {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        webClient.get().uri("/user/repo/all")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(User.class))
                .doOnNext(System.out::println)
                .blockLast();
    }

    //    @Test
    public void timesTest() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient.get().uri("/times")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .log()
                .take(10)
                .blockLast();
    }

    @Test
    public void testPostEvents() {
        Flux<MyEvent> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(l -> new MyEvent(System.currentTimeMillis(), "message-" + l)).take(5);
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(eventFlux, MyEvent.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    //test get event: curl http://localhost:8080/events

}