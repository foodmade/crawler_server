package com.spider.commonUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.spider.spiderUtil.Item;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class RobotOnMessageHandler {

    @Inject
    MongoUtils mongoUtils;

    /**
     * 精确搜索
     */
    public List<Item> searchMovieInfo(String movieName){
        if(CommonUtils.isEmpty(movieName)){
            return null;
        }
        List<Item> result = new ArrayList<>();

        DBObject query = new BasicDBObject()
                        .append("videoName",movieName);

        List<DBObject> items =  mongoUtils.getMongoDB().getCollection(MongoTable._VIDEO_SOURCES).find(query,CommonUtils.getFelid()).toArray();

        if(items == null || items.isEmpty()){
            return null;
        }

        for (DBObject dbObject:items){
            result.add(JSON.parseObject(dbObject.toString(),new TypeReference<Item>(){}));
        }
        return result;
    }

    /**
     * 模糊搜索
     * @param movieName 电影名称
     */
    public List<Item> searchVagueMovieInfo(String movieName){
        if(CommonUtils.isEmpty(movieName)){
            return null;
        }
        List<Item> result = new ArrayList<>();

        DBObject query = new BasicDBObject()
                .append("videoName",CommonUtils.getPatternFiled(movieName));

        List<DBObject> items =  mongoUtils.getMongoDB().getCollection(MongoTable._VIDEO_SOURCES).find(query,CommonUtils.getFelid()).toArray();

        if(items == null || items.isEmpty()){
            return null;
        }

        for (DBObject dbObject:items){
            result.add(JSON.parseObject(dbObject.toString(),new TypeReference<Item>(){}));
        }
        return result;
    }
}
