package com.spider.commonUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.spider.spiderUtil.Item;
import org.apache.log4j.Logger;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class CommonUtils {

    @Inject
    private MongoUtils mongoUtils;

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
            try {
                Object oj = f.get(bean);
                if (oj == null) {
                    obj.put(name, "");
                } else if (oj instanceof Integer) {
                    int value = (Integer) oj;
                    obj.put(name, value);
                } else if (oj instanceof Double) {
                    Double value = (Double) oj;
                    obj.put(name, value);
                } else if (oj instanceof Float) {
                    Float value = (Float) oj;
                    obj.put(name, value);
                } else if (oj instanceof Boolean) {
                    Boolean value = (Boolean) oj;
                    obj.put(name, value);
                } else if (oj instanceof Long) {
                    Long value = (Long) oj;
                    obj.put(name, value);
                } else {
                    obj.put(name, oj);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return obj;

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
        return Math.random()*9000+1000+"";
    }

    public MongoUtils getMongoUtils() {
        return mongoUtils;
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
        URI uri = null;
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

}
