package com.spider.listener;

import com.mongodb.DBObject;
import com.spider.annotation.AutoIncKey;
import com.spider.commonUtil.mongoUtil.MongoTable;
import com.spider.commonUtil.mongoUtil.MongoUtils;
import com.spider.entity.mongoEntity.SeqInfo;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * mongo保存监听操作,这儿用于创建自增ID
 */

public class SaveEventListener extends AbstractMongoEventListener<Object> {

    private static Logger logger = Logger.getLogger(SaveEventListener.class);
    @Inject
    private MongoUtils mongoUtils;


    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
//        ReflectionUtils.doWithFields(source.getClass());
    }
}
