package com.yanbao.vo;

import java.util.Date;

/**
 * 统计商家提现记录
 * Created by Administrator on 2017/6/19.
 */
public class StoreExchangeRecord {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 提现金额
     */
    private Double score;

    /**
     * 提现会员所属地区
     */
    private String areaId;

    /**
     * 商家id
     */
    private String storeId;

    private Date createTime;//创建时间
    
    private Date updateTime;//更新时间

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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
