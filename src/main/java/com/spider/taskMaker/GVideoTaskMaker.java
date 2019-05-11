package com.spider.taskMaker;

import com.spider.spiderUtil.TaskOptions;
import com.spider.spiderUtil.WorkData;
import com.spider.taskPool.*;

import javax.servlet.http.HttpServletRequest;

public class GVideoTaskMaker extends AbstractGeneraSpiderTaskMaker {

    private static Object config;
    private static Object parser;
    private static boolean parserExpire = true;
    private static boolean configExpire = true;
    private static String dirpath = "/WEB-INF/CrawlerData/GVideo";

    @Override
    protected Object getConfig() {
        return config;
    }

    @Override
    protected void setConfig(Object config) {
        GVideoTaskMaker.config =config;
    }

    @Override
    protected Object getParser() {
        return GVideoTaskMaker.parser;
    }

    @Override
    public boolean isParserExpire() {
        return GVideoTaskMaker.parserExpire;
    }

    @Override
    public boolean isConfigExpire() {
        return GVideoTaskMaker.configExpire;
    }

    @Override
    protected void setParser(Object parserObj) {
        GVideoTaskMaker.parser = parserObj;
    }

    @Override
    public void setParserExpire(boolean parserExpire) {
        GVideoTaskMaker.parserExpire = parserExpire;
    }

    @Override
    public void setConfigExpire(boolean configExpire) {
        GVideoTaskMaker.configExpire = configExpire;
    }

    @Override
    public String getDirPath() {
        return GVideoTaskMaker.dirpath;
    }

    @Override
    protected Object getCommonTask(HttpServletRequest request,String spiderType) {
        //从任务池获取一个任务
        TaskData taskData = TaskPool.getSubTask(spiderType);
        if(taskData == null){
            return null;
        }
        if(taskData.getStage() == 0){
            return TaskOptions.getGvideoOptions(taskData);
        }else if(taskData.getStage() == 1){
            return TaskOptions.getGVideoDetailOptions(taskData);
        }
        return null;
    }

    @Override
    protected void commitCommonTaskResult(HttpServletRequest request) {
        super.commitCommonTaskResult(request);
    }

    @Override
    protected void saveData(WorkData workData) {
        super.saveData(workData);
    }


}
