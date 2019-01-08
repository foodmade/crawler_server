package com.spider.taskMaker;

import com.spider.commonUtil.CommonUtils;
import com.spider.spiderUtil.TaskOptions;
import com.spider.taskPool.TaskData;
import com.spider.taskPool.TaskPool;

import javax.servlet.http.HttpServletRequest;

public class HaiWaiTaskMaker extends AbstractGeneraSpiderTaskMaker {

    private static Object config;
    private static Object parser;
    private static boolean parserExpire = true;
    private static boolean configExpire = true;
    private static String dirpath = "/WEB-INF/CrawlerData/HaiWai";

    @Override
    protected Object getConfig() {
        return config;
    }

    @Override
    protected void setConfig(Object config) {
        HaiWaiTaskMaker.config =config;
    }

    @Override
    protected Object getParser() {
        return HaiWaiTaskMaker.parser;
    }

    @Override
    public boolean isParserExpire() {
        return HaiWaiTaskMaker.parserExpire;
    }

    @Override
    public boolean isConfigExpire() {
        return HaiWaiTaskMaker.configExpire;
    }

    @Override
    protected void setParser(Object parserObj) {
        HaiWaiTaskMaker.parser = parserObj;
    }

    @Override
    public void setParserExpire(boolean parserExpire) {
        HaiWaiTaskMaker.parserExpire = parserExpire;
    }

    @Override
    public void setConfigExpire(boolean configExpire) {
        HaiWaiTaskMaker.configExpire = configExpire;
    }

    @Override
    public String getDirPath() {
        return HaiWaiTaskMaker.dirpath;
    }


    @Override
    protected Object getCommonTask(HttpServletRequest request, String spiderType) {
        //从任务池获取一个任务
        TaskData taskData = TaskPool.getSubTask(spiderType);
        if(taskData == null){
            return null;
        }
        if(taskData.getStage() == 0){
            return TaskOptions.getHaiWaiTaskOptions(taskData);
        }else if(taskData.getStage() == 1){
            return TaskOptions.getHaiWaiDetailOptions(taskData);
        }
        return null;
    }

    @Override
    protected void commitCommonTaskResult(HttpServletRequest request) {
        super.commitCommonTaskResult(request);
    }
}
