package com.spider.spiderUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.spider.commonUtil.CommonUtils;
import com.spider.taskPool.TaskData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskOptions {

    private static final String _cateId = "cateId";
    private static final String _pageId = "pageId";
    private static final String _sourceId = "sourceId";
    private static final String _dirId = "dirId";

    /**
     * GVideo首页搜索任务
     */
    public static List<Object> getGvideoOptions(TaskData taskData){
        List<Object> result = new ArrayList<>();
        String url = taskData.getUrl().replace(_pageId,taskData.getPage()+"");
        url = url.replace(_cateId,taskData.getSourcecateid()+"");
        String taskJsonStr = " {type:1, info:''," +
                "          taskData:{\n" +
                "              crawlingOptions:{\n" +
                "                  url:\""+url+"\",\n" +
                "                  method: 'GET',\n" +
                "                  gzip:true,\n" +
                "                  headers:{\n" +
                "                      'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',\n" +
                "                      'Accept-Encoding': 'gzip, deflate',\n" +
                "                      'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
                "                      'Cache-Control': 'max-age=0',\n" +
                "                      'Connection': 'keep-alive',\n" +
                "                      'Host': \'"+ CommonUtils.getUrlByHost(url)+"\',\n" +
                "                      'Referer':'http://97daimeng.com/index.php?m=vod-list-id-8-pg-1-order--by-hits-class-0-year-0-letter--area--lang-.html',\n"+
                "                      'Upgrade-Insecure-Requests': 1,\n" +
                "                      'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36'\n" +
                "                  },\n" +
                "                  agent:false\n" +
                "              }\n" +
                "          }\n" +
                "      }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);
        result.add(resultMap);
        return result;
    }

    /**
     * Gvideo详情页任务
     */
    public static List<Object> getGVideoDetailOptions(TaskData taskData){
        List<Object> result = new ArrayList<>();
        String url = taskData.getUrl().replace(_sourceId,taskData.getVideoid());
        String taskJsonStr = " {type:1, info:''," +
                "          taskData:{\n" +
                "              crawlingOptions:{\n" +
                "                  url:\""+url+"\",\n" +
                "                  method: 'GET',\n" +
                "                  gzip:true,\n" +
                "                  headers:{\n" +
                "                      'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',\n" +
                "                      'Accept-Encoding': 'gzip, deflate',\n" +
                "                      'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
                "                      'Cache-Control': 'max-age=0',\n" +
                "                      'Connection': 'keep-alive',\n" +
                "                      'Host': \'"+ CommonUtils.getUrlByHost(url)+"\',\n" +
                "                      'Referer':'http://97daimeng.com',\n"+
                "                      'Upgrade-Insecure-Requests': 1,\n" +
                "                      'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36'\n" +
                "                  },\n" +
                "                  agent:false\n" +
                "              }\n" +
                "          }\n" +
                "      }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);
        result.add(resultMap);
        return result;
    }

    /**
     * 海外影院 首页搜索列表
     */
    public static List<Object> getHaiWaiTaskOptions(TaskData taskData){
        List<Object> result = new ArrayList<>();
        String url = taskData.getUrl().replace(_pageId,taskData.getPage()+"");
        url = url.replace(_cateId,taskData.getSourcecateid()+"");
        String taskJsonStr = " {type:1, info:''," +
                "            taskData:{\n" +
                "                crawlingOptions:{\n" +
                "                    url:'"+url+"', \n" +
                "                    method: 'GET',\n" +
                "                    gzip:true,\n" +
                "                    headers:{\n" +
                "                        'Accept': '*/*',\n" +
                "                        'Accept-Encoding': 'gzip, deflate',\n" +
                "                        'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
                "                        'Cache-Control': 'no-cache',\n" +
                "                        'Connection': 'keep-alive',\n" +
                "                        'Host': '"+CommonUtils.getUrlByHost(url)+"',\n" +
                "                        'Pragma': 'no-cache',\n" +
                "                        'Referer': 'http://www.hwmov.com/?s=vod-show-id-26.html',\n" +
                "                        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36',\n" +
                "                        'X-Requested-With': 'XMLHttpRequest'\n" +
                "                    },\n" +
                "                    agent:false\n" +
                "                }\n" +
                "            }\n" +
                "        }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);
        result.add(resultMap);
        return result;
    }

    /**
     * HaiWai详情页任务
     */
    public static List<Object> getHaiWaiDetailOptions(TaskData taskData){
        List<Object> result = new ArrayList<>();
        String url = taskData.getUrl().replace(_sourceId,taskData.getVideoid());
        String taskJsonStr = " {type:1, info:'', " +
                "            taskData:{\n" +
                "                crawlingOptions:{\n" +
                "                    url:'"+url+"',\n" +
                "                    method: 'GET',\n" +
                "                    gzip:true,\n" +
                "                    headers:{\n" +
                "                        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',\n" +
                "                        'Accept-Encoding': 'gzip, deflate',\n" +
                "                        'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
                "                        'Cache-Control': 'no-cache',\n" +
                "                        'Connection': 'keep-alive',\n" +
                "                        'Host': '"+CommonUtils.getUrlByHost(url)+"',\n" +
                "                        'Pragma': 'no-cache',\n" +
                "                        'Referer': 'http://www.hwmov.com/?s=vod-read-id-20132.html',\n" +
                "                        'Upgrade-Insecure-Requests': 1,\n" +
                "                        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36'\n" +
                "                    },\n" +
                "                    agent:false\n" +
                "                }\n" +
                "            }\n" +
                "        }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);
        result.add(resultMap);
        return result;
    }

    /**
     * 云盘资源详情页任务
     */
    public static List<Object> getYunPanDetailOptions(TaskData taskData){
        List<Object> result = new ArrayList<>();
        String url = taskData.getUrl().replace(_sourceId,taskData.getVideoid());
        //计算目录id
        Integer dirId = CommonUtils.parseDirId(Integer.parseInt(taskData.getVideoid()));
        url = url.replace(_dirId,dirId+"");
        String taskJsonStr = " {type:1, info:'', " +
                "            taskData:{\n" +
                "                crawlingOptions:{\n" +
                "                    url:'"+url+"',\n" +
                "                    method: 'GET',\n" +
                "                    gzip:true,\n" +
                "                    encoding:null,\n"+
                "                    headers:{\n" +
                "                        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',\n" +
                "                        'Accept-Encoding': 'gzip, deflate',\n" +
                "                        'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
                "                        'Cache-Control': 'no-cache',\n" +
                "                        'Connection': 'keep-alive',\n" +
                "                        'Host': '"+CommonUtils.getUrlByHost(url)+"',\n" +
                "                        'Pragma': 'no-cache',\n" +
                "                        'Referer': 'http://www.yunpankk.com/mulu2015/11_540.html',\n" +
                "                        'Upgrade-Insecure-Requests': 1,\n" +
                "                        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36'\n" +
                "                    },\n" +
                "                    agent:false\n" +
                "                }\n" +
                "            }\n" +
                "        }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);
        result.add(resultMap);
        return result;
    }

    /**
     * 云盘资源影院 首页搜索列表
     */
    public static List<Object> getYunPanTaskOptions(TaskData taskData){
        List<Object> result = new ArrayList<>();
        String url;
        String sourceId = taskData.getSourcecateid();
       if(taskData.getPage() != 1){
           sourceId = sourceId + "_" + taskData.getPage();
       }
        url = taskData.getUrl().replace(_cateId,sourceId);
        String taskJsonStr = " {type:1, info:''," +
                "            taskData:{\n" +
                "                crawlingOptions:{\n" +
                "                    url:'"+url+"', \n" +
                "                    method: 'GET',\n" +
                "                    gzip:true,\n" +
                "                    encoding:null,\n"+
                "                    headers:{\n" +
                "                        'Accept': '*/*',\n" +
                "                        'Accept-Encoding': 'gzip, deflate',\n" +
                "                        'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
                "                        'Cache-Control': 'no-cache',\n" +
                "                        'Connection': 'keep-alive',\n" +
                "                        'Host': '"+CommonUtils.getUrlByHost(url)+"',\n" +
                "                        'Pragma': 'no-cache',\n" +
                "                        'Referer': 'http://www.yunpankk.com/mulu2015/11_540.html',\n" +
                "                        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36',\n" +
                "                        'X-Requested-With': 'XMLHttpRequest'\n" +
                "                    },\n" +
                "                    agent:false\n" +
                "                }\n" +
                "            }\n" +
                "        }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);
        result.add(resultMap);
        return result;
    }

    /**
     * dongjinggan影院 首页搜索列表
     */
    public static List<Object> getDongJingTaskOptions(TaskData taskData){
        List<Object> result = new ArrayList<>();
        String url = taskData.getUrl();
        String replaceStr;
        if(taskData.getPage()!=1){
            replaceStr = taskData.getSourcecateid()+"_"+taskData.getPage();
        }else{
            replaceStr = taskData.getSourcecateid();
        }
        url = url.replace(_pageId,replaceStr);
        String taskJsonStr = " {type:1, info:''," +
                "            taskData:{\n" +
                "                crawlingOptions:{\n" +
                "                    url:'"+url+"', \n" +
                "                    method: 'GET',\n" +
                "                    gzip:true,\n" +
                "                    encoding:null,\n"+
                "                    headers:{\n" +
                "                        'Accept': '*/*',\n" +
                "                        'Accept-Encoding': 'gzip, deflate',\n" +
                "                        'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
                "                        'Cache-Control': 'no-cache',\n" +
                "                        'Connection': 'keep-alive',\n" +
                "                        'Host': '"+CommonUtils.getUrlByHost(url)+"',\n" +
                "                        'Pragma': 'no-cache',\n" +
                "                        'Referer': 'http://www.abc03.me/tian/index57_3.html',\n" +
                "                        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36',\n" +
                "                        'X-Requested-With': 'XMLHttpRequest'\n" +
                "                    },\n" +
                "                    agent:false\n" +
                "                }\n" +
                "            }\n" +
                "        }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);
        result.add(resultMap);
        return result;
    }

    /**
     * 东京干详情页任务
     */
    public static List<Object> getDongJingDetailOptions(TaskData taskData){
        List<Object> result = new ArrayList<>();
        String url = taskData.getUrl().replace(_sourceId,taskData.getVideoid());
        String taskJsonStr = " {type:1, info:'', " +
                "            taskData:{\n" +
                "                crawlingOptions:{\n" +
                "                    url:'"+url+"',\n" +
                "                    method: 'GET',\n" +
                "                    gzip:true,\n" +
                "                    encoding:null,\n"+
                "                    headers:{\n" +
                "                        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',\n" +
                "                        'Accept-Encoding': 'gzip, deflate',\n" +
                "                        'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
                "                        'Cache-Control': 'no-cache',\n" +
                "                        'Connection': 'keep-alive',\n" +
                "                        'Host': '"+CommonUtils.getUrlByHost(url)+"',\n" +
                "                        'Pragma': 'no-cache',\n" +
                "                        'Referer': 'http://www.abc03.me/tian/index57_4.html',\n" +
                "                        'Upgrade-Insecure-Requests': 1,\n" +
                "                        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36'\n" +
                "                    },\n" +
                "                    agent:false\n" +
                "                }\n" +
                "            }\n" +
                "        }";
        HashMap<String,Object> resultMap = JSON.parseObject(taskJsonStr,new TypeReference<HashMap<String, Object>>(){});
        resultMap.put("task",taskData);
        result.add(resultMap);
        return result;
    }
}
