package com.monkeybean.labo.util;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by MonkeyBean on 2018/7/19.
 */
public class IpUtilTest {

    @Before
    public void setUp() throws Exception {
        OkHttpUtil.initClient();
    }

    @Test
    public void getCityInfo() throws Exception {
        String ip = "1.119.195.242";
        String res = IpUtil.getCityInfo(ip);

        // 结果形式如：中国|华北|北京|北京市|阿里云
        System.out.println("getCityInfo result : " + res);
    }

}