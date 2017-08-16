package com.yanbao.vo;

import java.io.Serializable;

/**
 * Created by summer on 2016-12-08:16:32;
 */
public class SysStatistics implements Serializable{

    private static final long serialVersionUID = -4419096054194682710L;
    /**
     * 商品发布总数
     */
    private Integer goodsCount;
    /**
     * 竞拍开奖总期数
     */
    private Integer issueCount;
    /**
     * 系统收入积分总数
     */
    private Double pointCount;
    /**
     * 会员数量
     */
    private Integer userCount;
    /**
     * 竞拍参与人次
     */
    private Integer userIssueCount;
    /**
     * 手工充值总积分
     */
    private Double handworkScore;

    public Double getHandworkScore() {
        return handworkScore;
    }

    public void setHandworkScore(Double handworkScore) {
        this.handworkScore = handworkScore;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Integer getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    public Double getPointCount() {
        return pointCount;
    }

    public void setPointCount(Double pointCount) {
        this.pointCount = pointCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getUserIssueCount() {
        return userIssueCount;
    }

    public void setUserIssueCount(Integer userIssueCount) {
        this.userIssueCount = userIssueCount;
    }
}
