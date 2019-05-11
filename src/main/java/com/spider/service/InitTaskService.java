package com.spider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.*;
import com.spider.KQRobot.KQClient;
import com.spider.commonUtil.CommonUtils;
import com.spider.commonUtil.EmailUtil;
import com.spider.commonUtil.config.RedisCacheConfig;
import com.spider.commonUtil.mongoUtil.MongoTable;
import com.spider.commonUtil.mongoUtil.MongoUtil;
import com.spider.commonUtil.RobotOnMessageHandler;
import com.spider.entity.mongoEntity.BaseTask;
import com.spider.taskPool.TaskConst;
import com.spider.taskPool.TaskParams;
import com.spider.taskPool.TaskPool;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Service
public class InitTaskService {

    private static Logger logger = Logger.getLogger(InitTaskService.class);

    @Inject
    MongoUtil mongoUtil;
    @Inject
    EmailUtil emailUtil;
    @Inject
    CommonUtils commonUtils;
    @Inject
    RedisCacheConfig redisCacheConfig;

    @PostConstruct
    private void initTask(){
        //初始化任务池
        initAllTaskOptions();
        //初始化redisConfig
        redisCacheConfig.loadRedisConfig();
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
     * 任务调度 每隔10天 激活任务 等待爬虫执行
     */
    @Scheduled(cron = "0 0 0 1/10 * ? ")
    public void taskStatusScheduled(){
        commonUtils.updateBaseTaskStatus(TaskConst._OPEN_TASK_STATUS);
        logger.info("【baseTask任务状态激活成功,创建爬虫任务】");
        initAllTaskOptions();
    }

    /**
     * 每隔5分钟刷新redis配置到内存
     */
    @Scheduled(cron = "0 0/5 0 1/10 * ? ")
    public void updateRedisConfig(){
        redisCacheConfig.loadRedisConfig();
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

        //将任务状态定义为0 等待下一次更新资源时开启任务
        commonUtils.updateBaseTaskStatus(TaskConst._STOP_TASK_STATUS);
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
            collection = mongoUtil.getMongoDB().getCollection(MongoTable._BASE_TASK);
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
