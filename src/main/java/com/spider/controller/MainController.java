package com.spider.controller;

import com.spider.annotation.BaseCheck;
import com.spider.entity.BaseResult;
import com.spider.entity.mongoEntity.Account;
import com.spider.service.dao.MainServiceDao;
import com.spider.spiderUtil.Item;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Inject
    MainServiceDao mainService;

    /**
     * 获取菜单信息
     */
    @RequestMapping("getMovieTypeInfo.do")
    @ResponseBody
    public BaseResult getMovieTypeInfo(HttpServletRequest request){
        return mainService.getMovieTypeInfo();
    }

    /**
     * 根据类型获取影片资源
     * @param videoId 分类id
     */
    @RequestMapping(value = "getMovies.do" ,method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getMovies(HttpServletRequest request, String videoId,Integer page,Integer page_size){
        return mainService.getMoviesInfo(videoId,page,page_size);
    }

    /**
     * 根据电影名称搜索
     */
    @RequestMapping(value = "getMoviesByName.do",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getMoviesByName(HttpServletRequest request, String videoName,Integer page,Integer page_size){
        return mainService.getMoviesByNameInfo(videoName,page,page_size);
    }

    /**
     * 发送邮件
     */
    @RequestMapping(value = "sendEmail.do",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult sendEmail(HttpServletRequest request,String email){
        return mainService.sendEmail(request.getSession().getId(),email);
    }

    /**
     * 点赞
     */
    @RequestMapping(value = "movieDotPraise.do",method = RequestMethod.POST)
    @ResponseBody
    @BaseCheck(beanClazz = Item.class)
    public BaseResult movieDotPraise(HttpServletRequest request, @RequestAttribute Item paramModel){
        return mainService.movieDotPraise(paramModel);
    }

    /**
     * 收藏影片
     * @param userAccessTokenBean  登录用户信息
     * @param movieId 影片唯一id
     */
    @RequestMapping(value = "collectionMovie.do",method = RequestMethod.GET)
    @ResponseBody
    @BaseCheck(needLogin = true,needCheckParameter = false)
    public BaseResult collectionMovie(HttpServletRequest request, @RequestAttribute Account userAccessTokenBean, String movieId){
        return mainService.collectionMovie(userAccessTokenBean,movieId);
    }
}
