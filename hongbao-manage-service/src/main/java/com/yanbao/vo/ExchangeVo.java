package com.yanbao.vo;


import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;

/**
 * Created by summer on 2016-12-16:14:47;
 */
public class ExchangeVo extends BaseVo {

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 兑换积分
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
     * 银行名称
     */
    private String bankName;
    /**
     * 卡类型：0：银行卡，1：支付宝，2：微信支付
     */
    private Integer cardType;
    /**
     * 银行卡号/支付宝账号/微信支付账号
     */
    private String cardNo;
    /** UID */
    private Integer uid;

    /** 手机号 */
    private String phone;

    /** 真实姓名 */
    private String userName;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
