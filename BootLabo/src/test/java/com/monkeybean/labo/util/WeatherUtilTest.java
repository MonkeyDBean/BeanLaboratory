package com.monkeybean.labo.util;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by MonkeyBean on 2018/7/19.
 */
public class WeatherUtilTest {

    @Before
    public void setUp() throws Exception {
        OkHttpUtil.initClient();
    }

    @Test
    public void useSoJson() throws Exception {
        String city = "北京";
        String res = WeatherUtil.useSoJson(city);
        System.out.println("useSoJson weather result : " + res);
    }

    @Test
    public void getWeatherByIp() throws Exception {
        String ip = "1.119.195.242";
        String cityRes = IpUtil.getCityInfo(ip);
        String city = cityRes.split("\\|")[3];
        InputStream is = this.getClass().getResourceAsStream("/application-dev.properties");
        Properties prop = new Properties();
        prop.load(is);
        String user = prop.getProperty("other.weatherUserId");
        String key = prop.getProperty("other.weatherApiKey");

        String res1 = WeatherUtil.getNowWeather(key, city);
        System.out.println("getNowWeather result : " + res1);

        String res2 = WeatherUtil.getDailyWeather(user, key, city);
        System.out.println("getDailyWeather result : " + res2);
    }

}