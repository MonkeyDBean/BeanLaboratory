package com.monkeybean.labo.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by MonkeyBean on 2019/2/19.
 */
public class CoderTest {
    private static Logger logger = LoggerFactory.getLogger(CoderTest.class);

    @Test
    public void encodeMd5StrAndBase64() throws Exception {
        String origin = "a12345678";
        String result = Coder.encodeMd5StrAndBase64(origin);
        logger.info("encodeMd5StrAndBase64, origin: [{}], result: [{}]", origin, result);
    }

    @Test
    public void encodeMd5ByteAndBase64() throws Exception {
        String origin = "a12345678";
        String result = Coder.encodeMd5ByteAndBase64(origin);
        logger.info("encodeMd5ByteAndBase64, origin: [{}], result: [{}]", origin, result);
    }

    @Test
    public void encryptHmacSha256() throws Exception {
        String origin = "a12345678";
        String key = "testKey";
        String result = Coder.encryptHmacSha256(origin, key);
        logger.info("encryptHmacSha256, origin: [{}], key: [{}], result: [{}]", origin, key, result);
    }

    @Test
    public void unicodeToCn() {
        String origin = "\\u6d4b\\u8bd5\\u75\\u6e\\u69\\u63\\u6f\\u64\\u65\\u8f6c\\u7801";
        String result = Coder.unicodeToCn(origin);
        logger.info("unicodeToCn, origin: [{}], result: [{}]", origin, result);
    }

    @Test
    public void cnToUnicode() {
        String origin = "测试unicode转码";
        String result = Coder.cnToUnicode(origin);
        logger.info("cnToUnicode, origin: [{}], result: [{}]", origin, result);
    }

    @Test
    public void encodeSHA() throws Exception {
        String origin = "just test origin data";
        String encodeSHA1Str = Coder.encodeSHA1(origin);
        String encodeSHA1ByDigestStr = Coder.encodeSHA1ByDigestUtils(origin);
        String encodeSHA256Str = Coder.encodeSHA256(origin);
        String encodeSHA256ByDigestStr = Coder.encodeSHA256ByDigestUtils(origin);
        assertEquals(encodeSHA1Str, encodeSHA1ByDigestStr);
        assertEquals(encodeSHA256Str, encodeSHA256ByDigestStr);
        logger.info("encodeSHA, encodeSHA1Str: [{}], encodeSHA256Str: [{}]", encodeSHA1Str, encodeSHA256Str);
    }

}