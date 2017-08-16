package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 订单明细
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class GoodsWinDetail extends SimpleModel {

    private static final long serialVersionUID = 5420862836860779588L;
    /**
     * 订单主表ID
     */
    private String goodsWinId;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 商铺Id
     */
    private String storeId;
    /**
     * 商铺名称
     */
    private String storeName;
    /**
     * 商品Id
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
     * 商品数量
     */
    private Integer num;
    /**
     * 一级分销比例
     */
    private Double firstReferrerScale = 0d;
    /**
     * 二级分销比例
     */
    private Double secondReferrerScale = 0d;
    /**
     * 三级分销比例
     */
    private Double thirdReferrerScale = 0d;
    /**
     * 商家赠送Ep
     */
    private Double businessSendEp = 0d;
    /**
     * 商家原价
     */
    private Double originalPrice;
    //ep商品折扣优惠
    private Double discountEP;

    public String getGoodsWinId() {
        return goodsWinId;
    }

    public void setGoodsWinId(String goodsWinId) {
        this.goodsWinId = goodsWinId;
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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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

    public Double getBusinessSendEp() {
        return businessSendEp;
    }

    public void setBusinessSendEp(Double businessSendEp) {
        this.businessSendEp = businessSendEp;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getDiscountEP() {
        return discountEP;
    }

    public void setDiscountEP(Double discountEP) {
        this.discountEP = discountEP;
    }
}
