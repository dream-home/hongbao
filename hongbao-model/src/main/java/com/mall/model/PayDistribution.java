package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/* 
 * 文件名：付款分销po类 
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年4月6日
 * 版本号：v1.0
*/
public class PayDistribution extends SimpleModel {

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
     * 参数类型
     * 1:面对面扫码 2:商户二维码支付
     */
    private Integer type;
    /**
     * EP折扣率
     */
    private Double discountEP;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getDiscountEP() {
        return discountEP;
    }

    public void setDiscountEP(Double discountEP) {
        this.discountEP = discountEP;
    }

}

