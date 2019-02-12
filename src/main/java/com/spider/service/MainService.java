package com.spider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.spider.commonUtil.*;
import com.spider.commonUtil.emailUtil.EmailModel;
import com.spider.commonUtil.mongoUtil.MongoTable;
import com.spider.commonUtil.mongoUtil.MongoUtil;
import com.spider.entity.BaseResult;
import com.spider.enumUtil.ExceptionEnum;
import com.spider.spiderUtil.Item;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {

    private static Logger logger = Logger.getLogger(MainService.class);

    @Inject
    private MongoUtil mongoUtil;
    @Inject
    private RedisCacheManager redisCacheManager;
    @Inject
    private EmailUtil emailUtil;
    @Inject
    private MongoTemplate mongoTemplate;
    @Inject
    private UserManageService userManageService;

    public BaseResult getMovieTypeInfo(){
        DBObject query = new BasicDBObject()
                .append("level",2);

        DBCollection collection = mongoUtil.getMongoDB().getCollection(MongoTable._CATE_MAPPER);
        List<DBObject> data = collection.find(query,CommonUtils.getFelid()).toArray();
        return new BaseResult(data);
    }

    public BaseResult getMoviesInfo(String videoId,Integer page,Integer page_size) {
        if(CommonUtils.isEmpty(videoId)){
            return new BaseResult(ExceptionEnum.PARAMEMPTYPEROR);
        }
        DBCollection collection = mongoUtil
                .getMongoDB()
                .getCollection(MongoTable._VIDEO_SOURCES);

        //判断是否存在播放源
        DBObject isExistsSource = new BasicDBObject()
                .append("$exists",true)
                .append("$ne",new ArrayList<>());

        DBObject queryDB = new BasicDBObject()
                        .append("cateId",Integer.parseInt(videoId))
                        .append("videoSourceList",isExistsSource);

        long count = collection
                .count(queryDB);

        List<DBObject> videoList = collection
                .find(queryDB,CommonUtils.getFelid())
                .skip((page-1)*page_size)
                .limit(page_size)
                .toArray();
        BaseResult result = new BaseResult(videoList);
        result.setPageSize(page_size);
        result.setPage(page);
        result.setTotal(count);
        return result;
    }

    public BaseResult getMoviesByNameInfo(String name,Integer page,Integer page_size){
        BaseResult baseResult = new BaseResult();
        baseResult.setPage(page);
        baseResult.setPageSize(page_size);
        if(CommonUtils.isEmpty(name)){
            return baseResult;
        }
        List<Item> result = new ArrayList<>();

        //判断是否存在播放源
        DBObject isExistsSource = new BasicDBObject()
                .append("$exists",true)
                .append("$ne",new ArrayList<>());

        DBObject query = new BasicDBObject()
                .append("videoName",CommonUtils.getPatternFiled(name))
                .append("videoSourceList",isExistsSource);
        DBCollection collection = mongoUtil.getMongoDB().getCollection(MongoTable._VIDEO_SOURCES);
        long count = collection.count(query);
        List<DBObject> items =  collection.find(query,CommonUtils.getFelid()).skip((page-1)*page_size).limit(page_size).toArray();

        if(items.isEmpty()){
            return baseResult;
        }

        for (DBObject dbObject:items){
            result.add(JSON.parseObject(dbObject.toString(),new TypeReference<Item>(){}));
        }
        baseResult.setTotal(count);
        baseResult.setResponseBody(result);
        return baseResult;
    }

    /**
     * 发送email邮件
     */
    public BaseResult sendEmail(String sessionId, final String email) {
        if(CommonUtils.invalidEmailFormat(email)){
            return BaseResult.makeResult(ExceptionEnum.EMAILFORMATERR);
        }
        if(CommonUtils.isEmpty(sessionId)){
            return BaseResult.makeResult(ExceptionEnum.REQUESTERR);
        }
        String key = RedisKey.registerCodeKey(sessionId);
        //检查发送邮件时间间隔
        if(!checkSendIsTimeout(key)){
            return BaseResult.makeResult(ExceptionEnum.LATERRETRY);
        }
        if(userManageService.userIsExist(email)){
            return BaseResult.makeResult(ExceptionEnum.EXISTEUSER);
        }
        //获得随机验证码
        final String code = CommonUtils.randomCode();
        final EmailModel eMdl = new EmailModel(email,"register","您的验证码","【Search Movies】your code:["+code+"]");
        //发送邮件
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        emailUtil.sendMail(eMdl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            logger.error("【发送邮件出现异常 e:{"+e.getMessage()+"}】");
            return new BaseResult(ExceptionEnum.SERVER_ERR);
        }
        //缓存验证码至redis
        cacheCodeToRedis(code,key);
        return new BaseResult(ExceptionEnum.SUCCESS);
    }

    private boolean checkSendIsTimeout(String sessionId) {
        return redisCacheManager.get(CommonUtils.createRedisMode(sessionId,null)) == null;
    }

    private void cacheCodeToRedis(String code, String key) {
        redisCacheManager.set(CommonUtils.createRedisMode(key, code, Const._AUTHCODE_DEFAULT_TIME));
    }
}
