package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2017年2月4日
 */
public class PayPwdVo implements Serializable {

	private static final long serialVersionUID = 8315942462674630954L;

	/** 当前支付密码 */
	private String oldPayPwd;
	/** 新支付密码 */
	private String newPayPwd;
	/** 新支付密码 */
	private String newPayPwd2;

	public String getOldPayPwd() {
		return oldPayPwd;
	}

	public void setOldPayPwd(String oldPayPwd) {
		this.oldPayPwd = oldPayPwd;
	}

	public String getNewPayPwd() {
		return newPayPwd;
	}

	public void setNewPayPwd(String newPayPwd) {
		this.newPayPwd = newPayPwd;
	}

	public String getNewPayPwd2() {
		return newPayPwd2;
	}

	public void setNewPayPwd2(String newPayPwd2) {
		this.newPayPwd2 = newPayPwd2;
	}

}
