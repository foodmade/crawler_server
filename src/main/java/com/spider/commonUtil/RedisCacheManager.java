package com.spider.commonUtil;

import com.spider.entity.RedisModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.*;

@Service
@Scope("singleton")
public class RedisCacheManager implements InitializingBean {
    private static Logger logger = Logger.getLogger(RedisCacheManager.class);

    @Inject
    private JedisConnectionFactory jedisConnectionFactory;

    private JedisPool jedisPool;

    public void set(RedisModel redisModel){
        if(!paramValid(redisModel)){
            logger.error("redisModel invalid SET JSON:【"+redisModel.toString()+"】");
            return;
        }
        try {
            opsSet(redisModel);
        } catch (Exception e) {
            logger.error("do set key err:"+e.getMessage());
        }
    }

    public String get(RedisModel redisModel){
        if(!paramValid(redisModel)){
            logger.error("redisModel invalid GET JSON:【"+redisModel.toString()+"】");
            return null;
        }
        return opsGet(redisModel);
    }

    public Boolean del(RedisModel redisModel){
        if(!paramValid(redisModel)){
            logger.error("redisModel invalid DEL JSON:【"+redisModel.toString()+"】");
            return null;
        }
        return opsDel(redisModel);
    }

    private Boolean opsDel(RedisModel redisModel) {
        if(jedisPool == null){
            afterPropertiesSet();
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(redisModel.getKey());
        }catch (Exception e){
            logger.error("redis execute del fail e:"+e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return true;
    }

    public Jedis getJedis(){
        return jedisPool.getResource();
    }

    public void closeJedis(Jedis jedis){
        jedis.close();
    }

    private String opsGet(RedisModel redisModel){
        if(jedisPool == null){
            afterPropertiesSet();
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(redisModel.getDatabase());
            return jedis.get(redisModel.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null)
                closeJedis(jedis);
        }
        return null;
    }

    private void opsSet(RedisModel redisModel){
        if(jedisPool == null){
            afterPropertiesSet();
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(redisModel.getDatabase());
            //识别类型
            Class cls = redisModel.getValue().getClass();
            if (cls.equals(String.class)) {
                opsForString(redisModel, jedis);
            } else if (cls.equals(List.class)) {
                opsForList(redisModel, jedis);
            } else if (cls.equals(HashMap.class) || cls.equals(Map.class)) {
                opsForHashMap(redisModel, jedis);
            } else if (cls.equals(HashSet.class) || cls.equals(Set.class)) {
                opsForHashSet(redisModel, jedis);
            } else {
                opsForString(redisModel, jedis);
            }
        } catch (Exception e) {
            logger.error("操作redis时,发生异常 e:" + e.getMessage());
        }finally {
            if(jedis != null)
                jedis.close();
        }

    }

    /**
     * 字符串操作
     * @param redisModel
     */
    private void opsForString(RedisModel redisModel,Jedis jedis) throws Exception {
        int expires = redisModel.getExpire();
        if(expires < 0){
            throw new Exception("expire不能为负数");
        }
        try {
            if(expires == 0){
                jedis.set(redisModel.getKey(),redisModel.getValue().toString());
            }else{
                jedis.setex(redisModel.getKey(),expires,redisModel.getValue().toString());
            }
        } catch (Exception e) {
            logger.error("执行setString操作时出现异常 e:"+e.getMessage());
        }
    }

    /**
     * HashMap集合操作
     * @param redisModel
     */
    private void opsForHashMap(RedisModel redisModel,Jedis jedis){

    }

    /**
     * HashSet集合操作
     * @param redisModel
     */
    private void opsForHashSet(RedisModel redisModel,Jedis jedis){

    }

    /**
     * list集合操作
     * @param redisModel
     */
    private void opsForList(RedisModel redisModel,Jedis jedis){

    }

    private Boolean paramValid(RedisModel redisModel){
        if(redisModel == null){
            return false;
        }
        if(redisModel.getValue() == null){
            redisModel.setValue(new StringBuilder());
        }
        return !CommonUtils.isEmpty(redisModel.getKey());
    }


    @Override
    public void afterPropertiesSet() {
        Field poolField = ReflectionUtils.findField(JedisConnectionFactory.class, "pool");
        ReflectionUtils.makeAccessible(poolField);
        this.jedisPool = (JedisPool) ReflectionUtils.getField(poolField, jedisConnectionFactory);
        logger.info("fetch redis connection success");
    }
}
