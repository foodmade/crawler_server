package com.spider.controller;

import com.alibaba.fastjson.JSON;
import com.spider.annotation.BaseCheck;
import com.spider.entity.BaseResult;
import com.spider.entity.UserModel;
import com.spider.entity.mongoEntity.Account;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class TestController {

    @Inject
    MongoTemplate mongoTemplate;

    @RequestMapping("test.do")
    @ResponseBody
    public HashMap<String,Object> test(HttpServletRequest request){

        HashMap<String,Object> result = new HashMap<>();
        result.put("rows",mongoTemplate.findAll(Account.class));
        return result;
    }

    @RequestMapping(value = "testFilter.do",method = RequestMethod.POST)
    @ResponseBody
    @BaseCheck(needLogin = false)
    public BaseResult testFilter(HttpServletRequest request,@RequestAttribute UserModel paramModel){
        System.out.println(JSON.toJSONString(paramModel));
        return new BaseResult();
    }

    @RequestMapping(value = "testMongoJiLian.do",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult testMongoJiLian(){
        Account account = new Account();
        account.setPassword("111");
        account.setPermissionLevel(1);
        account.setUserName("chen");
        account.setUserNick("xiaominmg");
        mongoTemplate.insert(account);
        return new BaseResult();
    }
}
