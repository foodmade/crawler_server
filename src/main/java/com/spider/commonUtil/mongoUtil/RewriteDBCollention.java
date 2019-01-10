package com.spider.commonUtil.mongoUtil;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class RewriteDBCollention extends DBCollection {

    protected RewriteDBCollention(DB database, String name) {
        super(database, name);
    }

    /**
     * 把普通的对象写入数据库
     *
     * @param obj
     * @return
     */
    public WriteResult insert(Object obj) {
        Gson gson = new Gson();
        DBObject dbObject = (DBObject) JSON.parse(gson.toJson(obj));
        return super.insert(dbObject);
    }
}
