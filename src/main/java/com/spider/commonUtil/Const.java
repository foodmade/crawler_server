package com.spider.commonUtil;

public class Const {

    public static final int _AUTHCODE_DEFAULT_TIME = 60;  //邮件验证码有限时间

    public static final String _USER_SESSION_KEY = "userAccessTokenBean";  //用户信息存放key

    public static final Integer _CONFIG_DB         = 1;      //redis Config  -- redis
    public static final Integer _USER_SESSION_DB   = 2;      //用户信息存放库  -- redis
    public static final Integer _RECORD_ID_DB      = 3;      //每日访问ip记录  -- redis

}
