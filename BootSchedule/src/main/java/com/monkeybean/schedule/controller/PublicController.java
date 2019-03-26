package com.monkeybean.schedule.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MonkeyBean on 2019/3/26.
 */
@RequestMapping(path = "public/util")
@RestController
public class PublicController {
    private static Logger logger = LoggerFactory.getLogger(PublicController.class);

    @Autowired
    public PublicController() {
    }

    @GetMapping(path = "sniff/status")
    public String sniffStatus() {
        logger.debug("sniff test ok, now time: {}", System.currentTimeMillis());
        return "SUCCESS";
    }

}
