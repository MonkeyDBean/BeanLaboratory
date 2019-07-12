package com.monkeybean.labo.component.annotation;

import java.lang.annotation.*;

/**
 * 测试注解类
 * <p>
 * Created by MonkeyBean on 2019/7/11.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestAnnotation {

    /**
     * 参数名
     */
    String property() default "";

    /**
     * 参数中第几位
     */
    int index() default 0;
}
