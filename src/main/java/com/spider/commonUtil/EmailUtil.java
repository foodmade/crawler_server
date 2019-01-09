package com.spider.commonUtil;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailUtil {

    @Inject
    private Properties emailProperties;

    private JavaMailSenderImpl mailSender;

    /**
     * 邮件发送器
     */
    public void initMailSender() {
        if(mailSender != null){
            return;
        }
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(emailProperties.getProperty("email.host"));
        sender.setPort(Integer.parseInt(emailProperties.getProperty("email.port")));
        sender.setUsername(emailProperties.getProperty("email.username"));
        sender.setPassword(emailProperties.getProperty("email.password"));
        sender.setDefaultEncoding("utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", emailProperties.getProperty("email.timeout"));
        p.setProperty("mail.smtp.auth", emailProperties.getProperty("email.auth"));
        sender.setJavaMailProperties(p);
        this.mailSender = sender;
    }

    /**
     * 发送邮件
     *
     * @param to 接受人
     * @param subject 主题
     * @param html 发送内容
     */
    public void sendMail(String to, String subject, String html) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(emailProperties.getProperty("email.username"));
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }
}
