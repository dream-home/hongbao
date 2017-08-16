package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zzwei
 * @date 2017年07月12日
 */
public class WalletV42Vo implements Serializable {
	private static final long serialVersionUID = -6151106905225989668L;
	/** 银行编号 */
	private String bankId;
	/** 银行中文名 */
	private String bankName;
	/** 银行卡号 */
	private String cardNo;
	/** 真实姓名 */
	private String userName;

	/** 赠送积分 */
	private Double score;
	/** 支付密码 */
	private String payPwd;
	/** 充值来源：1：支付宝，2：微信，3：余额  */
	private Integer source;
	/** IP */
	private String ip;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
