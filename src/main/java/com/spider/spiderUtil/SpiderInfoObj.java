package com.spider.spiderUtil;
import com.spider.taskMaker.AbstractGeneraSpiderTaskMaker;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class SpiderInfoObj {

    private static Logger logger = Logger.getLogger(SpiderInfoObj.class);

    private String spiderAddr;
    private String spiderType;
    private String curTaskType;

    private GeneralSpiderTaskFactoryService taskMakerService;

    public SpiderInfoObj() {
    }

    public SpiderInfoObj(String spiderAddr, String spiderType) {
        this.spiderAddr = spiderAddr;
        this.spiderType = spiderType;
    }

    public void setSpiderAddr(String spiderAddr) {
        this.spiderAddr = spiderAddr;
    }

    public void setSpiderType(String spiderType) {
        this.spiderType = spiderType;
    }

    public void setCurTaskType(String curTaskType) {
        this.curTaskType = curTaskType;
    }

    public void setTaskMakerService(GeneralSpiderTaskFactoryService taskMakerService) {
        this.taskMakerService = taskMakerService;
    }

    public String getSpiderAddr() {
        return spiderAddr;
    }

    public String getSpiderType() {
        return spiderType;
    }

    public String getCurTaskType() {
        return curTaskType;
    }

    public Object getTask(HttpServletRequest request) throws InstantiationException, IllegalAccessException {
        AbstractGeneraSpiderTaskMaker taskMaker = taskMakerService.getTaskMaker(this.getSpiderType());
        try {
            return taskMaker.getTask(getCurTaskType(), request,getSpiderType());
        } catch (Exception e) {
            logger.error("【fetch spider task fail e:】"+e.getMessage());
        }
        return null;
    }

    public void commitTaskResult(HttpServletRequest request){
       try {
           AbstractGeneraSpiderTaskMaker taskMaker = taskMakerService.getTaskMaker(this.getSpiderType());
           taskMaker.commitTask(this.curTaskType,request);
       }catch (Exception e){
           logger.error("【爬虫提交时发生错误 e:】"+e.getMessage());
           e.printStackTrace();
       }
    }
}
