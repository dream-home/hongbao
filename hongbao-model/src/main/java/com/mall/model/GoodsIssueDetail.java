package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 商品竞拍明细表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class GoodsIssueDetail extends SimpleModel {

    private static final long serialVersionUID = -3056121684138761592L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 商铺Id
     */
    private String storeId;
    /**
     * 商铺名称
     */
    private String storeName;
    /**
     * 期数Id
     */
    private String issueId;
    /**
     * 期数编号
     */
    private Integer issueNo;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 图片
     */
    private String icon;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 竞拍价
     */
    private Double drawPrice;
    /**
     * 是否运气王
     */
    private Integer isWinner;
    /**
     * 中奖积分
     */
    private Double score;
    /**
     * 是否委托出售：0：否，1：是
     */
    private Integer saleSwitch;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public Integer getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(Integer issueNo) {
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

    public Integer getSaleSwitch() {
        return saleSwitch;
    }

    public void setSaleSwitch(Integer saleSwitch) {
        this.saleSwitch = saleSwitch;
    }

}
