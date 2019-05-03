package com.spider.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.spider.annotation.Encrypted;
import com.spider.annotation.IncKey;
import com.spider.commonUtil.CommonUtils;
import com.spider.commonUtil.RSA.RSAUtils;
import com.spider.commonUtil.config.RSAConfig;
import com.spider.listener.Inckeystrategy.AbsStrategy;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;

import javax.inject.Inject;

/**
 * mongo进入真正操作服务器之前会进入这儿,可以做我们想做的初始化操作
 */

public class SaveEventListener extends AbstractMongoEventListener<Object> {

    private static Logger logger = Logger.getLogger(SaveEventListener.class);
    @Inject
    private RSAConfig rsaConfig;

    @Inject
    private CommonUtils commonUtils;

    /**
     * 前置操作
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        final Object source = event.getSource();
        if (source != null) {
            ReflectionUtils.doWithFields(source.getClass(), field -> {
                ReflectionUtils.makeAccessible(field);
                // 如果字段添加了我们自定义的AutoIncKey注解
                if (field.isAnnotationPresent(IncKey.class)) {
                    //获取生成策略
                    IncKey incKey = field.getAnnotation(IncKey.class);
                    // 设置自增ID
                    field.set(source,genId(incKey,source));
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
            });
        }
    }


    private Object genId(IncKey incKey,Object source) {
        try {
            String collection = source.getClass().getAnnotation(Document.class).collection();
            Constructor<? extends AbsStrategy> cons =
                    incKey.strategyCls().getConstructor(CommonUtils.class,String.class);
            AbsStrategy strategy = cons.newInstance(commonUtils,collection);
            return strategy.doGenId();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("执行id策略器的时候发生异常 e:"+e.getMessage());
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
