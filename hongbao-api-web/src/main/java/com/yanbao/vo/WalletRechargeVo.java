package com.yanbao.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @author yiyr
 * @date 2017年5月23日
 */
public class WalletRechargeVo implements Serializable {

	private static final long serialVersionUID = -6151106965489599668L;
	//id
	private String id;
	//订单号
	private String orderNo;
	//用户ID(支付者)
	private String userId;
	//充值积分
	private Double score;
	//付款金额
    private Double payment;
    //实际到账
    private Double confirmScore;
    //来源
    private String source;
    //商户Id
    private String storeUserId;
    //商家赠送ep
    private Double businessSendEp;
    //一级分销
    private Double firstReferrerScale;
    // 二级分销
    private Double secondReferrerScale;
    // 三级分销
    private Double thirdReferrerScale;
    //EP抵用率
    private Double discountEP;
    //创建时间
    private Date createTime;
    //支付状态
    private String status;
    //其他状态
    private Map<String,String> storeInfo;
   
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Double getPayment() {
		return payment;
	}
	public void setPayment(Double payment) {
		this.payment = payment;
	}
	public Double getConfirmScore() {
		return confirmScore;
	}
	public void setConfirmScore(Double confirmScore) {
		this.confirmScore = confirmScore;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStoreUserId() {
		return storeUserId;
	}
	public void setStoreUserId(String storeUserId) {
		this.storeUserId = storeUserId;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Map<String, String> getStoreInfo() {
		return storeInfo;
	}
	public void setStoreInfo(Map<String, String> storeInfo) {
		this.storeInfo = storeInfo;
	}
	public Double getDiscountEP() {
		return discountEP;
	}
	public void setDiscountEP(Double discountEP) {
		this.discountEP = discountEP;
	}
	
    
    
}
