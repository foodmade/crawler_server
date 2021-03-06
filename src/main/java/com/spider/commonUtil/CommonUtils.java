package com.spider.commonUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.spider.annotation.SerializeSign;
import com.spider.commonUtil.config.RedisCacheConfig;
import com.spider.commonUtil.mongoUtil.MongoUtil;
import com.spider.entity.RedisModel;
import com.spider.entity.UserModel;
import com.spider.entity.mongoEntity.Account;
import com.spider.entity.mongoEntity.BaseTask;
import com.spider.spiderUtil.Item;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

@Getter
@Component
public class CommonUtils {

    @Inject
    private MongoUtil mongoUtil;
    @Inject
    private RedisCacheManager redisCacheManager;
    @Inject
    private MongoTemplate mongoTemplate;

    private static Logger logger = Logger.getLogger(CommonUtils.class);

    public static List<DBObject> transDBObject(List<Item> data) {
        List<DBObject> result = new ArrayList<>();
        if(data == null || data.size() == 0){
            return result;
        }
        for (Item item : data) {
            result.add(getDBObject(item));
        }
        return result;
    }

    public static <T> DBObject getDBObject(T bean) {
        if (bean == null) {
            return null;
        }
        DBObject obj = new BasicDBObject();
        Field[] field = bean.getClass().getDeclaredFields();
        for (Field f : field) {
            String name = f.getName();
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            SerializeSign sign = f.getAnnotation(SerializeSign.class);
            if(sign!=null && !sign.needSerialize()){
                continue;
            }
            try {
                Object oj = f.get(bean);
                obj.put(name, getValueCut(oj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;

    }

    public static Object getValueCut(Object oj){
        if (oj == null) {
            return null;
        } else if (oj instanceof Integer) {
            return Integer.parseInt(oj + "");
        } else if (oj instanceof Double) {
            return Double.parseDouble(oj + "");
        } else if (oj instanceof Float) {
            return Float.parseFloat(oj +"");
        } else if (oj instanceof Boolean) {
            return Boolean.parseBoolean(oj + "");
        } else if (oj instanceof Long) {
           return Long.parseLong(oj + "");
        } else {
            return oj;
        }
    }

    public static Object getValueDefault(Class<?> cls){
        if (cls == null) {
            return 0;
        } else if (cls.getName().equals("Integer")) {
            return 0;
        } else if (cls.getName().equals("Double")) {
            return 0.00;
        } else if (cls.getName().equals("Float")) {
            return 0.0F;
        } else if (cls.getName().equals("Boolean")) {
            return false;
        } else if (cls.getName().equals("Long")) {
            return 0L;
        } else {
            return 0;
        }
    }

    /**
     * 验证邮箱格式
     */
    public static boolean invalidEmailFormat(String email) {
        if(isEmpty(email)){
            return false;
        }
        return email.matches("^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$");
    }

    public static String randomCode() {
        return ((int) (Math.random() * 9000) +1000)+"";
    }

    public static UserModel accountTransModel(Account user) {

        UserModel userModel = new UserModel();

        userModel.setUsernick(user.getUserNick());

        return null;
    }

    public MongoUtil getMongoUtil() {
        return mongoUtil;
    }

    public static String readRequestBuff(HttpServletRequest request){
        String submitData = "";
        try {
            while (true) {
                String str = request.getReader().readLine();
                if (str == null)
                    break;
                submitData = submitData + str;
            }
        } catch (IOException e) {
            logger.error("【读取网络请求流失败 e:】"+e.getMessage());
        }
        return submitData;
    }

    /**
     * 判断字符串是否为空 null "" "null" 都返回true
     */
    public static Boolean isEmpty(String str){
        return str == null || str.equals("") || str.equals("null") || str.equals("undefined");
    }

    /**
     * 获取随机数
     */
    public static String getUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取url中的Host地址
     */
    public static String getUrlByHost(String validUrl){
        if(!isValidUrl(validUrl)){
            logger.error("输入的url不合法 url:"+validUrl);
        }
        URL url;
        try {
            url = new URL(validUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url.getHost();// 获取主机名
    }

    /**
     * 检验url是否合法
     */
    public static boolean isValidUrl(String urlString){
        URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }

        if(uri.getHost() == null){
            return false;
        }
        return uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https");
    }

    public static <T> T mapTransModel(HashMap map, Class<T> cls){
        if(map == null || map.isEmpty()){
            return null;
        }
        //获取类加载器
        T model = null;
        try {
            model = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if(model == null){
            return null;
        }
        Method[] methods = model.getClass().getMethods();
        if(methods == null || methods.length == 0){
            return null;
        }
        String key;
        Object value;
        for (Method m:methods){
            key = m.getName().toLowerCase();
            if(!key.contains("set")){
                continue;
            }
            key = key.replace("set","");
            if(CommonUtils.isEmpty(key)){
                continue;
            }
            value = map.get(key);
            if(value == null){
                continue;
            }
            //如果参数是集合类型,则需要转换为相应类型
            value = specialHandle(m.getParameterTypes()[0].getSimpleName(),value);
            try {
                m.invoke(model,value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    private static Object specialHandle(String simpleName,Object value) {
        switch (simpleName){
            case "HashMap":
                return JSON.parseObject(value+"",new TypeReference<HashMap>(){});
            case "List":
                return JSON.parseObject(value+"",new TypeReference<List>(){});
            case "Integer":
                return Integer.parseInt(value+"");
            default:
                return value;
        }
    }

    public static DBObject getFelid(){
        return new BasicDBObject()
                .append("_id",0);
    }

    public static Pattern getPatternFiled(String key){
        return Pattern.compile("^.*" + key +".*$", Pattern.CASE_INSENSITIVE);
    }

    public static Integer parseDirId(Integer videoId){
        if(videoId == null || videoId == 0){
            return null;
        }
        //取除以256的模
        return videoId & ((1<<8)-1);
    }

    public static RedisModel createRedisMode(String key,Object value,int expire){
        RedisModel redisModel = createRedisMode(key, value);
        redisModel.setExpire(expire);
        return redisModel;
    }

    public static RedisModel createRedisMode(String key,Object value,Integer dataBase){
        RedisModel redisModel = createRedisMode(key, value);
        redisModel.setDatabase(dataBase);
        return redisModel;
    }

    public static RedisModel createRedisMode(String key,Object value,Integer dataBase,int expire){
        RedisModel redisModel = createRedisMode(key, value);
        redisModel.setDatabase(dataBase);
        redisModel.setExpire(expire);
        return redisModel;
    }

    public static RedisModel createRedisMode(String key,Object value){
        RedisModel redisModel = new RedisModel();
        redisModel.setKey(key);
        redisModel.setValue(value);
        return redisModel;
    }

    /**
     * 获取http首部
     *
     * @param request
     * @return
     */

    public static HashMap<String, String> getHeader(HttpServletRequest request) {
        HashMap<String, String> map = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            String value = request.getHeader(element);
            map.put(element.toLowerCase(), value);
        }
        return map;
    }

    //首字母大写
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    public void pushUserInfoToMemory(Account account, HttpServletRequest request) {
        //缓存到redis  有效时间30分钟
        try {
            redisCacheManager.set(createRedisMode(RedisKey.loginStatusKey(request.getSession().getId()),account,Const._USER_SESSION_DB,RedisKey._TIME_MINUTE_ONE * 30));
        } catch (Exception e) {
            logger.error("加载用户信息到缓存失败 e:"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateBaseTaskStatus(Integer status) {
        try {
            mongoTemplate.updateMulti(new Query(),
                      Update.update("status", status)
                            .addToSet("maxPage", RedisCacheConfig.getRedisConfigVal("maxPage",Integer.class)),
                    BaseTask.class);
        } catch (Exception e) {
            logger.error("【m_base_task任务状态更新失败 e:】" + e.getMessage() + "");
        }
    }

}
