package com.spider.service;

import org.apache.log4j.Logger;

import java.util.HashMap;

public class TaskMakerService {

    private static Logger logger = Logger.getLogger(TaskMakerService.class);
    private HashMap<String, Object> taskMakerMapper;

    public HashMap<String, Object> getTaskMakerMapper() {
        return taskMakerMapper;
    }

    public void setTaskMakerMapper(HashMap<String, Object> taskMakerMapper) {
        this.taskMakerMapper = taskMakerMapper;
    }

}
