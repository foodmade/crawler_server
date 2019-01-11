package com.spider.listener;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.spider.annotation.Encrypted;
import com.spider.annotation.IncKey;
import com.spider.commonUtil.CommonUtils;
import com.spider.commonUtil.RSA.RSAUtils;
import com.spider.commonUtil.config.RSAConfig;
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

    @Inject
    RSAConfig rsaConfig;

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
                    // 如果字段添加了自定义的Encrypted注解 则代表文本需要加密
                    if(field.isAnnotationPresent(Encrypted.class)){
                        //只支持对String数据类型加密
                        if(String.class.getName().equals(field.getType().getName())){
                            try {
                                //待加密文本
                                Object text ;
                                if((text = field.get(source)) != null){
                                    //设置加密后的文本
                                    field.set(source,RSAUtils.publicEncrypt(text.toString(),RSAUtils.getPublicKey(rsaConfig.getPub_rsa())));
                                }
                            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                                logger.error("文本加密出现异常 e:" + e.getMessage());
                                e.printStackTrace();
                            }
                        }else{
                            logger.warn("不支持加密的数据类型:"+field.getType().getName());
                        }
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
