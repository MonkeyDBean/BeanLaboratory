package com.monkeybean.dynamicds.service;

/**
 * 注解@Profile为指定配置生效, 适用场景: 开发环境与生产环境的功能不同
 * reference: https://www.cnblogs.com/jingmoxukong/p/10151785.html
 * <p>
 * Created by MonkeyBean on 2019/8/13.
 */
public interface ProfileService {
    String getProfileInfo();
}
