package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 积分赠送表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class WalletDonate extends SimpleModel {

    private static final long serialVersionUID = -2583222568080769746L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 赠送用户Id
     */
    private String donateUserId;
    /**
     * 赠送用户Uid
     */
    private Integer donateUid;
    /**
     * 赠送积分
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

    public String getDonateUserId() {
        return donateUserId;
    }

    public void setDonateUserId(String donateUserId) {
        this.donateUserId = donateUserId;
    }

    public Integer getDonateUid() {
        return donateUid;
    }

    public void setDonateUid(Integer donateUid) {
        this.donateUid = donateUid;
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
}
