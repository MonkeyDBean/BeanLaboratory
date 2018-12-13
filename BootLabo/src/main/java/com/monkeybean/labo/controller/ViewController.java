package com.monkeybean.labo.controller;

import com.monkeybean.labo.predefine.CacheData;
import com.monkeybean.labo.service.database.LaboDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by MonkeyBean on 2018/06/02.
 */
@RequestMapping(path = "view/display")
@Controller
public class ViewController {

    private static Logger logger = LoggerFactory.getLogger(ViewController.class);

    @Autowired
    private LaboDoService laboDoService;

    @GetMapping("active")
    public String activeMail(ModelMap map, HttpServletRequest request) {
        String activeDes = "激活状态";
        String mapKey = request.getParameter("verify");
        if (!StringUtils.isEmpty(mapKey)) {
            String mapValue = CacheData.MAIL_KEY_MAP.get(mapKey);
            CacheData.MAIL_KEY_MAP.remove(mapKey);
            if (mapValue != null && mapValue.length() > 15) {
                String phone = mapValue.substring(0, 11);
                String mail = mapValue.substring(11);
                Map<String, Object> accountInfo = laboDoService.queryAccountInfoByPhone(phone);
                if (accountInfo == null) {
                    logger.warn("activeMail, account not exist in db, phone: {}", phone);
                    activeDes = "激活失败，账号不存在";
                } else {

                    //邮箱是否被其他账号使用
                    if (laboDoService.queryAccountInfoByEmail(mail) != null) {
                        logger.info("mail: {} has been used by others", mail);
                        activeDes = "邮箱已被其他账号使用";
                    } else {

                        //账号是否已绑定该邮箱
                        if (accountInfo.get("email") != null && mail.equals(accountInfo.get("email").toString())) {
                            logger.info("account: {} has bind this mail: {}", phone, mail);
                            activeDes = "邮箱已激活";
                        } else {
                            laboDoService.updateEmail(phone, mail);
                            activeDes = "激活成功，账号已绑定邮箱";
                        }
                    }
                }
            } else {
                activeDes = "激活链接失效，若邮箱未激活，则需重新申请激活链接";
            }
        }
        map.addAttribute("activeDes", activeDes);
        return "active";

        // 前后端完全分离，可跳转到前端地址
        //String activeFrontUrl = "$http..." + "?" + "des=";
        //response.sendRedirect(activeFrontUrl);
    }
}
