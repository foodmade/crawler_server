package com.spider.controller;

import com.spider.commonUtil.SpiderTypeConst;
import com.spider.service.GeneraCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * 提供给爬虫调用的api
 */
@Controller
public class GeneralCrawlerController {

    @Autowired
    private GeneraCrawlerService generaCrawlerService;

    /**
     * 获取任务 <Gvideo影院>
     */
    @RequestMapping("getGVideoTaskResult.do")
    @ResponseBody
    public Object getMacroTaskResult(HttpServletRequest request){
        return generaCrawlerService.dispatchCrawlerTask(request,SpiderTypeConst.GVIDEO_TYPE);
    }

    /**
     * 提交任务  <Gvideo影院>
     */
    @RequestMapping("commitGVideoTaskResult.do")
    @ResponseBody
    public void commitMacroTaskResult(HttpServletRequest request){
        generaCrawlerService.dispatchCommitTaskResult(request, SpiderTypeConst.GVIDEO_TYPE);
    }

    /**
     * 获取任务 <HaiWai影院>
     */
    @RequestMapping("getHaiWaiTaskResult.do")
    @ResponseBody
    public Object getHaiWaiTaskResult(HttpServletRequest request){
        return generaCrawlerService.dispatchCrawlerTask(request,SpiderTypeConst.HAIWAI_TYPE);
    }

    /**
     * 提交任务  <HaiWai影院>
     */
    @RequestMapping("commitHaiWaiTaskResult.do")
    @ResponseBody
    public void commitHaiWaiTaskResult(HttpServletRequest request){
        generaCrawlerService.dispatchCommitTaskResult(request, SpiderTypeConst.HAIWAI_TYPE);
    }

    /**
     * 爬虫状态初始化
     */
    @RequestMapping("initSpiderState.do")
    @ResponseBody
    public void initSpiderState(HttpServletRequest request){
        generaCrawlerService.initSpiderHostMap();
    }

    /**
     * 获取任务 <YunPan影院>
     */
    @RequestMapping("getYunPanTaskResult.do")
    @ResponseBody
    public Object getYunPanTaskResult(HttpServletRequest request){
        return generaCrawlerService.dispatchCrawlerTask(request,SpiderTypeConst.YUNPAN_TYPE);
    }

    /**
     * 提交任务  <YunPan影院>
     */
    @RequestMapping("commitYunPanTaskResult.do")
    @ResponseBody
    public void commitYunPanTaskResult(HttpServletRequest request){
        generaCrawlerService.dispatchCommitTaskResult(request, SpiderTypeConst.YUNPAN_TYPE);
    }

    /**
     * 获取任务 <DongJing影院>
     */
    @RequestMapping("getDongJingTaskResult.do")
    @ResponseBody
    public Object getDongJingTaskResult(HttpServletRequest request){
        return generaCrawlerService.dispatchCrawlerTask(request,SpiderTypeConst.DONGJING_TYPE);
    }

    /**
     * 提交任务  <DongJing影院>
     */
    @RequestMapping("commitDongJingTaskResult.do")
    @ResponseBody
    public void commitDongJingTaskResult(HttpServletRequest request){
        generaCrawlerService.dispatchCommitTaskResult(request, SpiderTypeConst.DONGJING_TYPE);
    }


}
