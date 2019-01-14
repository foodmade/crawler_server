package com.spider.controller;

import com.spider.Vo.inModel.RegisterModel;
import com.spider.annotation.BaseCheck;
import com.spider.entity.BaseResult;
import com.spider.service.UserManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Inject
    private UserManageService userManageService;

    /**
     * 注册用户
     */
    @RequestMapping(value = "registerAccount.do",method = RequestMethod.POST)
    @ResponseBody
    @BaseCheck(needLogin = false,beanClazz = RegisterModel.class)
    public BaseResult registerAccount(HttpServletRequest request, @RequestAttribute RegisterModel paramModel){
        return userManageService.registerAccount(request, paramModel);
    }

    /**
     * 检查验证码是否正确
     */
    @RequestMapping(value = "invalidEmailCode.do",method = RequestMethod.POST)
    @ResponseBody
    @BaseCheck(needLogin = false,beanClazz = RegisterModel.class)
    public BaseResult invalidEmailCode(HttpServletRequest request,@RequestAttribute RegisterModel paramModel){
        return userManageService.checkEmailCode(request.getSession().getId(),paramModel.getCode());
    }

    /**
     * 检查用户名是否已经存在
     */
    @RequestMapping(value = "checkUserNameExist.do",method = RequestMethod.POST)
    @ResponseBody
    @BaseCheck(needLogin = false,beanClazz = RegisterModel.class)
    public BaseResult checkUserNameExist(HttpServletRequest request,@RequestAttribute RegisterModel paramModel){
        return userManageService.checkUserIsExist(paramModel);
    }

    /**
     * 登录
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    @BaseCheck(needLogin = false,beanClazz = RegisterModel.class)
    public BaseResult login(HttpServletRequest request,@RequestAttribute RegisterModel paramModel){
        return userManageService.login(request,paramModel);
    }

    /**
     * 检查是否登录
     */
    @RequestMapping(value = "isLogin.do",method = RequestMethod.POST)
    @ResponseBody
    @BaseCheck(needLogin = false,beanClazz = RegisterModel.class)
    public BaseResult isLogin(HttpServletRequest request){
        return userManageService.isLogin(request);
    }

    /**
     * 注销登录
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    @BaseCheck()
    public BaseResult logout(HttpServletRequest request){
        return userManageService.logout(request);
    }
}
