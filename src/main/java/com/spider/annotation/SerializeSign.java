package com.spider.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SerializeSign {

    boolean needSerialize() default false;
}
