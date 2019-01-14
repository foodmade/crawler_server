package com.spider.commonUtil;

public class RedisKey {

    private static final String PREFIX = "api";

    private static final String CACHE_PREFIX = ":";

    public static final Integer _TIME_SECOND_ONE = 1;

    public static final Integer _TIME_MINUTE_ONE = _TIME_SECOND_ONE * 60;

    public static final Integer _TIME_HOUR_ONE = _TIME_MINUTE_ONE * 60;

    /**
     * 邮件注册验证码key
     * @param key sessionId
     */
    public static String registerCodeKey(String key){
        return buildKey("registerCode",key);
    }

    /**
     * 登录状态key
     * @param key sessionId
     */
    public static String loginStatusKey(String key){
        return buildKey("loginStatus",key);
    }

    private static String buildKey(Object str1, Object... array) {
        StringBuilder stringBuffer = new StringBuilder(PREFIX);
        stringBuffer.append(CACHE_PREFIX).append(str1);
        for (Object obj : array) {
            stringBuffer.append(CACHE_PREFIX).append(obj);
        }
        return stringBuffer.toString();
    }
}
