package com.spider;

import com.alibaba.fastjson.JSON;
import com.spider.Vo.inModel.RegisterModel;
import com.spider.commonUtil.*;
import com.spider.commonUtil.RSA.RSAUtils;
import com.spider.commonUtil.config.RSAConfig;
import com.spider.commonUtil.mongoUtil.MongoUtil;
import com.spider.entity.mongoEntity.SeqInfo;
import com.spider.spiderUtil.Item;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;


public class ClassTest extends BaseTest{

    @Inject
    RedisCacheManager redisCacheManager;
    @Inject
    EmailUtil emailUtil;
    @Inject
    MongoUtil mongoUtil;
    @Inject
    MongoTemplate mongoTemplate;
    @Inject
    RSAConfig rsaConfig;

    public static void main(String[] args) throws Exception {
        RegisterModel registerModel = new RegisterModel();

        Optional<RegisterModel> optModel = Optional.of(registerModel);

        Boolean b = optModel.map(u -> (u.getUsername()==null))
                .orElse(true);

        System.out.println(b);

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

        List<Item> list = mongoTemplate.find(new Query(Criteria.where("videoName").is("人生2012")),Item.class);
        System.out.println(JSON.toJSONString(list));
    }

}
