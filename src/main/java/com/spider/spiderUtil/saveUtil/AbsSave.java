package com.spider.spiderUtil.saveUtil;

import com.spider.spiderUtil.Item;
import com.spider.spiderUtil.WorkData;

import java.util.HashMap;

public abstract class AbsSave {

    public static HashMap<String, HashMap<String,Item>> cacheVideoMap = new HashMap<>();

    /**
     * 第一阶段保存
     */
    public void afterStageOneDataCache(WorkData workData){}

    /**
     * 第二阶段保存
     */
    public void afterStageTwoDataCache(WorkData workData){}

}
