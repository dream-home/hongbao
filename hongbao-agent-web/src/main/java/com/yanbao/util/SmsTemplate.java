package com.yanbao.util;

/**
 * 短信模板类
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum SmsTemplate {

	/** 通用 */
	COMMON("SMS_15555481", ""),

	/** 绑定手机号 */
	BIND_PHONE("SMS_34910339", ",'product':'斗拍商城'");

	private final String code;

	private final String param;

	private SmsTemplate(String code, String param) {
		this.code = code;
		this.param = param;
	}

	public String getCode() {
		return code;
	}

	public String getParam() {
		return param;
	}

}
