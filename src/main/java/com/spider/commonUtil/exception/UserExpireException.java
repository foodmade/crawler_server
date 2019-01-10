package com.spider.commonUtil.exception;

import com.spider.enumUtil.ExceptionEnum;

/**
 * 用户信息过期
 */
public class UserExpireException extends RuntimeException {

    private String code;

    public UserExpireException() {
        super();
    }

    public UserExpireException(String message) {
        super(message);
        this.code = ExceptionEnum.SERVER_ERR.getCode();
    }

    public UserExpireException(ExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
    }

    public UserExpireException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
