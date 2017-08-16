package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2017年2月4日
 */
public class OrderVo implements Serializable {

	private static final long serialVersionUID = 1945539359683835123L;

	/** 订单号 */
	private String orderNo;
	/** 支付密码 */
	private String payPwd;
	/** 快递单号 */
	private String expressNo;
	/** 快递名称 */
	private String expressName;
	/** 收件人姓名 */
	private String userName;
	/** 手机号 */
	private String phone;
	/** 收货地址 */
	private String addr;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}
