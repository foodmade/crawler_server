package com.spider.taskMaker;

import com.spider.spiderUtil.TaskOptions;
import com.spider.taskPool.TaskData;
import com.spider.taskPool.TaskPool;

import javax.servlet.http.HttpServletRequest;

public class DongJingTaskMaker extends AbstractGeneraSpiderTaskMaker{
    private static Object config;
    private static Object parser;
    private static boolean parserExpire = true;
    private static boolean configExpire = true;
    private static String dirpath = "/WEB-INF/CrawlerData/DongJing";

    @Override
    protected Object getConfig() {
        return config;
    }

    @Override
    protected void setConfig(Object config) {
        DongJingTaskMaker.config =config;
    }

    @Override
    protected Object getParser() {
        return DongJingTaskMaker.parser;
    }

    @Override
    public boolean isParserExpire() {
        return DongJingTaskMaker.parserExpire;
    }

    @Override
    public boolean isConfigExpire() {
        return DongJingTaskMaker.configExpire;
    }

    @Override
    protected void setParser(Object parserObj) {
        DongJingTaskMaker.parser = parserObj;
    }

    @Override
    public void setParserExpire(boolean parserExpire) {
        DongJingTaskMaker.parserExpire = parserExpire;
    }

    @Override
    public void setConfigExpire(boolean configExpire) {
        DongJingTaskMaker.configExpire = configExpire;
    }

    @Override
    public String getDirPath() {
        return DongJingTaskMaker.dirpath;
    }

    @Override
    protected Object getCommonTask(HttpServletRequest request, String spiderType) {
        //从任务池获取一个任务
        TaskData taskData = TaskPool.getSubTask(spiderType);
        if(taskData == null){
            return null;
        }
        if(taskData.getStage() == 0){
            return TaskOptions.getDongJingTaskOptions(taskData);
        }else if(taskData.getStage() == 1){
            return TaskOptions.getDongJingDetailOptions(taskData);
        }
        return null;
    }

    @Override
    protected void commitCommonTaskResult(HttpServletRequest request) {
        super.commitCommonTaskResult(request);
    }
}
