/* 
 * 文件名：CashMoneyVo.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：李小龙 
 * 创建时间：2017年1月12日
 * 版本号：v1.0
*/
package com.yanbao.vo;

import java.io.Serializable;

/**
 * 提现对象
 * @author lxl
 * @version v1.0
 * @date 2017年1月12日
 */
public class CashMoneyVo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 提现编号
	 */
	private String cashId;
	
	/**
	 * 提现金额
	 */
	private String cashMoney;
	
	/**
	 * 银行卡号
	 */
	private String bankCardNo;
	
	/**
	 * 提现会员真实名称
	 */
	private String name;
	
	/**
	 * 清算联号
	 */
	private String unionPayNumber;
	
	/**
	 * 会员id
	 */
	private String userId;
	/**
	 * 提现订单号
	 */
	private String tranId;

	/**
	 * @return the cashId
	 */
	public String getCashId() {
		return cashId;
	}

	/**
	 * @param cashId the cashId to set
	 */
	public void setCashId(String cashId) {
		this.cashId = cashId;
	}

	/**
	 * @return the cashMoney
	 */
	public String getCashMoney() {
		return cashMoney;
	}

	/**
	 * @param cashMoney the cashMoney to set
	 */
	public void setCashMoney(String cashMoney) {
		this.cashMoney = cashMoney;
	}

	/**
	 * @return the bankCardNo
	 */
	public String getBankCardNo() {
		return bankCardNo;
	}

	/**
	 * @param bankCardNo the bankCardNo to set
	 */
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the unionPayNumber
	 */
	public String getUnionPayNumber() {
		return unionPayNumber;
	}

	/**
	 * @param unionPayNumber the unionPayNumber to set
	 */
	public void setUnionPayNumber(String unionPayNumber) {
		this.unionPayNumber = unionPayNumber;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

}
