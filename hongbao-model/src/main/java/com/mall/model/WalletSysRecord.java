package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/* 
 * 文件名：WalletSysRecord.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年3月6日
 * 版本号：v1.0
*/
public class WalletSysRecord extends SimpleModel {

    private static final long serialVersionUID = 2290058805671932342L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 积分
     */
    private Double score;
    /**
     * 流水类型：0:购买ep转换的系统积分
     */
    private Integer recordType;


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

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

}

