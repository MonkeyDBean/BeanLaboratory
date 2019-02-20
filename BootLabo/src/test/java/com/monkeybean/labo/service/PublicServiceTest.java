package com.monkeybean.labo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by MonkeyBean on 2018/7/26.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PublicServiceTest {

    private static Logger logger = LoggerFactory.getLogger(PublicServiceTest.class);

    @Autowired
    private PublicService publicService;

    @Test
    public void testGetNowIds() {
        for (Integer accountId : publicService.getNowIds()) {
            logger.info("accountId is: {}", accountId);
        }
    }

}