package com.spider.commonUtil;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.net.UnknownHostException;
import java.util.Properties;

@Component
public class MongoUtils {

    private static Logger logger = org.apache.log4j.Logger.getLogger(MongoUtils.class);

    @Inject
    Properties mongoProperties;

    private MongoClient mongoClient;

    public DB getMongoDB() {
        try {
            if (mongoClient == null) {
                mongoClient = new MongoClient(new ServerAddress(mongoProperties.getProperty("mongo.host"),
                        Integer.parseInt(mongoProperties.getProperty("mongo.port"))));
            }
            DB db = mongoClient.getDB(mongoProperties.getProperty("mongo.dataName"));
            boolean auth = db.authenticate(mongoProperties.getProperty("mongo.user"),
                    mongoProperties.getProperty("mongo.password").toCharArray());
            if (auth) {
                return db;
            } else {
                logger.error("Mongo_Jedi连接数据库失败!,请检查hosts,username,password是否正确！");
            }
        } catch (UnknownHostException e) {
            logger.error("e45: " + e.getMessage());
        }
        return null;
    }

}
