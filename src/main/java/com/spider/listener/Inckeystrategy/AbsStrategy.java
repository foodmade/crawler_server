package com.spider.listener.Inckeystrategy;

import com.spider.commonUtil.CommonUtils;
import lombok.Setter;

public abstract class AbsStrategy {

    @Setter
    protected CommonUtils commonUtils;

    protected String collName;

    public AbsStrategy(CommonUtils commonUtils,String collName){
        this.commonUtils = commonUtils;
        this.collName    = collName;
    }

    public abstract <T> T doGenId();
}
