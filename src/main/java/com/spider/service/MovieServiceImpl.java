package com.spider.service;

import com.spider.commonUtil.CommonUtils;
import com.spider.entity.mongoEntity.CollectMapper;
import com.spider.service.dao.MovieServiceDao;
import com.spider.spiderUtil.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Item getMovieInfoById(String videoId) {

        if(CommonUtils.isEmpty(videoId)){
            return null;
        }
        try {
            return mongoTemplate.findOne(new Query(new Criteria("_id").is(videoId)),Item.class);
        } catch (Exception e) {
            logger.error("【根据videoId查询电影详情出现异常 】e:{}",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CollectMapper getCollectByMovieId(String movieId, Long userId) {

        if(CommonUtils.isEmpty(movieId) || userId == null){
            return null;
        }
        try {
            return mongoTemplate.findOne(new Query(
                    new Criteria("userId")
                    .is(userId)
                    .and("videoId")
                    .is(movieId)),
                    CollectMapper.class);
        } catch (Exception e) {
            logger.error("【根据videoId和UserId查询收藏信息出现异常 】e:{}",e.getMessage());
        }
        return null;
    }
}
