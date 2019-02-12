package com.spider.commonUtil.emailUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailModel {


    public EmailModel(String address,String type,String html,String subject){
        this.emailAddress = address;
        this.emailType = type;
        this.emailHtml = html;
        this.subject = subject;
    }

    /**
     * 发送邮件目标
     */
    private String emailAddress;

    /**
     * 业务类型
     */
    private String emailType;

    /**
     * 邮件内容
     */
    private String emailHtml;

    /**
     * 邮件主题
     */
    private String subject;
}
