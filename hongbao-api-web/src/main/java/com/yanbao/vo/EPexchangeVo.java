package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zzwei
 * @date 2017年06月17日
 */
public class EPexchangeVo implements Serializable {

	private static final long serialVersionUID = -6151106905405989698L;
	/** 兑换EP额度 */
	private Double ep;
	/** 支付密码 */
	private String payPwd;

	public Double getEp() {
		return ep;
	}

	public void setEp(Double ep) {
		this.ep = ep;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
}
