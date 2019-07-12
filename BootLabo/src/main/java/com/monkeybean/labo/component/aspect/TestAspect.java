package com.monkeybean.labo.component.aspect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.monkeybean.labo.component.annotation.TestAnnotation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 仅测试切面
 * 参考链接: https://www.ibm.com/developerworks/cn/java/j-spring-boot-aop-web-log-processing-and-distributed-locking/index.html
 * <p>
 * Created by MonkeyBean on 2019/7/11.
 */
@Aspect
@Component
//值越小, 优先级越高, 不指定则优先级最低
@Order(1)
public class TestAspect {
    private static final Logger logger = LoggerFactory.getLogger(TestAspect.class);

    @Before("@annotation(testAnnotation)")
    public void doBefore(JoinPoint joinPoint, TestAnnotation testAnnotation) {
        String methodName = joinPoint.getSignature().getName();
        JSONArray argArray = JSONArray.parseArray(JSONObject.toJSONString(joinPoint.getArgs()));
        String requirementValue;
        if (argArray.isEmpty()) {
            requirementValue = "defaultEmpty";
        } else {

            //参数值为空则获取索引对应参数值
            if (StringUtils.isEmpty(testAnnotation.property())) {
                requirementValue = argArray.getString(testAnnotation.index());
            } else {
                requirementValue = argArray.getJSONObject(testAnnotation.index()).getString(testAnnotation.property());
            }
        }
        logger.info("TestAspect, doBefore, methodName: [{}], annotation property: [{}], index: [{}], requirementValue: [{}]", methodName, testAnnotation.property(), testAnnotation.index(), requirementValue);

        //业务逻辑, 如分布式锁, Redis加锁
    }

    @After("@annotation(com.monkeybean.labo.component.annotation.TestAnnotation)")
    public void doAfter() {
        logger.info("TestAspect, doAfter");

        //业务逻辑, 如分布式锁, Redis解锁
    }
}
