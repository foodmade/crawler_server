package com.spider.spiderUtil;
import com.spider.commonUtil.CommonUtils;
import com.spider.enumUtil.SpiderTypeEnum;
import com.spider.service.TaskMakerService;
import com.spider.taskMaker.AbstractGeneraSpiderTaskMaker;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.Properties;

@Service
public class GeneralSpiderTaskFactoryService {

    @Inject
    private Properties appProperties;

    @Inject
    ServletContext servletContext;

    @Inject
    CommonUtils commonUtils;

    private static Logger logger = Logger.getLogger(TaskMakerService.class);

    public AbstractGeneraSpiderTaskMaker getTaskMaker(String taskType) throws IllegalAccessException, InstantiationException {
        //根据type获取爬虫对应的处理器
        SpiderTypeEnum taskEnum = SpiderTypeEnum.find(taskType);
        if(taskEnum == null){
            logger.error("【异常的爬虫任务类型:{"+taskType+"}】");
            return null;
        }
        Class<? extends AbstractGeneraSpiderTaskMaker> cls = taskEnum.getSpiderCls();
        AbstractGeneraSpiderTaskMaker taskMaker = cls.newInstance();
        taskMaker.setAppProperties(appProperties);
        taskMaker.setServletContext(servletContext);
        taskMaker.setCommonUtils(commonUtils);
        return taskMaker;
    }
}
