package com.spider.listener;


import java.lang.reflect.Field;

import com.spider.annotation.IncKey;
import com.spider.entity.mongoEntity.SeqInfo;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ReflectionUtils;

import javax.inject.Inject;

/**
 * mongo进入真正操作服务器之前会进入这儿,可以做我们想做的初始化操作
 */

public class SaveEventListener extends AbstractMongoEventListener<Object> {

    private static Logger logger = Logger.getLogger(SaveEventListener.class);
    @Inject
    private MongoTemplate mongoTemplate;

    /**
     * 前置操作
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        final Object source = event.getSource();
        if (source != null) {
            ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field);
                    // 如果字段添加了我们自定义的AutoIncKey注解
                    if (field.isAnnotationPresent(IncKey.class)) {
                        // 设置自增ID
                        field.set(source, getNextId(source.getClass().getSimpleName()));
                    }
                }
            });
        }
    }

    /**
     * 获取下一个自增ID
     * 这儿有根据表名进行区分
     * @param collName 这里代表数据库表名称
     * @return 唯一id
     */
    private Long getNextId(String collName) {
        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("seqId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SeqInfo seq = mongoTemplate.findAndModify(query, update, options, SeqInfo.class);
        return seq.getSeqId();
    }
}
