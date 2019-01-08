package com.spider.taskPool;

import java.io.Serializable;
import java.util.HashMap;

public class TaskData implements Serializable {

    /**
     * 爬取目标网站
     */
    private String url;
    /**
     * 子任务id
     */
    private String task_id;
    /**
     * 大任务id
     */
    private String query_id;
    /**
     * 任务状态
     */
    private Integer status = 0;
    /**
     * 重试次数
     */
    private Integer retry = 0;
    /**
     * 任务获取时间戳
     */
    private Long timestamp;
    /**
     * 任务类型
     */
    private String task_type;
    /**
     * 爬取页码
     */
    private Integer page;
    /**
     * 类目id
     */
    private Integer parentid;
    /**
     * 类目映射表
     */
    private HashMap<String,String> catemapper;
    /**
     * 资源Id
     */
    private String videoid;
    /**
     * 任务描述
     */
    private String desc;
    /**
     * 子任务集合
     */
    private HashMap<String,String> childurl;
    /**
     * 任务所处阶段
     */
    private Integer stage;
    /**
     * 任务总阶段
     */
    private Integer totalstage;
    /**
     * 目标网站类目id
     */
    private String sourcecateid;
    /**
     * 本地类目子id  用于映射
     */
    private Integer cateid;
    /**
     *  目标源网站和本地类目Id的映射表
     */
    private HashMap<String,Integer> childCateInfo = new HashMap<>();

    public HashMap<String, Integer> getChildCateInfo() {
        return childCateInfo;
    }

    public void setChildCateInfo(HashMap<String, Integer> childCateInfo) {
        this.childCateInfo = childCateInfo;
    }

    public Integer getCateid() {
        return cateid;
    }

    public void setCateid(Integer cateid) {
        this.cateid = cateid;
    }

    public String getSourcecateid() {
        return sourcecateid;
    }

    public void setSourcecateid(String sourcecateid) {
        this.sourcecateid = sourcecateid;
    }

    public Integer getTotalstage() {
        return totalstage;
    }

    public void setTotalstage(Integer totalstage) {
        this.totalstage = totalstage;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public HashMap<String, String> getChildurl() {
        return childurl;
    }

    public void setChildurl(HashMap<String, String> childurl) {
        this.childurl = childurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public HashMap<String, String> getCatemapper() {
        return catemapper;
    }

    public void setCatemapper(HashMap<String, String> catemapper) {
        this.catemapper = catemapper;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getQuery_id() {
        return query_id;
    }

    public void setQuery_id(String query_id) {
        this.query_id = query_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskData)) return false;

        TaskData taskData = (TaskData) o;

        if (!task_id.equals(taskData.getTask_id())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return task_id.hashCode();
    }
}
