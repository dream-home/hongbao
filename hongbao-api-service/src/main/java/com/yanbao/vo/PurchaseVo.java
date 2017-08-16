package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2017年2月4日
 */
public class PurchaseVo implements Serializable {

	private static final long serialVersionUID = 8041418455885782640L;

	/** 商品Id */
	private String goodsId;
	/** 支付密码 */
	private String payPwd;
	/** 收件人姓名 */
	private String userName;
	/** 手机号 */
	private String phone;
	/** 收货地址 */
	private String addr;
	/** 商品兑换数量 */
	private Integer num;
	/** 分享用户的id */
	private String shareUserId;
	/** 支付类型 */
	private Integer payType;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
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

	public String getShareUserId() {
		return shareUserId;
	}

	public void setShareUserId(String shareUserId) {
		this.shareUserId = shareUserId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
}
