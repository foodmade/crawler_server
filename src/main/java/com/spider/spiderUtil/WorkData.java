package com.spider.spiderUtil;

import com.spider.taskPool.TaskData;

import java.util.List;

public class WorkData {

    private TaskData taskData;

    private List<Item> items;

    private Integer retcode;

    public Integer getRetcode() {
        return retcode;
    }

    public void setRetcode(Integer retcode) {
        this.retcode = retcode;
    }

    public TaskData getTaskData() {
        return taskData;
    }

    public void setTaskData(TaskData taskData) {
        this.taskData = taskData;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
