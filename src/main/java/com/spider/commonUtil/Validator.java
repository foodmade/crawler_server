package com.spider.commonUtil;

import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class Validator {

    private volatile static ValidatorFactory factory = null;

    public static <T> void validate(T object, Class<?>... groups) throws Exception {
        if (null == object) {
            throw new Exception("参数不能为空");
        }

        if (null == factory) {
            synchronized (Validator.class) {
                if (null == factory) {
                    factory = Validation.buildDefaultValidatorFactory();
                }
            }
        }

        javax.validation.Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violationSet = validator.validate(object, groups);
        if (CollectionUtils.isNotEmpty(violationSet)) {
            for (ConstraintViolation<T> violation : violationSet) {
                throw new Exception(violation.getMessage());
            }
        }
    }
}
