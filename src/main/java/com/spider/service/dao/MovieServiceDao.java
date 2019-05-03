package com.spider.service.dao;

import com.spider.entity.mongoEntity.CollectMapper;
import com.spider.spiderUtil.Item;

public interface MovieServiceDao {

    /**
     * 根据影片ID获取影片详情
     * @param videoId
     * @return
     */
    Item getMovieInfoById(String videoId);

    /**
     * 根据影片id查询当前用户是否已经收藏
     * @param movieId  影片ID
     * @param userId   用户ID
     * @return
     */
    CollectMapper getCollectByMovieId(String movieId,Long userId);

}
