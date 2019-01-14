package com.spider;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.spider.Vo.inModel.RegisterModel;
import com.spider.commonUtil.*;
import com.spider.commonUtil.RSA.RSAUtils;
import com.spider.commonUtil.config.RSAConfig;
import com.spider.commonUtil.mongoUtil.MongoTable;
import com.spider.commonUtil.mongoUtil.MongoUtils;
import com.spider.commonUtil.mongoUtil.RewriteDBCollention;
import com.spider.entity.mongoEntity.Account;
import com.spider.entity.mongoEntity.SeqInfo;
import com.spider.entity.mongoEntity.UserDetailInfo;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;


public class ClassTest extends BaseTest{

    @Inject
    RedisCacheManager redisCacheManager;
    @Inject
    EmailUtil emailUtil;
    @Inject
    MongoUtils mongoUtils;
    @Inject
    MongoTemplate mongoTemplate;
    @Inject
    RSAConfig rsaConfig;

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
    public void testRSA() throws InvalidKeySpecException, NoSuchAlgorithmException {
   /*     Map<String, String> keyMap = RSAUtils.createKeys(1024);
        String  publicKey = keyMap.get("publicKey");
        String  privateKey = keyMap.get("privateKey");*/
 /*       System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);*/

        String str = "chen19960119";
        System.out.println("\r明文：\r\n" + str);
        String encodedData = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(rsaConfig.getPub_rsa()));
        System.out.println("密文：\r\n" + encodedData);
        String decodedData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(rsaConfig.getPrivate_rsa()));
        System.out.println("解密后文字: \r\n" + decodedData);

    }

    @Test
    public void testLinstener(){
        Account account = new Account();
        account.setPassword("111");
        account.setPermissionLevel(1);
        account.setUserName("chen");
        account.setUserNick("xiaominmg");
        UserDetailInfo userDetailInfo = new UserDetailInfo();
        userDetailInfo.setCordId("511623199601197670");
        account.setUserDetailInfo(userDetailInfo);
        mongoTemplate.insert(account);
    }

}
