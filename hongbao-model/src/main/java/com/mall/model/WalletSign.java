package com.mall.model;

import com.yanbao.core.model.SimpleModel;

import java.util.Date;

/**
 * 积分签到赠送表
 *
 * @author zcj
 * @date 2017年03月03日
 */
public class WalletSign extends SimpleModel {
    private static final long serialVersionUID = -1402747850821257828L;
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
    /**
     * 签到次数
     */
    private Integer signNo;
    /**
     * 当前签到等级
     */
    private Integer grade;
    /**
     * 用户签到时间
     */
    private Date signTime;
    /**
     * 签到轮次
     */
    private Integer signCount;
    /**
     * 类型 1:斗斗签到  2：合伙人签到  3：ep兑换成斗斗
     */
    private Integer type;
    /**
     * EP兑换成斗斗的比例：如1ep兑换5个斗斗，即是 5
     **/
    private Double scale;
    /**
     * EP兑换成斗斗,斗斗的到账数量
     **/
    private Double doudou;

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
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

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the signNo
     */
    public Integer getSignNo() {
        return signNo;
    }

    /**
     * @param signNo the signNo to set
     */
    public void setSignNo(Integer signNo) {
        this.signNo = signNo;
    }

    /**
     * @return the grade
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getSignCount() {
        return signCount;
    }

    public void setSignCount(Integer signCount) {
        this.signCount = signCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public Double getDoudou() {
        return doudou;
    }

    public void setDoudou(Double doudou) {
        this.doudou = doudou;
    }
}
