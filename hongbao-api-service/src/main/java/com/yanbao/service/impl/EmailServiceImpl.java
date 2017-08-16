/* 
 * 文件名：EmailServiceImpl.java  
 * 版权：Copyright 2016-2016 炎宝网络科技  All Rights Reserved by
 * 修改人：邱深友  
 * 创建时间：2016年12月26日
 * 版本号：v1.0
*/
package com.yanbao.service.impl;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.yanbao.service.EmailService;






/**
 * 发送Email实现类
 * @author qsy
 * @version v1.0
 * @date 2016年12月26日
 */
@Service
public class EmailServiceImpl implements EmailService {
	@Resource
    private JavaMailSenderImpl javaMailSender;
	
	@Override
	public void sendEmail(String subject,String content,String ... toEmails){
		// 建立邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
        	messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        	//设置发件人
        	messageHelper.setFrom(javaMailSender.getJavaMailProperties().getProperty("email.sendFrom"));
        	//设置收件人
        	messageHelper.setTo(toEmails);
        	//设置邮件主题
        	messageHelper.setSubject(subject);
        	//设置内容HTML
        	messageHelper.setText(content, true);
        	//发送邮件
        	javaMailSender.send(message);
        	
		} catch (Exception ex) {
			
		}
	}

}
