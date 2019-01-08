package com.spider.spiderUtil.saveUtil;

import com.alibaba.fastjson.JSON;
import com.spider.commonUtil.CommonUtils;
import com.spider.spiderUtil.Item;
import com.spider.spiderUtil.WorkData;
import com.spider.taskPool.TaskData;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class DefaultAfterHandler extends AbsSave {

    private static Logger logger = Logger.getLogger(DefaultAfterHandler.class);

    @Override
    public void afterStageOneDataCache(WorkData workData) {
        if(workData.getTaskData() == null){
            logger.warn("异常的提交任务 taskData等于空 stage:0");
            return;
        }
        String query_id = workData.getTaskData().getQuery_id();
        if(CommonUtils.isEmpty(query_id)){
            logger.warn("seq:47328  query_id为空");
            return;
        }
        HashMap<String,Item> nodeCacheMap = cacheVideoMap.get(query_id);
        if(nodeCacheMap == null || nodeCacheMap.isEmpty()){
            nodeCacheMap = new HashMap<>();
        }
        List<Item> videoItems = workData.getItems();
        if(videoItems == null ||videoItems.isEmpty()){
            return;
        }
        TaskData taskData = workData.getTaskData();
        for (Item item:videoItems){
            item.setCateId(taskData.getCateid());
            item.setParentId(taskData.getParentid());
            nodeCacheMap.put(item.getVideoId(),item);
        }
        cacheVideoMap.put(query_id,nodeCacheMap);
    }

    @Override
    public void afterStageTwoDataCache(WorkData workData) {
        if(workData.getTaskData() == null){
            logger.warn("异常的提交任务 taskData等于空 stage:1");
            return;
        }
        String query_id = workData.getTaskData().getQuery_id();
        if(CommonUtils.isEmpty(query_id)){
            logger.warn("seq:47329  query_id为空");
            return;
        }
        List<Item> items = workData.getItems();
        logger.debug("第二阶段任务提交内容:"+ JSON.toJSONString(items));
        if(items == null || items.size() == 0){
            return;
        }
        HashMap<String,Item> itemMap = cacheVideoMap.get(query_id);
        if(itemMap == null || itemMap.isEmpty()){
            return;
        }
        //合并第一阶段数据
        TaskData taskData = workData.getTaskData();
        if(taskData == null){
            return;
        }
        String videoId = taskData.getVideoid();
        if(CommonUtils.isEmpty(videoId)){
            return;
        }
        if(!itemMap.containsKey(videoId)){
            return;
        }
        Item item = itemMap.get(videoId);
        item.setVideoSourceList(items.get(0).getVideoSourceList());
        if(!CommonUtils.isEmpty(items.get(0).getVideoDesc())){
            item.setVideoDesc(items.get(0).getVideoDesc());
        }
        itemMap.put(videoId,item);

        cacheVideoMap.put(query_id,itemMap);
    }
}
