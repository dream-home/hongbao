package com.yanbao.vo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import com.yanbao.core.model.SimpleModel;

public class WalletSubsidySignVo extends SimpleModel {
	
	  /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 补贴用户Id
     */
    private String donateUserId;
    /**
     * 补贴用户Uid
     */
    private Integer donateUid;
    /**
     * 补贴积分
     */
    private Double score;
    /**
     * 实际到账
     */
    private Double confirmScore;
    /**
     * 手续费
     */
    private Double poundage;
    /**
     * 补贴次数
     */
    private Integer subsidyNo;
    /**
     * 补贴等级
     */
    private Integer grade;
    /**
     * 用户补贴时间
     */

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date signTime;
    
    @JsonIgnore
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date fromTime;
	@JsonIgnore
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date stopTime;
	
	 /**用户名*/
    private String userName;
    
    /**用户手机电话*/
    private String phone;
	
    
    
    
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
	public String getDonateUserId() {
		return donateUserId;
	}
	public void setDonateUserId(String donateUserId) {
		this.donateUserId = donateUserId;
	}
	public Integer getDonateUid() {
		return donateUid;
	}
	public void setDonateUid(Integer donateUid) {
		this.donateUid = donateUid;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Double getConfirmScore() {
		return confirmScore;
	}
	public void setConfirmScore(Double confirmScore) {
		this.confirmScore = confirmScore;
	}
	public Double getPoundage() {
		return poundage;
	}
	public void setPoundage(Double poundage) {
		this.poundage = poundage;
	}
	public Integer getSubsidyNo() {
		return subsidyNo;
	}
	public void setSubsidyNo(Integer subsidyNo) {
		this.subsidyNo = subsidyNo;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public Date getSignTime() {
		return signTime;
	}
	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
	public Date getFromTime() {
		return fromTime;
	}
	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	
	
	
	
	
	
	
	

}
