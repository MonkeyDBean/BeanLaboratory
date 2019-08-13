package com.monkeybean.dynamicds.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * 测试异步注解: @Async
 * 需在启动类加: @EnableAsync
 * Created by MonkeyBean on 2019/8/13.
 */
@Service
@Slf4j
public class TestAsyncService {

    public long synMethod1() throws InterruptedException {
        log.info("--------start-synMethod1------------");
        Thread.sleep(3000);
        log.info("--------end-synMethod1------------");
        return 1;
    }

    public long synMethod2() throws InterruptedException {
        log.info("--------start-synMethod2------------");
        Thread.sleep(2000);
        log.info("--------end-synMethod2------------");
        return 2;
    }

    public void synMethod3() throws InterruptedException {
        log.info("--------start-synMethod3------------");
        Thread.sleep(8000);
        log.info("--------end-synMethod3------------");
    }

    @Async
    public Future<Long> asynchronousMethod1() throws InterruptedException {
        log.info("--------start-asynchronousMethod1------------");
        Thread.sleep(3000);
        log.info("--------end-asynchronousMethod1------------");
        return new AsyncResult<>(1L);
    }

    @Async
    public Future<Long> asynchronousMethod2() throws InterruptedException {
        log.info("--------start-asynchronousMethod2------------");
        Thread.sleep(2000);
        log.info("--------end-asynchronousMethod2------------");
        return new AsyncResult<>(2L);
    }

    @Async
    public void asynchronousMethod3() throws InterruptedException {
        log.info("--------start-asynchronousMethod3------------");
        Thread.sleep(8000);
        log.info("--------end-asynchronousMethod3------------");
    }
}
