package com.spider;

import com.spider.commonUtil.EmailUtil;
import com.spider.commonUtil.RedisCacheManager;
import com.spider.entity.RedisModel;
import org.junit.Test;

import javax.inject.Inject;


public class ClassTest extends BaseTest{

    @Inject
    RedisCacheManager redisCacheManager;
    @Inject
    EmailUtil emailUtil;

    public static void main(String[] args) throws Exception {

    }

    @Test
    public void testRedis() throws Exception {
        emailUtil.sendMail("2210465185@qq.com","test tile","you code is 3333");
    }

}
