package com.spider.commonUtil.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.spider.commonUtil.CommonUtils;
import com.spider.commonUtil.Const;
import com.spider.commonUtil.RedisCacheManager;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;

@Component
public class RedisCacheConfig {

    private static Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);

    @Getter
    private static HashMap<String,Object> redisCache = new HashMap<>();

    public static final String config_key = "config";

    @Inject
    RedisCacheManager redisCacheManager;

    public void loadRedisConfig(){
        long start = System.currentTimeMillis();
        String value = redisCacheManager.opsGet(CommonUtils.createRedisMode(config_key,null, Const._CONFIG_DB));
        if(value == null){
            return;
        }
        redisCache = JSON.parseObject(value,new TypeReference<HashMap<String,Object>>(){});

        logger.info("【load Redis Config Success OverTime: {}】",(System.currentTimeMillis()-start));
    }

    public static <T> T getRedisConfigVal(String key,Class<?> cls){
        Object valObj = getRedisCache().get(key);
        return valObj == null ? (T)CommonUtils.getValueDefault(cls) : (T)JSON.parseObject(valObj + "",cls);
    }
}
