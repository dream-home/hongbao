package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2017年2月4日
 */
public class LoginPwdVo implements Serializable {

	private static final long serialVersionUID = 8315942462674630954L;

	/** 当前支付密码 */
	private String oldLoginPwd;
	/** 新支付密码 */
	private String newLoginPwd;
	/** 新支付密码 */
	private String newLoginPwdConfirm;
	/**
	 * @return the oldLoginPwd
	 */
	public String getOldLoginPwd() {
		return oldLoginPwd;
	}
	/**
	 * @param oldLoginPwd the oldLoginPwd to set
	 */
	public void setOldLoginPwd(String oldLoginPwd) {
		this.oldLoginPwd = oldLoginPwd;
	}
	/**
	 * @return the newLoginPwd
	 */
	public String getNewLoginPwd() {
		return newLoginPwd;
	}
	/**
	 * @param newLoginPwd the newLoginPwd to set
	 */
	public void setNewLoginPwd(String newLoginPwd) {
		this.newLoginPwd = newLoginPwd;
	}
	/**
	 * @return the newLoginPwdConfirm
	 */
	public String getNewLoginPwdConfirm() {
		return newLoginPwdConfirm;
	}
	/**
	 * @param newLoginPwdConfirm the newLoginPwdConfirm to set
	 */
	public void setNewLoginPwdConfirm(String newLoginPwdConfirm) {
		this.newLoginPwdConfirm = newLoginPwdConfirm;
	}
}
