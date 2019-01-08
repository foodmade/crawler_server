package com.spider.taskMaker;

import com.spider.spiderUtil.TaskOptions;
import com.spider.taskPool.TaskData;
import com.spider.taskPool.TaskPool;

import javax.servlet.http.HttpServletRequest;

public class YunPanTaskMaker extends AbstractGeneraSpiderTaskMaker {

    private static Object config;
    private static Object parser;
    private static boolean parserExpire = true;
    private static boolean configExpire = true;
    private static String dirpath = "/WEB-INF/CrawlerData/YunPan";

    @Override
    protected Object getConfig() {
        return config;
    }

    @Override
    protected void setConfig(Object config) {
        YunPanTaskMaker.config =config;
    }

    @Override
    protected Object getParser() {
        return YunPanTaskMaker.parser;
    }

    @Override
    public boolean isParserExpire() {
        return YunPanTaskMaker.parserExpire;
    }

    @Override
    public boolean isConfigExpire() {
        return YunPanTaskMaker.configExpire;
    }

    @Override
    protected void setParser(Object parserObj) {
        YunPanTaskMaker.parser = parserObj;
    }

    @Override
    public void setParserExpire(boolean parserExpire) {
        YunPanTaskMaker.parserExpire = parserExpire;
    }

    @Override
    public void setConfigExpire(boolean configExpire) {
        YunPanTaskMaker.configExpire = configExpire;
    }

    @Override
    public String getDirPath() {
        return YunPanTaskMaker.dirpath;
    }

    @Override
    protected Object getCommonTask(HttpServletRequest request, String spiderType) {
        //从任务池获取一个任务
        TaskData taskData = TaskPool.getSubTask(spiderType);
        if(taskData == null){
            return null;
        }
        if(taskData.getStage() == 0){
            return TaskOptions.getYunPanTaskOptions(taskData);
        }else if(taskData.getStage() == 1){
            return TaskOptions.getYunPanDetailOptions(taskData);
        }
        return null;
    }

    @Override
    protected void commitCommonTaskResult(HttpServletRequest request) {
        super.commitCommonTaskResult(request);
    }
}
