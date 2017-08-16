package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2017年2月4日
 */
public class PhoneVo implements Serializable {

	private static final long serialVersionUID = -658908664230187672L;

	/** 手机号 */
	private String phone;
	/** 短信验证码 */
	private String smsCode;
	/** 图片验证码 */
	private String picCode;
	/** 图片验证码reids key */
	private String key;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getPicCode() {
		return picCode;
	}

	public void setPicCode(String picCode) {
		this.picCode = picCode;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
