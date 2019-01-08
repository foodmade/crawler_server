package com.spider.enumUtil;

import com.spider.commonUtil.SpiderTypeConst;
import com.spider.taskMaker.*;

/**
 * 爬虫类型映射
 */
public enum  SpiderTypeEnum {

    GVIDEO(SpiderTypeConst.GVIDEO_TYPE, GVideoTaskMaker.class,"Gvideo影院"),
    HAIWAI(SpiderTypeConst.HAIWAI_TYPE, HaiWaiTaskMaker.class,"海外影院"),
    YUNPAN(SpiderTypeConst.YUNPAN_TYPE, YunPanTaskMaker.class,"云盘影视 http://www.yunpankk.com/"),
    DONGJING(SpiderTypeConst.DONGJING_TYPE, DongJingTaskMaker.class,"东京干 http://www.abc03.me");

    private String spiderType;

    private Class<? extends AbstractGeneraSpiderTaskMaker> spiderCls;

    private String desc;

    SpiderTypeEnum(String spiderType, Class<? extends AbstractGeneraSpiderTaskMaker> spiderCls,String desc) {
        this.spiderType = spiderType;
        this.spiderCls = spiderCls;
        this.desc = desc;
    }

    public String getSpiderType() {
        return spiderType;
    }

    public void setSpiderType(String spiderType) {
        this.spiderType = spiderType;
    }

    public Class<? extends AbstractGeneraSpiderTaskMaker> getSpiderCls() {
        return spiderCls;
    }

    public void setSpiderCls(Class<? extends AbstractGeneraSpiderTaskMaker> spiderCls) {
        this.spiderCls = spiderCls;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static SpiderTypeEnum find(String type){
        SpiderTypeEnum[] list = SpiderTypeEnum.values();
        for (SpiderTypeEnum spiderTypeEnum:list){
            if(spiderTypeEnum.getSpiderType().equals(type)){
                return spiderTypeEnum;
            }
        }
        return null;
    }
}
