package com.spider.annotation;

import com.spider.entity.UserModel;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseCheck {

    /**
     * 是否需要登录
     *
     * @return true需要 false不需要
     */
    boolean needLogin() default true;

    /**
     * 入参检查bean class
     */
    Class<?> beanClazz() default UserModel.class;

    /**
     * 是否需要检查参数
     */
    boolean needCheckParameter() default true;

    /**
     * 是否需要验证token
     */
    boolean needCheckToken() default true;

    /**
     * 权限拦截  默认员工级别
     */
    String permissionFilter() default "";
}
