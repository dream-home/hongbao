package com.yanbao.vo;

import com.yanbao.core.model.SimpleModel;

/* 
 * 文件名：付款分销vo类 
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年4月6日
 * 版本号：v1.0
*/
public class PayDistributionVo extends SimpleModel {

	private static final long serialVersionUID = 561413198877606188L;
	
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 商家赠送ep
	 */
	private Double businessSendEp;
	/**
	 * 一级分销
	 */
	private Double firstReferrerScale;
	/**
	 * 二级分销
	 */
	private Double secondReferrerScale;
	/**
	 * 三级分销
	 */
	private Double thirdReferrerScale;
	/**
	 * 付款金额
	 */
	private Double payAmount;
	/**
	 * 商家用户id
	 */
	private String storeUserId;
	
	
	/**
	 * @return the storeUserId
	 */
	public String getStoreUserId() {
		return storeUserId;
	}
	/**
	 * @param storeUserId the storeUserId to set
	 */
	public void setStoreUserId(String storeUserId) {
		this.storeUserId = storeUserId;
	}
	/**
	 * @return the payAmount
	 */
	public Double getPayAmount() {
		return payAmount;
	}
	/**
	 * @param payAmount the payAmount to set
	 */
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Double getBusinessSendEp() {
		return businessSendEp;
	}
	public void setBusinessSendEp(Double businessSendEp) {
		this.businessSendEp = businessSendEp;
	}
	public Double getFirstReferrerScale() {
		return firstReferrerScale;
	}
	public void setFirstReferrerScale(Double firstReferrerScale) {
		this.firstReferrerScale = firstReferrerScale;
	}
	public Double getSecondReferrerScale() {
		return secondReferrerScale;
	}
	public void setSecondReferrerScale(Double secondReferrerScale) {
		this.secondReferrerScale = secondReferrerScale;
	}
	public Double getThirdReferrerScale() {
		return thirdReferrerScale;
	}
	public void setThirdReferrerScale(Double thirdReferrerScale) {
		this.thirdReferrerScale = thirdReferrerScale;
	}

}

