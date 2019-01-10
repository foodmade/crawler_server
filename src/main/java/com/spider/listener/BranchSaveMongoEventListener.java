package com.spider.listener;

import com.spider.callback.BranchSaveCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;

import javax.inject.Inject;

/**
 * mongo实体类存在内嵌对象时的处理策略
 */
public class BranchSaveMongoEventListener extends AbstractMongoEventListener<Object> {

    @Inject
    private MongoTemplate mongoTemplate;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        final Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), new BranchSaveCallback(source, mongoTemplate));
        super.onBeforeConvert(event);
    }
}
