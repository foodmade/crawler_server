package com.spider.commonUtil;

public class RedisKey {

    private static final String PREFIX = "api";

    public static final String CACHE_PREFIX = ":";

    /**
     * 邮件注册验证码key
     * @param key sessionId
     */
    public static String registerCodeKey(String key){
        return buildKey("registerCode",key);
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
