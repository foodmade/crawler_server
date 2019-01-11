package com.spider.service;

import com.mongodb.DBObject;
import com.spider.Vo.inModel.RegisterModel;
import com.spider.commonUtil.*;
import com.spider.commonUtil.mongoUtil.MongoTable;
import com.spider.commonUtil.mongoUtil.MongoUtils;
import com.spider.entity.BaseResult;
import com.spider.entity.mongoEntity.Account;
import com.spider.entity.mongoEntity.UserDetailInfo;
import com.spider.enumUtil.ExceptionEnum;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserManageService {

    private static Logger logger = Logger.getLogger(UserManageService.class);

    @Inject
    private RedisCacheManager redisCacheManager;

    @Inject
    private MongoUtils mongoUtils;

    @Inject
    private MongoTemplate mongoTemplate;

    /**
     * 检查用户是否处于登陆状态
     */
    public Boolean checkUserIsLogin(String sessionId){
        if(CommonUtils.isEmpty(sessionId)){
            return false;
        }

        String val = getUserInfoByCache(sessionId);
        return val != null;
    }

    /**
     * 从缓存获取用户信息
     */
    public String getUserInfoByCache(String sessionId){
        return redisCacheManager.get(CommonUtils.createRedisMode(sessionId,null,Const._USER_SESSION_DB));
    }

    /**
     * 检查邮件验证码
     */
    public BaseResult checkEmailCode(String sessionId, Integer code){
        if(CommonUtils.isEmpty(sessionId)){
            return BaseResult.makeResult(ExceptionEnum.REQUESTERR);
        }

        if(code == null){
            return BaseResult.makeResult(ExceptionEnum.PARAMEMPTYPEROR);
        }

        if(!invalidCode(sessionId, code)){
            return BaseResult.makeResult(ExceptionEnum.AUTHCODEERR);
        }
        return BaseResult.success(null);
    }

    public BaseResult registerAccount(HttpServletRequest request, RegisterModel registerModel){
        if(request == null || CommonUtils.isEmpty(request.getSession().getId())){
            return BaseResult.makeResult(ExceptionEnum.REQUESTERR);
        }
        if(registerModel == null){
            return BaseResult.makeResult(ExceptionEnum.PARAMEMPTYPEROR);
        }
        String sessionId = request.getSession().getId();
        //检查验证码是否正确
        if(!invalidCode(sessionId,registerModel.getCode())){
            return BaseResult.makeResult(ExceptionEnum.AUTHCODEERR);
        }
        //检查2次密码是否一致
        if(!registerModel.getConfirmPassword().equals(registerModel.getPassword())){
            return BaseResult.makeResult(ExceptionEnum.CONFIRMPASSWORDERR);
        }
        //write account data to DB
        if(writeAccountInfo(registerModel)){
            return BaseResult.success(null);
        }else{
            return BaseResult.makeResult(ExceptionEnum.SERVER_ERR);
        }
    }

    private Boolean writeAccountInfo(RegisterModel registerModel) {
        Account account = getAccount(registerModel);
        if(account == null){
            return false;
        }
        try {
            mongoTemplate.insert(account);
        } catch (Exception e) {
            logger.error("写入注册用户信息失败 e:"+e.getMessage());
            return false;
        }
        return true;
    }

    private Account getAccount(RegisterModel registerModel) {
        Account account = new Account();
        account.setUserDetailInfo(new UserDetailInfo());
        account.setUserNick(registerModel.getUsernick());
        account.setUserName(registerModel.getUsername());
        account.setPassword(registerModel.getPassword());

        return account;
    }

    /**
     * code invalid
     */
    public Boolean invalidCode(String sessionId, Integer code){
        if(code == null){
            return false;
        }
        //获取验证码
        String cacheCode = null;
        try {
            cacheCode = redisCacheManager.get(CommonUtils.createRedisMode(RedisKey.registerCodeKey(sessionId),null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return !CommonUtils.isEmpty(cacheCode) || code.toString().equals(cacheCode);
    }
}
