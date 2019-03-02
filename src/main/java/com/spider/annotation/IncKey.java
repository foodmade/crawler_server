package com.spider.annotation;

import com.spider.listener.Inckeystrategy.AbsStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识自增主键
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IncKey {

    /**
     * id生成策略
     */
    Class<? extends AbsStrategy> strategyCls() default AbsStrategy.class;
}
