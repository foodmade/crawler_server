package com.spider.commonUtil;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class ApiRequestSupport {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestSupport.class);

    public static void invokeExceptionWrapper(HttpServletResponse response, String code, String message)
            throws IOException {
        HashMap<String,Object> json = new HashMap<>();
        json.put("code",code);
        json.put("message",message);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(json));
    }
}
