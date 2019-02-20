package com.monkeybean.labo.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by MonkeyBean on 2019/2/19.
 */
public class CoderTest {
    private static Logger logger = LoggerFactory.getLogger(CoderTest.class);

    @Test
    public void encodeMd5StrAndBase64() throws Exception {
        String origin = "a12345678";
        String result = Coder.encodeMd5StrAndBase64(origin);
        logger.info("encodeMd5StrAndBase64, origin: {}, result: {}", origin, result);
    }

    @Test
    public void encodeMd5ByteAndBase64() throws Exception {
        String origin = "a12345678";
        String result = Coder.encodeMd5ByteAndBase64(origin);
        logger.info("encodeMd5ByteAndBase64, origin: {}, result: {}", origin, result);
    }

}