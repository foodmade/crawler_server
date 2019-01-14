package com.spider.enumUtil;

public enum  ExceptionEnum {
    SERVER_ERR("500","服务器内部错误"),
    TASKTYPE_ERR("10001","任务类型异常"),
    SUCCESS("200","成功"),
    PARAMEMPTYPEROR("10002","参数缺失"),
    EMAILFORMATERR("10003","邮箱格式错误"),
    REQUESTERR("10004","异常的请求"),
    LATERRETRY("10005","请稍后重试"),
    USERLOGINEXPIRE("10006","登陆信息已失效,请重新登陆"),
    AUTHCODEERR("10007","验证码输入错误或已过期"),
    CONFIRMPASSWORDERR("10008","2次密码输入不一致"),
    EXISTEUSER("10009","该邮箱已被注册"),
    USERINFOERR("10010","用户名或密码错误"),
    UNKNOWNERR("100101","未知错误");

    private String code;

    private String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
