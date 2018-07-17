package com.monkeybean.labo.component.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 数据库层日志
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@Aspect
@Component
public class DaoLogAspect {

    private static Logger logger = LoggerFactory.getLogger(DaoLogAspect.class);

    private ThreadLocal<Long> beginTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.monkeybean.labo.dao.*.*(..))")
    public void daoPoint() {
    }

    @Before("daoPoint()")
    public void doBefore(JoinPoint joinPoint) {
        beginTime.set(System.currentTimeMillis());
        logger.info("Dao Class Method: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("Dao Args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "daoPoint()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("Dao Response is: " + ret);
        logger.info("Dao Method execute takes: {} ms", System.currentTimeMillis() - beginTime.get());
    }

}
