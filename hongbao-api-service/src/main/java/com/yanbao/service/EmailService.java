/* 
 * 文件名：EmailService.java  
 * 版权：Copyright 2016-2016 炎宝网络科技  All Rights Reserved by
 * 修改人：邱深友  
 * 创建时间：2016年12月26日
 * 版本号：v1.0
*/
package com.yanbao.service;

/**
 * 发送EMAIL业务层接口
 * @author qsy
 * @version v1.0
 * @date 2016年12月26日
 */
public interface EmailService {

	/**
	 * 发送邮件
	 * @param subject 邮件主题
	 * @param content 邮件内容（HTML）
	 * @param toEmails 收件人
	 */
	public void sendEmail(String subject, String content, String ... toEmails);

}
