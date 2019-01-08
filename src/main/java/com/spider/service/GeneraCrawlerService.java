package com.spider.service;

import com.spider.commonUtil.SpiderTypeConst;
import com.spider.dao.SpiderControlMapper;
import com.spider.entity.SpiderControl;
import com.spider.spiderUtil.GeneralSpiderTaskFactoryService;
import com.spider.spiderUtil.SpiderInfoObj;
import com.spider.taskMaker.AbstractGeneraSpiderTaskMaker;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeneraCrawlerService {

    @Autowired
    private SpiderControlMapper spiderControlMapper;

    @Autowired
    private GeneralSpiderTaskFactoryService generalSpiderTaskFactoryService;

    private static Logger logger = Logger.getLogger(GeneraCrawlerService.class);
    public static ConcurrentHashMap<String, SpiderInfoObj> spiderHostMap  = new ConcurrentHashMap<>();

    public Object dispatchCrawlerTask(HttpServletRequest request, String spiderType){
        return dispatchCrawlerTaskResult(request, spiderType);
    }

    /**
     * 为爬虫分配任务
     */
    private Object dispatchCrawlerTaskResult(HttpServletRequest request, String spiderType){
        if (AbstractGeneraSpiderTaskMaker.contextPath == null)
            AbstractGeneraSpiderTaskMaker.contextPath = request.getSession().getServletContext().getRealPath("/");
        String spider_ip = request.getRemoteHost();
        SpiderInfoObj spiderInfoObj = getSpiderInfoObj(spider_ip,spiderType);
        logger.debug("【获取到信息为 ip:{"+spider_ip+"} 任务类型为:{"+spiderType+"} 的爬虫构造器】");
        if(spiderInfoObj == null){
            return null;
        }
        return getGeneralTask(spiderInfoObj,request);
    }

    /**
     * 爬虫提交任务
     */
    public void dispatchCommitTaskResult(HttpServletRequest request, String spiderType) {
        String hostAddr = request.getRemoteHost();
        SpiderInfoObj spiderInfoObj = getSpiderInfoObj(hostAddr, spiderType);
        if (spiderInfoObj == null) {
            return;
        }
        doCommitTaskResult(spiderInfoObj, request);
    }

    private void doCommitTaskResult(SpiderInfoObj spiderInfoObj, HttpServletRequest request) {
        spiderInfoObj.commitTaskResult(request);
    }

    private SpiderInfoObj getSpiderInfoObj(String spider_ip,String task_type){
        if(spiderHostMap.isEmpty()){
            initSpiderHostMap();
        }
        SpiderInfoObj result = spiderHostMap.get(spider_ip + "_" + task_type);
        if (result == null) {
            logger.debug("【没有获取到合法爬虫  ip:{"+spider_ip+"}  spider_type:{"+task_type+"}】");
            result = new SpiderInfoObj(spider_ip,task_type);
            result.setCurTaskType(SpiderTypeConst.taskTypeUpdateConfigState);
        }
        return result;
    }

    private Object getGeneralTask(SpiderInfoObj spiderInfoObj,HttpServletRequest request) {
        Object result = null;
        try {
            result = spiderInfoObj.getTask(request);
        } catch (Exception e) {
            logger.error("【e 15445:】"+e.getMessage());
        }
        //设置爬虫状态
        if(result != null){
            curTaskTypeChange(spiderInfoObj);
        }
        return result;
    }

    private void curTaskTypeChange(SpiderInfoObj spiderInfoObj) {
        /**
         * 服务器端进行爬虫控制 第一次请求返回配置文件 第二次请求获取解析器 之后返回执行任务
         */
        if (spiderInfoObj.getCurTaskType().equals(SpiderTypeConst.taskTypeUpdateConfigState)) {
            spiderInfoObj.setCurTaskType(SpiderTypeConst.taskTypeUpdateParserState);
            logger.info("【爬虫ip:{"+spiderInfoObj.getSpiderAddr()+"} 执行更新config操作】");
        } else if (spiderInfoObj.getCurTaskType().equals(SpiderTypeConst.taskTypeUpdateParserState)) {
            spiderInfoObj.setCurTaskType(SpiderTypeConst.taskTypeCommonState);
            logger.info("【爬虫ip:{"+spiderInfoObj.getSpiderAddr()+"} 执行更新parser操作】");
        }
    }

    /**
     * 初始化爬虫白名单
     */
    public synchronized void initSpiderHostMap(){
        List<SpiderControl> allSpiderList = spiderControlMapper.selectAll();
        if(allSpiderList == null || allSpiderList.isEmpty()){
            logger.warn("【未获取到爬虫白名单相关配置】");
            return;
        }
        for (SpiderControl spiderControl:allSpiderList){
            spiderHostMap.put(spiderControl.getSpiderIp()+"_"+spiderControl.getSpiderType(),transToSpiderInfoObj(spiderControl));
        }
        logger.info("【初始化爬虫白名单成功 size:{"+allSpiderList.size()+"}】");
    }

    /**
     * 初始化爬虫状态
     */
    private SpiderInfoObj transToSpiderInfoObj(SpiderControl spiderControl) {
        SpiderInfoObj spiderInfoObj = new SpiderInfoObj();
        spiderInfoObj.setSpiderAddr(spiderControl.getSpiderIp());
        spiderInfoObj.setSpiderType(spiderControl.getSpiderType());
        spiderInfoObj.setCurTaskType(SpiderTypeConst.taskTypeUpdateConfigState);
        spiderInfoObj.setTaskMakerService(generalSpiderTaskFactoryService);
        return spiderInfoObj;
    }
}
