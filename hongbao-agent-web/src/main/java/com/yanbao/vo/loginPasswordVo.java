package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zzwei
 * @date 2017年2月16日
 */
public class loginPasswordVo implements Serializable {

	private static final long serialVersionUID = 8315942462674630954L;

	private String phone;
	private String smsCode;
	private String newLoginPass;
	private String newLoginPassConfirm;
	/**
	 * @return the smsCode
	 */
	public String getSmsCode() {
		return smsCode;
	}
	/**
	 * @param smsCode the smsCode to set
	 */
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	/**
	 * @return the newLoginPass
	 */
	public String getNewLoginPass() {
		return newLoginPass;
	}
	/**
	 * @param newLoginPass the newLoginPass to set
	 */
	public void setNewLoginPass(String newLoginPass) {
		this.newLoginPass = newLoginPass;
	}
	/**
	 * @return the newLoginPassConfirm
	 */
	public String getNewLoginPassConfirm() {
		return newLoginPassConfirm;
	}
	/**
	 * @param newLoginPassConfirm the newLoginPassConfirm to set
	 */
	public void setNewLoginPassConfirm(String newLoginPassConfirm) {
		this.newLoginPassConfirm = newLoginPassConfirm;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
