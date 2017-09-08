package com.mall.model;

import com.yanbao.core.model.SimpleModel;

import java.io.Serializable;

/**
 * 积分兑换表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class WalletExchange extends SimpleModel implements Serializable {

    private static final long serialVersionUID = -6966754808156525194L;
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
     * 卡类型：0：银行卡，1：支付宝，2：微信支付,99:代理统计使用
     */
    private Integer cardType;
    /**
     * 银行卡号/支付宝账号/微信支付账号
     */
    private String cardNo;
    /**
     * 银行id
     */
    private String bankId;

    /**
     * 类型1：普通会员   2：商家
     */
    private Integer type;

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

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
