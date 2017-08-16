package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 积分充值表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class WalletRecharge extends SimpleModel {

    private static final long serialVersionUID = 9152393046045549128L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 充值积分
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
     * 来源：0：支付宝，1：微信，2：苹果支付，3：网银
     */
    private Integer source;
    /**
     * 商户Id
     */
    private String storeUserId;

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
     * EP抵用率
     */
    private Double discountEP;
    /**
     * 用户获赠的斗斗
     */
    private Double doudou;

    /**
     * 使用场景 1：微店
     */
    private Integer scenes;


    public Double getDoudou() {
        return doudou;
    }

    public void setDoudou(Double doudou) {
        this.doudou = doudou;
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

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getStoreUserId() {
        return storeUserId;
    }

    public void setStoreUserId(String storeUserId) {
        this.storeUserId = storeUserId;
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

    public Double getDiscountEP() {
        return discountEP;
    }

    public void setDiscountEP(Double discountEP) {
        this.discountEP = discountEP;
    }

    public Integer getScenes() {
        return scenes;
    }

    public void setScenes(Integer scenes) {
        this.scenes = scenes;
    }
}
