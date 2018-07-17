package com.monkeybean.labo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by MonkeyBean on 2018/06/02.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IdentityServiceTest {

    @Autowired
    private IdentityService identityService;

    @Value("${spring.mail.username}")
    private String mailSendFrom;

    @Test
    public void testSendMail() {
        String mailSendTo = mailSendFrom;
        String activeUrl = "https://www.google.com";
        identityService.sendMail(mailSendTo, activeUrl);
    }
}