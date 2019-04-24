package com.monkeybean.dingtalk.controller;

import com.monkeybean.dingtalk.component.config.CustomInfoConfig;
import com.monkeybean.dingtalk.util.DingUtil;
import com.monkeybean.dingtalk.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by MonkeyBean on 2019/4/19.
 */
@Controller
public class PageController {
    private final CustomInfoConfig customInfoConfig;

    @Autowired
    public PageController(CustomInfoConfig customInfoConfig) {
        this.customInfoConfig = customInfoConfig;
    }

    @GetMapping("/index")
    public String indexJump(Model model, HttpServletRequest request) {
        String ticket = DingUtil.getTicket(customInfoConfig.getAppKey(), customInfoConfig.getAppSecret());
        if (ticket != null) {
            int randStrLength = 10;
            String nonceStr = RandomUtil.getRandomString(randStrLength);
            long timeStamp = System.currentTimeMillis();
            String url = customInfoConfig.getSignUrl();
            String sign = DingUtil.sign(ticket, nonceStr, timeStamp, url);
            model.addAttribute("ticket", ticket);
            model.addAttribute("nonceStr", nonceStr);
            model.addAttribute("timeStamp", timeStamp);
            model.addAttribute("sign", sign);
            model.addAttribute("corpId", customInfoConfig.getCorpId());
            model.addAttribute("agentId", customInfoConfig.getAgentId());
        }
        return "index";
    }
}
