package com.spider.commonUtil.exception;

import com.spider.enumUtil.ExceptionEnum;

public class TaskTypeException extends RuntimeException{

    protected String code;

    public TaskTypeException() {
        super();
    }

    public TaskTypeException(String message) {
        super(message);
        this.code = ExceptionEnum.TASKTYPE_ERR.getCode();
    }

    public TaskTypeException(ExceptionEnum exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
    }

    public TaskTypeException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
