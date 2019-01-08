package com.spider.spiderUtil;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public String getAreacity() {
        return areacity;
    }

    public void setAreacity(String areacity) {
        this.areacity = areacity;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public List<HashMap<String, String>> getVideoSourceList() {
        return videoSourceList;
    }

    public void setVideoSourceList(List<HashMap<String, String>> videoSourceList ) {
        this.videoSourceList = videoSourceList;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public HashMap<String, List<String>> getFigures() {
        return figures;
    }

    public void setFigures(HashMap<String, List<String>> figures) {
        this.figures = figures;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
