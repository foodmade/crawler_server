package com.spider.KQRobot;
import com.spider.commonUtil.RobotOnMessageHandler;
import org.apache.log4j.Logger;

import java.net.URI;

public class KQClient {

    private static Logger logger = Logger.getLogger(KQClient.class);

    private static RewriteKQWebClient kqWebClient;

    public static void runClient(String host,RobotOnMessageHandler robotOnMessageHandler){
        try {
            if(kqWebClient == null){
                kqWebClient = new RewriteKQWebClient(new URI(host));
            }
            MyQQAdapter myQQAdapter = new MyQQAdapter(kqWebClient);
            myQQAdapter.setRobotOnMessageHandler(robotOnMessageHandler);
            kqWebClient.addQQMSGListenner(myQQAdapter);
        }catch (Exception e){
            logger.error("init KQ client fail e:"+e.getMessage());
            e.printStackTrace();
        }
    }
}
