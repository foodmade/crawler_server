package com.spider.commonUtil.mongoUtil;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Properties;

@Component
public class MongoUtil {

    private static Logger logger = org.apache.log4j.Logger.getLogger(MongoUtil.class);

    @Inject
    private Properties mongoProperties;

    private MongoClient mongoClient;

    public DB getMongoDB() {
        try {
            if (mongoClient == null) {
                MongoCredential credential = MongoCredential.createCredential(mongoProperties.getProperty("mongo.user"),
                        mongoProperties.getProperty("mongo.dataName"), mongoProperties.getProperty("mongo.password").toCharArray());
                ServerAddress addr = new ServerAddress(mongoProperties.getProperty("mongo.host"),
                        Integer.parseInt(mongoProperties.getProperty("mongo.port")));
                mongoClient = new MongoClient(addr, Collections.singletonList(credential));
            }
            return mongoClient.getDB(mongoProperties.getProperty("mongo.dataName"));
        } catch (Exception e) {
            logger.error("e45: " + e.getMessage());
        }
        return null;
    }

}
