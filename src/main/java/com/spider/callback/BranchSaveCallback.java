package com.spider.callback;

import com.spider.annotation.BranchSave;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class BranchSaveCallback implements ReflectionUtils.FieldCallback {

    private Object source;
    private MongoTemplate mongoTemplate;

    public BranchSaveCallback(Object source, MongoTemplate mongoTemplate) {
        super();
        this.source = source;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if(field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(BranchSave.class)){
            final Object fieldValue = field.get(source);

            if(fieldValue != null){
                FieldCallback fc = new FieldCallback();
                ReflectionUtils.doWithFields(fieldValue.getClass(), fc);

                mongoTemplate.save(fieldValue);
            }
        }
    }
}
