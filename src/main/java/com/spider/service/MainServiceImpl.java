package com.spider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.spider.annotation.IncKey;
import com.spider.commonUtil.*;
import com.spider.commonUtil.emailUtil.EmailModel;
import com.spider.commonUtil.mongoUtil.MongoTable;
import com.spider.commonUtil.mongoUtil.MongoUtil;
import com.spider.entity.BaseResult;
import com.spider.entity.mongoEntity.Account;
import com.spider.entity.mongoEntity.CollectMapper;
import com.spider.enumUtil.ExceptionEnum;
import com.spider.service.dao.MainServiceDao;
import com.spider.service.dao.MovieServiceDao;
import com.spider.spiderUtil.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MainServiceImpl implements MainServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);

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
    @Inject
    private MovieServiceDao movieServiceDao;

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

        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("cateId").is(Integer.parseInt(videoId)),
                Criteria.where("videoSourceList")
                        .ne(new ArrayList<>())
                        .exists(true)
        ));

        Pageable pageable = new PageRequest(page, page_size);
        query.with(pageable);

        long count = mongoTemplate.count(query,Item.class);

        List<Item> items = mongoTemplate.find(query,Item.class);

        BaseResult result = new BaseResult(items);
        result.setPageSize(pageable.getPageSize());
        result.setPage(pageable.getPageNumber());
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
            new Thread(() -> {
                try {
                    emailUtil.sendMail(eMdl);
                } catch (Exception e) {
                    e.printStackTrace();
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

    public BaseResult movieDotPraise(Item model) {
        if(CommonUtils.isEmpty(model.getMovieid())){
            return BaseResult.makeResult(ExceptionEnum.PARAMEMPTYPEROR);
        }
        Query query = new Query(Criteria.where("_id").is(model.getMovieid()));

        Update update = new Update().inc("dotcnt",1);

        try {
            mongoTemplate.upsert(query,update,Item.class);
        } catch (Exception e) {
            return BaseResult.makeResult(ExceptionEnum.SERVER_ERR);
        }
        return BaseResult.success(null);
    }

    public BaseResult collectionMovie(Account userAccessTokenBean, String movieId) {

        if(CommonUtils.isEmpty(movieId)){
            return BaseResult.makeResult(ExceptionEnum.PARAMEMPTYPEROR);
        }

        //查询当前影片资源是否存在
        Item item = movieServiceDao.getMovieInfoById(movieId);
        if(item == null){
            return BaseResult.makeExceptionResult("不存在的影片 movieId:" + movieId);
        }
        //查询当前用户是否已经点赞过
        CollectMapper mapper = movieServiceDao.getCollectByMovieId(movieId,userAccessTokenBean.getUserId());
        if(mapper != null){
            return BaseResult.makeExceptionResult("当前影片已收藏");
        }
        CollectMapper collectMapper = new CollectMapper();
        collectMapper.setCollectTime(DateUtils.formatYMDHMS(new Date()));
        collectMapper.setUserId(userAccessTokenBean.getUserId());
        collectMapper.setVideoId(movieId);

        try {
            mongoTemplate.insert(collectMapper);
        } catch (Exception e) {
            logger.error("【收藏影片时出现异常】e:{}",e.getMessage());
            return BaseResult.makeResult(ExceptionEnum.SERVER_ERR);
        }
        return BaseResult.success(null);
    }
}
