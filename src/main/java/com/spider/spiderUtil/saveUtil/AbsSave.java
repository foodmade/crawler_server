package com.spider.spiderUtil.saveUtil;

import com.mongodb.DBCollection;
import com.spider.commonUtil.CommonUtils;
import com.spider.commonUtil.MongoTable;
import com.spider.spiderUtil.Item;
import com.spider.spiderUtil.WorkData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
