package com.monkeybean.dynamicds.service.impl;

import com.monkeybean.dynamicds.service.ProfileService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by MonkeyBean on 2019/8/13.
 */
@Profile(value = "fat")
@Service
public class ProfileFatServiceImpl implements ProfileService {

    @Override
    public String getProfileInfo() {
        return "当前是测试环境, 仅展示测试功能";
    }
}
