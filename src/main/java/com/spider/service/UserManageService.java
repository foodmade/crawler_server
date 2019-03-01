package com.spider.service;

import com.alibaba.fastjson.JSON;
import com.spider.Vo.inModel.RegisterModel;
import com.spider.commonUtil.*;
import com.spider.commonUtil.RSA.RSAUtils;
import com.spider.commonUtil.config.RSAConfig;
import com.spider.entity.BaseResult;
import com.spider.entity.mongoEntity.Account;
import com.spider.entity.mongoEntity.UserDetailInfo;
import com.spider.enumUtil.ExceptionEnum;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
public class UserManageService {

    private static Logger logger = Logger.getLogger(UserManageService.class);

    @Inject
    private RedisCacheManager redisCacheManager;

    @Inject
    private MongoTemplate mongoTemplate;

    @Inject
    private RSAConfig rsaConfig;

    @Inject
    private CommonUtils commonUtils;

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
        return redisCacheManager.get(CommonUtils.createRedisMode(RedisKey.loginStatusKey(sessionId),null,Const._USER_SESSION_DB));
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
        //删除缓存的验证码
        redisCacheManager.del(CommonUtils.createRedisMode(RedisKey.registerCodeKey(sessionId),null));
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

        return code.toString().equals(cacheCode);
    }

    /**
     * 检查用户是否已经存在
     */
    public BaseResult checkUserIsExist(RegisterModel registerModel) {

        if(registerModel == null || CommonUtils.isEmpty(registerModel.getUsername())){
            return BaseResult.makeResult(ExceptionEnum.PARAMEMPTYPEROR);
        }
        if(userIsExist(registerModel.getUsername())){
            return BaseResult.makeResult(ExceptionEnum.EXISTEUSER);
        }
        return BaseResult.success(null);
    }

    public Boolean userIsExist(String userName){
        try {
            return mongoTemplate.exists(new Query(Criteria.where("userName").is(userName)),Account.class);
        } catch (Exception e) {
            logger.error("查询用户信息失败 e:"+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public BaseResult login(HttpServletRequest request,RegisterModel paramModel) {
        if(paramModel == null ||
                CommonUtils.isEmpty(paramModel.getUsername()) ||
                CommonUtils.isEmpty(paramModel.getPassword())){
            return BaseResult.makeResult(ExceptionEnum.PARAMEMPTYPEROR);
        }
        //查询用户
        Account account = getAccountByUserName(paramModel.getUsername());
        if(account == null){
            return BaseResult.makeResult(ExceptionEnum.USERINFOERR);
        }
        try {
            //解密
            String encodePwd = RSAUtils.privateDecrypt(account.getPassword(), RSAUtils.getPrivateKey(rsaConfig.getPrivate_rsa()));
            if(paramModel.getPassword().equals(encodePwd)){
                account.setPassword(null);
                //刷新缓存登录状
                commonUtils.pushUserInfoToMemory(account,request);
                return BaseResult.success(account);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return BaseResult.makeResult(ExceptionEnum.USERINFOERR);
    }

    private Account getAccountByUserName(String username) {
        if(CommonUtils.isEmpty(username)){
            return null;
        }
        Query query = new Query(Criteria
                .where("userName")
                .is(username));
//        query.fields().exclude("password");
        try {
            return mongoTemplate.findOne(query,Account.class);
        } catch (Exception e) {
            logger.error("根据userName查询用户信息失败 {login.do}");
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult isLogin(HttpServletRequest request) {
        if(request == null || request.getSession() == null){
            return BaseResult.makeResult(ExceptionEnum.REQUESTERR);
        }
        Account account = fetchUserInfoByRedis(request.getSession().getId());
        if(account == null){
            return BaseResult.makeResult(ExceptionEnum.USERLOGINEXPIRE);
        }else{
            return BaseResult.success(account);
        }
    }

    /**
     * 根据token 从redis中获取用户详情
     */
    public Account fetchUserInfoByRedis(String token){
        String val = redisCacheManager.get(CommonUtils.createRedisMode(RedisKey.loginStatusKey(token),null,Const._USER_SESSION_DB));
        if(CommonUtils.isEmpty(val)){
            return null;
        }else{
            return JSON.parseObject(val,Account.class);
        }
    }

    public BaseResult logout(HttpServletRequest request) {
        if(request == null || request.getSession() == null){
            return BaseResult.makeResult(ExceptionEnum.REQUESTERR);
        }
        try {
            redisCacheManager.del(CommonUtils.createRedisMode(RedisKey.loginStatusKey(request.getSession().getId()),null,Const._USER_SESSION_DB));
        } catch (Exception e) {
            logger.error("注销登录失败 e:"+e.getMessage());
            return BaseResult.makeResult(ExceptionEnum.SERVER_ERR);
        }
        return BaseResult.success(null);
    }
}
