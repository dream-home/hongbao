package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 积分流水表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class WalletRecord extends SimpleModel {

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
     * 积分
     */
    private Double score;
    /**
     * 流水类型：0：充值+，1：兑换+，2：赠送+-，3：竞拍消费-，4：竞拍中奖+；5：委托出售+，6：积分购买-，7：积分回退+
     */
    private Integer recordType;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }
}
