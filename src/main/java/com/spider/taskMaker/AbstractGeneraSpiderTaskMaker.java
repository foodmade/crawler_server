package com.spider.taskMaker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.spider.commonUtil.CommonUtils;
import com.spider.commonUtil.DateUtils;
import com.spider.commonUtil.mongoUtil.MongoTable;
import com.spider.commonUtil.SpiderTypeConst;
import com.spider.enumUtil.SaveEnum;
import com.spider.spiderUtil.Item;
import com.spider.spiderUtil.WorkData;
import com.spider.spiderUtil.saveUtil.AbsSave;
import com.spider.taskPool.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * 爬虫处理器抽象类
 */
public abstract class AbstractGeneraSpiderTaskMaker {

    protected static Logger logger = Logger.getLogger(AbstractGeneraSpiderTaskMaker.class);

    public static String contextPath = null;

    private ServletContext servletContext;

    private Properties appProperties;

    private CommonUtils commonUtils;

    public CommonUtils getCommonUtils() {
        return commonUtils;
    }

    public void setCommonUtils(CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Properties getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(Properties appProperties) {
        this.appProperties = appProperties;
    }

    abstract protected Object getConfig();

    protected abstract void setConfig(Object config);

    abstract protected Object getParser();

    abstract public boolean isParserExpire();

    abstract public boolean isConfigExpire();

    abstract protected void setParser(Object parserObj);

    public abstract void setParserExpire(boolean value);

    public abstract void setConfigExpire(boolean value);

    abstract public String getDirPath();


    public Object getTask(String curTaskType, HttpServletRequest request,String spiderType) {
        Object result;
        switch (curTaskType) {
            case SpiderTypeConst.taskTypeUpdateConfigState:
                result = getUpdateConfigTask(request);
                log(request, curTaskType);
                return result;
            case SpiderTypeConst.taskTypeUpdateParserState:
                result = getUpdateParserTask(request);
                log(request, curTaskType);
                return result;
            case SpiderTypeConst.taskTypeCommonState:
                return getCommonTask(request, spiderType);
        }
        return null;
    }

    public void commitTask(String curTaskType, HttpServletRequest request) {
        if (curTaskType.equals(SpiderTypeConst.taskTypeCommonState)) {
            commitCommonTaskResult(request);
        }
    }

    private void log(HttpServletRequest request,String type){
        String spider_ip = null;
        if(request != null){
            spider_ip = request.getRemoteAddr();
        }
        if(spider_ip == null){
            return;
        }
        logger.info("【日期：{"+ DateUtils.formatDate(new Date(),DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS) +"}爬虫ip:{"+spider_ip+"}  请求获取配置 type:{"+type+"}】");
    }

    private Object getUpdateConfigTask(HttpServletRequest request) {
        if(isConfigExpire()){
            ArrayList<Object> result = new ArrayList<Object>();
            HashMap<String, Object> innerMap = new HashMap<String, Object>();
            innerMap.put("config", transStringToMap(loadStringFromFile(null)));
            HashMap<String, Object> outerMap = getTaskResultTransdMap();
            outerMap.put("taskData", innerMap);
            outerMap.put("type", 3);
            result.add(outerMap);
            setConfig(result);
        }
        setConfigExpire(false);
        return getConfig();
    }

    private HashMap<String, Object> getTaskResultTransdMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("type", 1);
        result.put("info", "");
        result.put("taskData", null);
        return result;
    }

    private Object transStringToMap(String s) {
        return JSON.parseObject(s, HashMap.class);
    }

    private String loadStringFromFile(String filePath) {
        StringBuilder result = new StringBuilder();
        String fullFilePath;
        if (AbstractGeneraSpiderTaskMaker.contextPath == null) {
            AbstractGeneraSpiderTaskMaker.contextPath = servletContext.getRealPath("/");
        }
        if (filePath == null) {
            filePath = appProperties.getProperty("configPosition");
            fullFilePath = getDirPath() + filePath;
            fullFilePath = contextPath + fullFilePath;
        } else {
            fullFilePath = filePath;
        }
        logger.info("【更新配置文件地址：{"+filePath+"}】");
        File file = new File(fullFilePath);
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String tempStr;
            while ((tempStr = br.readLine()) != null) {
                result.append(tempStr.trim());
            }
            br.close();
        } catch (Exception e) {
            logger.error("【read file error:】"+e.getMessage());
        }
        return result.toString();
    }

    private Object getUpdateParserTask(HttpServletRequest request) {
        if (isParserExpire()) {
            ArrayList<HashMap<String, Object>> parsers = loadParserStringFromDir(appProperties.getProperty("parserDir"));
            setParser(transToParserString(parsers));
        }
        setParserExpire(false);
        return getParser();
    }

    private Object transToParserString(ArrayList<HashMap<String, Object>> parsers) {
        HashMap<String, Object> result = getTaskResultTransdMap();
        result.put("type", 2);
        HashMap<String, Object> parserMapItem = new HashMap<String, Object>();
        parserMapItem.put("parsers", parsers);
        result.put("taskData", parserMapItem);
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(result);
        return arrayList;
    }

    private ArrayList<HashMap<String, Object>> loadParserStringFromDir(String dirpath) {
        ArrayList<HashMap<String, Object>> parsers = new ArrayList<HashMap<String, Object>>();
        if (!dirpath.endsWith(File.separator)) {
            dirpath += File.separator;
        }
        dirpath = contextPath + getDirPath() + dirpath;
        File dirFile = new File(dirpath);
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return parsers;
        }

        File[] files = dirFile.listFiles();
        if(files == null){
            return parsers;
        }
        ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(files));
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getPath().compareTo(o2.getPath());
            }
        });
        for (File file : fileList) {
            Object parserString = loadStringFromFile(file.getPath());
            HashMap<String, Object> tmp = new HashMap<String, Object>();
            tmp.put("parserCode", parserString);
            parsers.add(tmp);
        }
        return parsers;
    }

    protected WorkData parserTaskData(HashMap<String,Object> commitMap){
        WorkData workData = new WorkData();
        if(commitMap == null || commitMap.isEmpty()){
            return workData;
        }
        workData.setRetcode(SpiderTypeConst._FAIL_CODE);
        Object task = commitMap.get("task");
        if(task == null){
            return workData;
        }
        HashMap<String,Object> taskOptions = JSON.parseObject(task+"",new TypeReference<HashMap<String, Object>>(){});
        if(taskOptions == null || taskOptions.isEmpty()){
            return workData;
        }
        task = taskOptions.get("task");
        if(task == null){
            return workData;
        }
        HashMap<String,Object> taskMap = JSON.parseObject(task+"",new TypeReference<HashMap<String, Object>>(){});
        TaskData taskData = CommonUtils.mapTransModel(taskMap,TaskData.class);
        workData.setTaskData(taskData);
        //解析爬虫提交内容
        HashMap<String,Object> itemMap = JSON.parseObject(commitMap.get("result")+"",new TypeReference<HashMap<String, Object>>(){});
        if(itemMap == null || itemMap.isEmpty()){
            return workData;
        }
        int retcode = 0;
        try {
            retcode = Integer.parseInt(itemMap.get("retcode")+"");
        } catch (NumberFormatException e) {
            logger.error("解析retcode异常 retMap:"+JSON.toJSONString(itemMap));
        }
        workData.setRetcode(retcode);
        if(retcode == SpiderTypeConst._FAIL_CODE){
            return workData;
        }
        JSONArray jsonArray = JSON.parseArray(itemMap.get("rows")+"");
        if(jsonArray == null || jsonArray.isEmpty()){
            return workData;
        }
        List<Item> items = getItems(jsonArray);
        workData.setItems(items);
        return workData;
    }

    private List<Item> getItems(JSONArray jsonArray) {
        List<Item> result = new ArrayList<>();
        if(jsonArray.size() == 0){
            return result;
        }
        HashMap itemMap;
        for (Object itemObj :jsonArray){
            itemMap = JSON.parseObject(itemObj+"",new TypeReference<HashMap>(){});
            if(itemMap == null){
                continue;
            }
            result.add(CommonUtils.mapTransModel(itemMap,Item.class));
        }
        return result;
    }

    private void checkTaskIsFinish(WorkData workData) {
        String query_id = workData.getTaskData().getQuery_id();
        QueryTaskNode queryTaskNode = TaskPool.getQueryTaskNode(query_id);
        if(queryTaskNode == null){
            logger.error("检查任务是否完成时,queryNode为空");
            return;
        }
        QueryInfo queryInfo = TaskPool.getQueryInfo(query_id,workData.getTaskData().getTask_type());
        if(queryInfo == null  || queryTaskNode.getFinish()){
            logger.info("任务已完成.........");
            return;
        }
        if(queryInfo.getUnOverTaskCnt() == 0){
            logger.info("任务已完成 query_id:"+query_id);
            //说明任务已经完成 标志当前任务已完成 防止其他线程进行重复操作
            queryTaskNode.setFinish(true);
            //开始保存缓存数据
            logger.debug("最终缓存数据:"+ JSON.toJSONString(AbsSave.cacheVideoMap.get(query_id)));
            saveData(workData);
        }
    }

    protected Object getCommonTask(HttpServletRequest request,String spiderType){
        return null;
    }

    protected void commitCommonTaskResult(HttpServletRequest request){
        String commitData = CommonUtils.readRequestBuff(request);
        logger.debug("提交内容:"+ commitData);

        HashMap<String,Object> commitMap = JSON.parseObject(commitData,new TypeReference<HashMap<String,Object>>(){});
        if(commitMap == null || commitMap.keySet().size() == 0){
            logger.error("解析提交文本失败");
            return;
        }
        WorkData workData = parserTaskData(commitMap);
        logger.debug("workData:"+JSON.toJSONString(workData));
        if(workData.getRetcode().equals(SpiderTypeConst._FAIL_CODE)){
            logger.info("爬虫提交异常 retcode:"+workData.getRetcode()+" commitStr:"+commitData);
            TaskPool.closeTask(workData.getTaskData().getTask_type(),workData.getTaskData().getTask_id(),workData.getTaskData().getQuery_id());
            return;
        }
        try {
            //获取当前maker对应的保存实例
            SaveEnum saveEnum = SaveEnum.find(workData.getTaskData().getTask_type());
            if(saveEnum == null){
                logger.error("未获取到task_type:"+workData.getTaskData().getTask_type()+" 对应的保存类,请声明定义");
                return;
            }
            //缓存数据至内存
            doCacheData(saveEnum,workData);
            //创建详情页任务
            doCreateDetailTask(workData);
        } catch (Exception e) {
            logger.error("处理【GVideo】提交任务出现异常 e:"+e.getMessage());
            e.printStackTrace();
        } finally {
            TaskPool.closeTask(workData.getTaskData().getTask_type(),workData.getTaskData().getTask_id(),workData.getTaskData().getQuery_id());
        }
        //检查任务是否全部完成
        checkTaskIsFinish(workData);
    }

    private void doCacheData(SaveEnum saveEnum,WorkData workData) {
        if(saveEnum == null){
            return;
        }
        Class<? extends AbsSave> cls = saveEnum.getCls();
        try {
            switch (workData.getTaskData().getStage()){
                case 0:
                    cls.newInstance().afterStageOneDataCache(workData);
                    return;
                case 1:
                    cls.newInstance().afterStageTwoDataCache(workData);
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void doCreateDetailTask(WorkData workData) {
   /*     if(checkPageIsOver(workData.getTaskData())){
            return;
        }*/
        List<TaskParams> taskParams = getTaskParams(workData);
        if(taskParams == null){
            return;
        }
//        TaskData taskData = workData.getTaskData();
        QueryTaskNode queryTaskNode = TaskPool.getQueryTaskNode(workData.getTaskData().getQuery_id());
        for (TaskParams taskParam:taskParams){
            TaskPool.appendSubTaskToNode(queryTaskNode,taskParam);
        }
        logger.debug("创建第二阶段任务成功 page:"+workData.getTaskData().getPage()+ " cateId:"+workData.getTaskData().getCateid()+" taskData:"+JSON.toJSONString(workData.getTaskData()));
    }

    private boolean checkPageIsOver(TaskData taskData) {
        QueryTaskNode queryTaskNode = TaskPool.getQueryTaskNode(taskData.getQuery_id());
        if(queryTaskNode == null){
            return true;
        }
        return queryTaskNode.getOverPageSet().contains(taskData.getPage());
    }

    private List<TaskParams> getTaskParams(WorkData workData) {
        List<Item> items = workData.getItems();
        if(items == null || items.isEmpty()){
            return null;
        }
        List<TaskParams> result = new ArrayList<>();
        TaskData taskData = workData.getTaskData();
        HashMap<String,String> childMap = taskData.getChildurl();
        Integer stage = taskData.getStage()+1;
        if(stage > taskData.getTotalstage()){
            logger.debug("已到达第【"+stage+"】阶段,最大阶段:【"+taskData.getTotalstage()+"】,无法创建任务");
            return null;
        }
        QueryTaskNode queryTaskNode = TaskPool.getQueryTaskNode(taskData.getQuery_id());
        if(queryTaskNode == null){
            return null;
        }
        queryTaskNode.getOverPageSet().add(taskData.getPage());
        for (Item item : items) {
            TaskParams taskParams = new TaskParams();
            taskParams.setStatus(1);
            taskParams.setDesc(workData.getTaskData().getDesc());
            taskParams.setUrl(childMap.get(stage.toString()));
            taskParams.setStage(stage);
            taskParams.setChildUrl(taskData.getChildurl());
            taskParams.setTask_type(taskData.getTask_type());
            taskParams.setMaxPage(1);
            taskParams.setVideoId(item.getVideoId());
            taskParams.setChildCateInfo(taskData.getChildCateInfo());
            result.add(taskParams);
        }
        return result;
    }

    protected void saveData(WorkData workData){
        String query_id = workData.getTaskData().getQuery_id();
        if(CommonUtils.isEmpty(query_id)){
            return;
        }
        HashMap<String,Item> cacheItems = AbsSave.cacheVideoMap.get(query_id);
        if(cacheItems == null || cacheItems.size() == 0){
            return;
        }
        List<Item> data = new ArrayList<>(cacheItems.values());

        long start = System.currentTimeMillis();
        try {
            DBCollection collection = commonUtils.getMongoUtil().getMongoDB().getCollection(MongoTable._VIDEO_SOURCES);
            List<DBObject> rows =CommonUtils.transDBObject(data);
            collection.insert(rows);
        } catch (Exception e) {
            logger.error("保存采集数据失败 e:"+e.getMessage());
            e.printStackTrace();
        }
        logger.info("【保存数据完毕 耗时】:"+(System.currentTimeMillis()-start));
        logger.info("【资源更新完毕：】taskType:{"+workData.getTaskData().getTask_type()+"} size:{"+data.size()+"}");
    }
}
