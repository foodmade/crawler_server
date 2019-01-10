package com.spider;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.spider.Vo.inModel.RegisterModel;
import com.spider.commonUtil.*;
import com.spider.commonUtil.mongoUtil.MongoTable;
import com.spider.commonUtil.mongoUtil.MongoUtils;
import com.spider.commonUtil.mongoUtil.RewriteDBCollention;
import com.spider.entity.mongoEntity.SeqInfo;
import org.junit.Test;

import javax.inject.Inject;


public class ClassTest extends BaseTest{

    @Inject
    RedisCacheManager redisCacheManager;
    @Inject
    EmailUtil emailUtil;
    @Inject
    MongoUtils mongoUtils;

    public static void main(String[] args) throws Exception {
        RegisterModel registerModel = new RegisterModel();

        System.out.println(JSON.toJSONString(CommonUtils.getDBObject(registerModel)));

    }

    @Test
    public void testRedis() throws Exception {
        emailUtil.sendMail("2210465185@qq.com","test tile","you code is 3333");
    }

    @Test
    public void testLinstener(){
        SeqInfo seqInfo = new SeqInfo();
        seqInfo.setCollName("initName");
        seqInfo.setSeqId(1L);
        seqInfo.setId("dsak");
        DBCollection dbCollention =mongoUtils.getMongoDB().getCollection(MongoTable._SEQ_INFO);

        DBObject dbObject = CommonUtils.getDBObject(seqInfo);

        dbCollention.insert(dbObject);
    }

}
