package com.spider.spiderUtil;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Item {
    /**
     * 电影封面图
     */
    private String coverImg;
    /**
     * 影片类型
     */
    private String videoType;
    /**
     * 电影名称
     */
    private String videoName;
    /**
     * 电影详细信息 导演 演员
     */
    private HashMap<String, List<String>> figures;
    /**
     * 电影id
     */
    private String videoId;
    /**
     * 电影播放地址 key --> 地址描述  value-->视频地址
     */
    private List<HashMap<String,String>> videoSourceList = new ArrayList<>();
    /**
     * 电影简介
     */
    private String videoDesc;
    /**
     * 电影父类Id
     */
    private Integer parentId;
    /**
     * 电影所属类目
     */
    private Integer cateId;
    /**
     * 地区
     */
    private String areacity;
    /**
     * 年份
     */
    private Integer year;

    /**
     * 评分
     */
    private Double score;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
