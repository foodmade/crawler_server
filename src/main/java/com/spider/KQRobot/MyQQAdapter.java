package com.spider.KQRobot;

import com.mumu.listenner.KQMSGAdapter;
import com.mumu.msg.RE_MSG;
import com.mumu.msg.RE_MSG_Forum;
import com.mumu.msg.RE_MSG_Group;
import com.mumu.msg.RE_MSG_Private;
import com.mumu.webclient.KQWebClient;
import com.spider.commonUtil.CommonUtils;
import com.spider.commonUtil.RobotOnMessageHandler;
import com.spider.spiderUtil.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyQQAdapter extends KQMSGAdapter {

    private RewriteKQWebClient kqWebClient;

    private RobotOnMessageHandler robotOnMessageHandler;

    MyQQAdapter(RewriteKQWebClient kqWebClient) {
        this.kqWebClient = kqWebClient;
    }

    public void setRobotOnMessageHandler(RobotOnMessageHandler robotOnMessageHandler) {
        this.robotOnMessageHandler = robotOnMessageHandler;
    }

    /**
     * 接收私聊消息
     */
    public void Re_MSG_Private(RE_MSG_Private msg) {
        System.out.println("接收到私聊信息 from:"+msg.getFromqq()+" \t msg:"+msg.getMsg());
        handleMsg(msg.getMsg(),msg.getFromqq(),KQConst._PRIVATE_MSG,"");
    }

    private void handleMsg(String msg,String qq,String sub_type,String groupId){
        if(msg == null || msg.isEmpty()) {
            return;
        }
        if(!msgFormatInvalid(msg)){
            return;
        }
        String movieName = msg.trim().replace(KQConst._INVALID_STR,"");
        List<Item> movieList = robotOnMessageHandler.searchVagueMovieInfo(movieName);
        String sendMsg = parseMsg(movieList,movieName);
        switch (sub_type){
            case KQConst._GROUP_MSG:
                kqWebClient.sendGroupMSG(qq,groupId,sendMsg,false);
                return;
            case KQConst._PRIVATE_MSG:
                kqWebClient.sendPrivateMSG(qq,sendMsg);
                return;
            default:
        }

    }

    private String parseMsg(List<Item> movieList,String movieName) {
        if(movieList == null || movieList.size() == 0){
            return KQConst._NOT_FOUND_SOURCE + movieName;
        }
        StringBuilder sb = new StringBuilder();
        List<HashMap<String, String>> allMoviePath = new ArrayList<>();
        for (Item item:movieList){
           allMoviePath.addAll(item.getVideoSourceList());
        }
        if(allMoviePath.size() == 0){
            return KQConst._NOT_FOUND_SOURCE + movieName;
        }
        if(allMoviePath.size()>10){
            allMoviePath = allMoviePath.subList(0,9);
        }
        sb.append("电影名称:")
                .append(movieName)
                .append("\n");
        Integer cnt = 0;
        for (HashMap<String,String> movieUrlMap : allMoviePath){
            sb.append(movieUrlMap.get("videoPathName"))
                    .append(" : ")
                    .append(movieUrlMap.get("videoPath"))
                    .append("\n");
            cnt++;
        }
        sb.append("查询到:")
                .append(cnt)
                .append("条结果");
        return sb.toString();
    }

    public void RE_MSG_FORUM(RE_MSG_Forum msg) {
        System.out.println("接收到消息 groupName:"+msg.getFromQQ() + "qq:"+msg.getFromQQ() + "msg:"+msg.getMsg());
    }

    /**
     * 接收群消息
     */
    public void RE_MSG_Group(RE_MSG_Group msg) {
        System.out.println("接收到群聊消息 groupName:"+msg.getFromGroupName() + "\t qq:"+msg.getFromQQ() + "\t msg:"+msg.getMsg());
        handleMsg(msg.getMsg(),msg.getFromQQ(),KQConst._GROUP_MSG,msg.getFromGroup());
    }

    private Boolean msgFormatInvalid(String msg){
        if(CommonUtils.isEmpty(msg)){
            return false;
        }
        return msg.contains(KQConst._INVALID_STR);
    }
}
