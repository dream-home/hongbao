package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2017年2月4日
 */
public class WalletVo implements Serializable {

	private static final long serialVersionUID = -6151106905405989668L;

	/** 赠送用户手机号/UID */
	private String donateTo;
	/** 赠送积分 */
	private Double score;
	/** 充值订单号 */
	private String orderNo;
	/** 支付密码 */
	private String payPwd;
	/** 充值来源：1：支付宝，2：微信，3：余额  */
	private Integer source;
	/** IP */
	private String ip;
	/** 收款商家用户id */
	private String storeUserId;
	/**二维码类型(1:面对面普通二维码 2:商家固定的二维码) 8*/
	private Integer codeType;
	/**ep折扣优惠*/
	private Double discountEP;
	/**使用场景 1：微店*/
	private Integer scenes;

	/**
	 * 赠送ep值
	 * @return
	 */
	private Double ep;

	public String getDonateTo() {
		return donateTo;
	}

	public void setDonateTo(String donateTo) {
		this.donateTo = donateTo;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

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

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getStoreUserId() {
		return storeUserId;
	}

	public void setStoreUserId(String storeUserId) {
		this.storeUserId = storeUserId;
	}

	public Integer getCodeType() {
		return codeType;
	}

	public void setCodeType(Integer codeType) {
		this.codeType = codeType;
	}

	public Double getDiscountEP() {
		return discountEP;
	}

	public void setDiscountEP(Double discountEP) {
		this.discountEP = discountEP;
	}

	public Integer getScenes() {
		return scenes;
	}

	public void setScenes(Integer scenes) {
		this.scenes = scenes;
	}

	public Double getEp() {
		return ep;
	}

	public void setEp(Double ep) {
		this.ep = ep;
	}
}
