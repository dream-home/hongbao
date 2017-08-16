package com.yanbao.vo;


import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;

/**
 * Created by summer on 2016-12-15:14:41;
 */
public class RechargeVo extends BaseVo {

    private static final long serialVersionUID = 3782152232870257148L;
    private Integer uid;

    private String phone;

    private String orderNo;

    private String userName;

    private Double score;// 积分数量

    private String remark;

    private Integer source;//充值来源：0：网银，1：支付宝，2：微信，3：苹果支付

    private String userId;

    private Double confirmScore;

    private Double poundage;
    //支付来源名字
    private String sourceName;
    
    private String storeUserId;
    
    private Double businessSendEp;
    
    private Double firstReferrerScale;
    
    private Double secondReferrerScale;
    
    private Double thirdReferrerScale;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;
    
    
    
    
    

    public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    
}
