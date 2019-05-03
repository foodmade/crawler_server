package com.spider.service.dao;

import com.spider.entity.BaseResult;
import com.spider.entity.UserModel;
import com.spider.entity.mongoEntity.Account;
import com.spider.spiderUtil.Item;

public interface MainServiceDao {

    BaseResult getMovieTypeInfo();

    /**
     * 根据类目id搜索影片
     * @param videoId
     * @param page
     * @param page_size
     * @return
     */
    BaseResult getMoviesInfo(String videoId,Integer page,Integer page_size);

    /**
     * 根据电源名称搜索店铺
     */
    BaseResult getMoviesByNameInfo( String videoName,Integer page,Integer page_size);

    /**
     * 邮件发送
     */
    BaseResult sendEmail(String sessionId,String email);

    /**
     * 点赞影片
     */
    BaseResult movieDotPraise(Item paramModel);

    /**
     * 收藏影片
     */
    BaseResult collectionMovie(Account userAccessTokenBean, String movieId);
}
