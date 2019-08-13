package com.monkeybean.dynamicds.service.impl;

import com.monkeybean.dynamicds.service.ProfileService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by MonkeyBean on 2019/8/13.
 */
@Profile(value = "pro")
@Service
public class ProfileProServiceImpl implements ProfileService {

    @Override
    public String getProfileInfo() {
        return "当前是生产环境, 展示线上功能";
    }
}
