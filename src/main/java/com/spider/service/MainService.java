package com.spider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.spider.commonUtil.CommonUtils;
import com.spider.commonUtil.MongoTable;
import com.spider.commonUtil.MongoUtils;
import com.spider.commonUtil.RobotOnMessageHandler;
import com.spider.entity.BaseResult;
import com.spider.enumUtil.ExceptionEnum;
import com.spider.spiderUtil.Item;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MainService {
    @Inject
    MongoUtils mongoUtils;
    @Inject
    RobotOnMessageHandler robotOnMessageHandler;

    public BaseResult getMovieTypeInfo(){
        DBObject query = new BasicDBObject()
                .append("level",2);

        DBCollection collection = mongoUtils.getMongoDB().getCollection(MongoTable._CATE_MAPPER);
        List<DBObject> data = collection.find(query,CommonUtils.getFelid()).toArray();
        return new BaseResult(data);
    }

    public BaseResult getMoviesInfo(String videoId,Integer page,Integer page_size) {
        if(CommonUtils.isEmpty(videoId)){
            return new BaseResult(ExceptionEnum.PARAMEMPTYPEROR);
        }
        DBCollection collection = mongoUtils
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

        DBObject query = new BasicDBObject()
                .append("videoName",CommonUtils.getPatternFiled(name));
        DBCollection collection = mongoUtils.getMongoDB().getCollection(MongoTable._VIDEO_SOURCES);
        long count = collection.count(query);
        List<DBObject> items =  collection.find(query,CommonUtils.getFelid()).skip((page-1)*page_size).limit(page_size).toArray();

        if(items == null || items.isEmpty()){
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
    public BaseResult sendEmail(String sessionId,String email) {
        if(CommonUtils.invalidEmailFormat(email)){
            return BaseResult.makeResult(ExceptionEnum.EMAILFORMATERR);
        }
        //获得随机验证码
        String code = CommonUtils.randomCode();
        return new BaseResult();
    }
}
