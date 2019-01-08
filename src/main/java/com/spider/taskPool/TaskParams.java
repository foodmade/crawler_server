package com.spider.taskPool;

import java.util.HashMap;

public class TaskParams {

    private String url;

    private Integer maxPage;

    private String task_type;

    private String desc;

    private Integer status;

    private Integer stage;

    private String videoId;

    private Integer parentId;

    private HashMap<String,Integer> childCateInfo;

    public HashMap<String, Integer> getChildCateInfo() {
        return childCateInfo;
    }

    public void setChildCateInfo(HashMap<String, Integer> childCateInfo) {
        this.childCateInfo = childCateInfo;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    private HashMap<String,String> childUrl;

    public Integer getStage() {
        return stage;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public HashMap<String, String> getChildUrl() {
        return childUrl;
    }

    public void setChildUrl(HashMap<String, String> childUrl) {
        this.childUrl = childUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
