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
import com.spider.entity.mongoEntity.Account;
import com.spider.entity.mongoEntity.SeqInfo;
import com.spider.entity.mongoEntity.UserDetailInfo;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;
import java.util.List;


public class ClassTest extends BaseTest{

    @Inject
    RedisCacheManager redisCacheManager;
    @Inject
    EmailUtil emailUtil;
    @Inject
    MongoUtils mongoUtils;
    @Inject
    MongoTemplate mongoTemplate;

    public static void main(String[] args) throws Exception {
        RegisterModel registerModel = new RegisterModel();

        System.out.println(JSON.toJSONString(CommonUtils.getDBObject(registerModel)));

    }

    @Test
    public void testRedis() throws Exception {
//        emailUtil.sendMail("2210465185@qq.com","test tile","you code is 3333");
        List<SeqInfo> items = mongoTemplate.findAll(SeqInfo.class);
        System.out.println(JSON.toJSONString(items));
    }

    @Test
    public void testLinstener(){
        Account account = new Account();
        account.setPassword("111");
        account.setPermissionLevel("1");
        account.setUserName("chen");
        account.setUserNick("xiaominmg");
        UserDetailInfo userDetailInfo = new UserDetailInfo();
        userDetailInfo.setCord("511623199601197670");
        account.setUserDetailInfo(userDetailInfo);
        mongoTemplate.insert(account);
    }

}
