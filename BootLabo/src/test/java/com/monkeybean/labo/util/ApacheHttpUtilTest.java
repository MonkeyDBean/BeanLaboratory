package com.monkeybean.labo.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by MonkeyBean on 2018/9/12.
 */
public class ApacheHttpUtilTest {
    private static Logger logger = LoggerFactory.getLogger(ApacheHttpUtilTest.class);

    /**
     * 本地起服后，测试多参数请求
     * 例：
     * url: http://127.0.0.1:8096/monkey/testtest/special?{"purpose":"justTest","dowhat":"nothing","heavey":100,"postField":"accountids,tempdata"}
     * Body form-data is：accountids=1234567,789456; tempdata=justtemp
     */
    @Test
    public void testApacheHttpAndSpecialParam() {
        String address = "http://127.0.0.1:8096/monkey/testtest/special";
        HashMap<String, Object> paramGet = new HashMap<>();
        paramGet.put("purpose", "justTest");
        paramGet.put("dowhat", "nothing");
        paramGet.put("heavey", 100);
        paramGet.put("postField", "accountids,tempdata");
        HashMap<String, Object> paramPost = new HashMap<String, Object>() {
            private static final long serialVersionUID = -7238581792286389468L;

            {
                put("accountids", "1234567,789456");
                put("tempdata", "justtemp");
            }
        };
        String result = ApacheHttpUtil.postRequest(paramGet, paramPost, address);
        logger.info("testSpecial result: {}", result);
    }

}