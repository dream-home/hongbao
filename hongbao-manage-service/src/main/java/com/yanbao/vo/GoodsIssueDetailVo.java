package com.yanbao.vo;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;

/**
 * Created by summer on 2016-12-23:15:47;
 */
public class GoodsIssueDetailVo extends BaseVo{

    private String phone;

    private Integer uid;

    private String storeName;

    private String userName;
    /** 订单号 */
    private String orderNo;
    /** 用户ID */
    private String userId;
    /** 期数Id */
    private String issueId;
    /** 期数编号 */
    private Long issueNo;
    /** 商品ID */
    private String goodsId;
    /** 商品名称 */
    private String goodsName;
    /** 图片 */
    private String icon;
    /** 商品价格 */
    private Double price;
    /** 竞拍价 */
    private Double drawPrice;
    /** 是否运气王 */
    private Integer isWinner;
    /** 中奖积分 */
    private Double score;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public Long getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(Long issueNo) {
        this.issueNo = issueNo;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDrawPrice() {
        return drawPrice;
    }

    public void setDrawPrice(Double drawPrice) {
        this.drawPrice = drawPrice;
    }

    public Integer getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(Integer isWinner) {
        this.isWinner = isWinner;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
