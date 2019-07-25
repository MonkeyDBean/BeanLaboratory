package com.monkeybean.dynamicds.component.aspect;

import com.monkeybean.dynamicds.component.PublicConfig;
import com.monkeybean.dynamicds.constant.ConstantValue;
import com.monkeybean.dynamicds.reqres.Result;
import com.monkeybean.dynamicds.reqres.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Aspect
@Component
@Order(10)
@Slf4j
public class SecretKeyAspect {

    private final PublicConfig publicConfig;

    @Autowired
    public SecretKeyAspect(PublicConfig publicConfig) {
        this.publicConfig = publicConfig;
    }

    @Pointcut("execution(public * com.monkeybean.dynamicds.controller.TestController.queryDataSourceKeys(..))")
    public void queryDataSourceKeysPoint() {
    }

    @Around("queryDataSourceKeysPoint()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        String queryString = request.getQueryString();
        if (queryString != null && queryString.contains(publicConfig.getSecretKey())) {
            return pjp.proceed();
        } else {
            response.setStatus(ConstantValue.HTTP_CODE_UN_AUTHORIZED);
            return new Result<>(ReturnCode.AUTHORIZED_FAILED);
        }
    }
}
