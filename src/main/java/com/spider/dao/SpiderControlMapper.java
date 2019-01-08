package com.spider.dao;

import com.spider.entity.SpiderControl;

import java.util.List;

public interface SpiderControlMapper {
    int insert(SpiderControl record);

    int insertSelective(SpiderControl record);

    List<SpiderControl> selectAll();
}