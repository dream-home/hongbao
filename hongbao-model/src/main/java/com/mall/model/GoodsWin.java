package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 我的竞拍中奖表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class GoodsWin extends SimpleModel {

    private static final long serialVersionUID = 5420862836860779548L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单类型：0：竞拍中奖，1、直接购买
     */
    private Integer orderType;
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
     * 商品介绍
     */
    private String detail;
    /**
     * 图片
     */
    private String icon;
    /**
     * 期数Id
     */
    private String issueId;
    /**
     * 期数编号
     */
    private Integer issueNo;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 竞拍积分
     */
    private Double drawPrice;
    /**
     * 中奖积分
     */
    private Double score;
    /**
     * 快递单号
     */
    private String expressNo;
    /**
     * 快递名称
     */
    private String expressName;
    /**
     * 收件人姓名
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 收货地址
     */
    private String addr;
    /**
     * 是否委托出售：0：否，1：是
     */
    private Integer saleSwitch;
    /**
     * 商品数量
     */
    private Integer num;
    /**
     * 分享用户id
     */
    private String shareUserId;
    /**
     * 付款方式
     */
    private Integer payWay;
    /**
     * EP抵用率
     */
    private Double discountEP;
    /**
     * 一级分销比例
     */
    private Double firstReferrerScale;
    /**
     * 二级分销比例
     */
    private Double secondReferrerScale;
    /**
     * 三级分销比例
     */
    private Double thirdReferrerScale;
    /**
     * 商家赠送Ep
     */
    private Double businessSendEp;
    /**
     * 使用场景 1：微店
     */
    private Integer scenes;
    /**
     * 用户获赠的斗斗
     */
    private Double doudou;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getSaleSwitch() {
        return saleSwitch;
    }

    public void setSaleSwitch(Integer saleSwitch) {
        this.saleSwitch = saleSwitch;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getShareUserId() {
        return shareUserId;
    }

    public void setShareUserId(String shareUserId) {
        this.shareUserId = shareUserId;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Double getDiscountEP() {
        return discountEP;
    }

    public void setDiscountEP(Double discountEP) {
        this.discountEP = discountEP;
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
    public Integer getScenes() {
        return scenes;
    }

    public void setScenes(Integer scenes) {
        this.scenes = scenes;
    }

    public Double getDoudou() {
        return doudou;
    }

    public void setDoudou(Double doudou) {
        this.doudou = doudou;
    }
}
