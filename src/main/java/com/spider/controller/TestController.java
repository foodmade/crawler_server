package com.spider.controller;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.spider.commonUtil.MongoTable;
import com.spider.commonUtil.MongoUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
public class TestController {

    @Inject
    MongoUtils mongoUtils;

    @RequestMapping("test.do")
    @ResponseBody
    public HashMap<String,Object> test(HttpServletRequest request){
        DB db = mongoUtils.getMongoDB();

        List<DBObject> list = db.getCollection(MongoTable._TEST_TAB).find().toArray();

        HashMap<String,Object> result = new HashMap<>();
        result.put("rows",list);
        return result;
    }
}
