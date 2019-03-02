package com.spider.listener.Inckeystrategy;

import com.spider.commonUtil.CommonUtils;

/**
 * 随机数生成策略
 */
public class UUIDStrategy extends AbsStrategy {
    public UUIDStrategy(CommonUtils commonUtils,String collName) {
        super(commonUtils,collName);
    }

    @Override
    public <T> T doGenId() {
        return (T)CommonUtils.getUuid();
    }
}
