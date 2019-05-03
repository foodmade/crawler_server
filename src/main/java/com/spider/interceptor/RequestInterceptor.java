package com.spider.interceptor;

import com.alibaba.fastjson.JSON;
import com.spider.annotation.BaseCheck;
import com.spider.commonUtil.ApiRequestSupport;
import com.spider.commonUtil.CommonUtils;
import com.spider.commonUtil.Const;
import com.spider.commonUtil.Validator;
import com.spider.commonUtil.exception.UserExpireException;
import com.spider.entity.UserModel;
import com.spider.entity.mongoEntity.Account;
import com.spider.enumUtil.ExceptionEnum;
import com.spider.service.UserManageService;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = Logger.getLogger(RequestInterceptor.class);

    @Inject
    private UserManageService userManageService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("拦截ip:"+request.getRemoteAddr());
        logger.debug("sessionId:"+request.getSession().getId());
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                BaseCheck annotation = method.getAnnotation(BaseCheck.class);

                if(annotation == null){
                    return true;
                }

                if(request.getSession().getId() == null){
                    throw new Exception(ExceptionEnum.REQUESTERR.getMessage());
                }

                //开始根据注解参数进行前置操作
                //判断当前访问接口是否需要权限检查
                boolean needLogin = annotation.needLogin();
                if(needLogin){
                    //开始检查session
                    String sessionId = request.getSession().getId();
                    //从redis检查session是否存在
                    if(!userManageService.checkUserIsLogin(sessionId)){
                        logger.error("用户信息过期,请重新登陆,sessionId:【"+sessionId+"】");
                        throw new UserExpireException(ExceptionEnum.USERLOGINEXPIRE);
                    }
                    //设置用户信息至本机缓存
                    if(!setUserToAttribute(request)){
                        logger.error("设置session到内存失败");
                        throw new Exception(ExceptionEnum.SERVER_ERR.getMessage());
                    }
                }

                //检查是否需要参数实例化
                boolean needCheckParameter = annotation.needCheckParameter();
                if(needCheckParameter){
                    // 参数字符串
                    String jsonString = CommonUtils.readRequestBuff(request);
                    Object model;
                    try {
                        model = getModel(jsonString, annotation.beanClazz());
                    } catch (Exception e) {
                        throw new InstantiationException(e.getMessage());
                    }
                    request.setAttribute("paramModel", model);
                }
            }
        } catch (UserExpireException e) {
            logger.info("【拦截器发生异常,异常类型:{UserExpireException} 】"+e.getMessage());
            ApiRequestSupport.invokeExceptionWrapper(response,e.getCode(),e.getMessage());
            return false;
        }catch (Exception e) {
            logger.error("【Exception 拦截器发生异常：】"+e.getMessage());
            ApiRequestSupport.invokeExceptionWrapper(response, ExceptionEnum.SERVER_ERR.getCode(),
                    ExceptionEnum.SERVER_ERR.getMessage());
            return false;
        }

        return true;
    }

    private <T> T getModel(String json, Class<T> clazz) throws Exception {
        Validator.validate(JSON.parseObject(json, clazz));
        return JSON.parseObject(json, clazz);
    }

    private Boolean setUserToAttribute(HttpServletRequest request) throws Exception {

        String sessionId = request.getSession().getId();

        String userInfo = userManageService.getUserInfoByCache(sessionId);

        if(userInfo == null){
            return false;
        }

        try {
            Account user = JSON.parseObject(userInfo, Account.class);
            if(user == null){
                return false;
            }

            request.setAttribute(Const._USER_SESSION_KEY,user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(ExceptionEnum.SERVER_ERR.getMessage());
        }
        return true;
    }
}
