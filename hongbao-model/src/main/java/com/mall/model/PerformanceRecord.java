package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 业绩处理60层流水表
 *
 * @author zzwei
 * @date 2017年06月21日
 */
public class PerformanceRecord extends SimpleModel {

    private static final long serialVersionUID = -8279693845246343459L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 消费EP
     */
    private Double consumeEp;
    /**
     * 流水类型：1、扫码支付、2  商家二维码支付、3：加入合伙人支付（余额 、支付宝、微信支付） 4：加入合伙人扣除800EP 5：直接购买EP抵扣
     */
    private Integer recordType;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getConsumeEp() {
        return consumeEp;
    }

    public void setConsumeEp(Double consumeEp) {
        this.consumeEp = consumeEp;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
