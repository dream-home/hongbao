package com.yanbao.vo;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;

/**
 * Created by summer on 2016-12-19:18:05;
 */
public class RecordVo extends BaseVo {

    private Integer uid;

    private String phone;

    private Integer recordType;

    private String remark;

    private String userId;
    // 1 收入类型 2 支出类型
    private Integer isIncome;

    
    private String recordTypeName;
    
    
    
    private Double score;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;
    
    
    
    
    

	public String getRecordTypeName() {
		return recordTypeName;
	}

	public void setRecordTypeName(String recordTypeName) {
		this.recordTypeName = recordTypeName;
	}

	public Integer getIsIncome() {
        return isIncome;
    }

    public void setIsIncome(Integer isIncome) {
        this.isIncome = isIncome;
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

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
