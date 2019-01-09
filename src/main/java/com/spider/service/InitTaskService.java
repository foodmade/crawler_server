package com.spider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.*;
import com.spider.KQRobot.KQClient;
import com.spider.commonUtil.EmailUtil;
import com.spider.commonUtil.MongoTable;
import com.spider.commonUtil.MongoUtils;
import com.spider.commonUtil.RobotOnMessageHandler;
import com.spider.taskPool.TaskParams;
import com.spider.taskPool.TaskPool;
import org.apache.bval.constraints.Email;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Service
public class InitTaskService {

    private static Logger logger = Logger.getLogger(InitTaskService.class);

    @Inject
    MongoUtils mongoUtils;

    @Inject
    Properties appProperties;

    @Inject
    RobotOnMessageHandler robotOnMessageHandler;

    @Inject
    EmailUtil emailUtil;

    @PostConstruct
    private void initTask(){
        //初始化任务池
        initAllTaskOptions();
        //初始化邮件发送器
        emailUtil.initMailSender();
        //初始化酷Q客户端
        try {
//            KQClient.runClient(appProperties.getProperty("kqHost"),robotOnMessageHandler);
        } catch (Exception e) {
            logger.error("初始化KQ客户端异常 e:"+e.getMessage());
        }
    }

    /**
     * 初始化爬虫任务
     */
    public void initAllTaskOptions(){
        List<DBObject> allTasks = getAllBaseTask();

        if(allTasks == null || allTasks.size() == 0){
            logger.warn("未获取到爬虫任务,退出执行");
            return;
        }
        //将任务添加至任务池
        createTaskToPool(allTasks);
    }

    private void createTaskToPool(List<DBObject> allTasks) {
        long start = System.currentTimeMillis();
        List<TaskParams> taskParams = getTaskParams(allTasks);

        if(taskParams == null || taskParams.size() == 0){
            logger.info("无任务可执行,结束线程");
            return;
        }
        for(TaskParams params : taskParams){
            TaskPool.addSubTask(params);
        }
        logger.info("任务创建完毕,任务数量:"+taskParams.size()+" 耗时:"+(System.currentTimeMillis()-start));
    }

    private List<TaskParams> getTaskParams(List<DBObject> allTasks) {

        if(allTasks == null || allTasks.size() == 0){
            return null;
        }
        List<TaskParams> result = new ArrayList<>();
        for (DBObject taskDB:allTasks){
            TaskParams params = new TaskParams();
            params.setMaxPage(Integer.parseInt(taskDB.get("maxPage")+""));
            params.setTask_type(taskDB.get("task_type")+"");
            params.setUrl(taskDB.get("url")+"");
            params.setDesc(taskDB.get("desc")+"");
            params.setStatus(Integer.parseInt(taskDB.get("status")+""));
            params.setChildUrl(JSON.parseObject(taskDB.get("childUrl")+"",new TypeReference<HashMap<String, String>>(){}));
            params.setStage(0);
            params.setParentId(Integer.parseInt(taskDB.get("parent_id")+""));
            params.setChildCateInfo(JSON.parseObject(taskDB.get("childCateInfo")+"",new TypeReference<HashMap<String, Integer>>(){}));
            result.add(params);
        }
        return result;
    }

    private List<DBObject> getAllBaseTask(){
        DBCollection collection;
        try {
            collection = mongoUtils.getMongoDB().getCollection(MongoTable._BASE_TASK);
        } catch (Exception e) {
            logger.error("获取mongo连接出现异常 e:"+e.getMessage());
            return null;
        }

        DBObject queryDB = new BasicDBObject()
                .append("status",1);

        DBObject fieldDB = new BasicDBObject()
                .append("_id",0);

        return collection.find(queryDB,fieldDB).toArray();
    }
}
