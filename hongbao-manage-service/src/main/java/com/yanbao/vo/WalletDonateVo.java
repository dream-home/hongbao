package com.yanbao.vo;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;

/**
 * Created by summer on 2017-01-13:17:06;
 */
public class WalletDonateVo extends BaseVo {

    /** 订单号 */
    private String orderNo;
    /** 用户Id */
    private String userId;
    /** 赠送积分 */
    private Double score;
    /** 实际到账 */
    private Double confirmScore;
    /** 手续费 */
    private Double poundage;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updateTime;
    /** 赠送人id */
    private String donateUserId;
    /** 赠送人uid */
    private Integer donateUid;

    private String phone;

    private String userName;

    private String nickName;

    private String storeName;

    
    private String uid;
    

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
