package com.mall.model;

import com.yanbao.core.model.SimpleModel;

public class BusinessEpRecord extends SimpleModel {

    private static final long serialVersionUID = -3630149972829325274L;

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 获赠ep值
     */
    private Double ep;

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

    public Double getEp() {
        return ep;
    }

    public void setEp(Double ep) {
        this.ep = ep;
    }

}
