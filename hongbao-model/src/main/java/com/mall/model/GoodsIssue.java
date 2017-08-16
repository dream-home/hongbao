package com.mall.model;

import com.yanbao.core.model.SimpleModel;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 商品竞拍期数表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class GoodsIssue extends SimpleModel {

    private static final long serialVersionUID = 957169163816433840L;
    /**
     * 期数编号
     */
    private Integer issueNo;
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
     * 竞拍价
     */
    private Double drawPrice;
    /**
     * 参与竞拍人数
     */
    private Integer drawNum;
    /**
     * 当前竞拍人数
     */
    private Integer curNum;
    /**
     * 剩余人数 数据库没有
     */
    private Integer remainNum;
    /**
     * 是否委托出售：0：否，1：是
     */
    private Integer saleSwitch;
    /**
     * 商家赠送Ep
     */
    private Double businessSendEp = 0d;
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

    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromTime;
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(Integer issueNo) {
        this.issueNo = issueNo;
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

    public Double getDrawPrice() {
        return drawPrice;
    }

    public void setDrawPrice(Double drawPrice) {
        this.drawPrice = drawPrice;
    }

    public Integer getDrawNum() {
        return drawNum;
    }

    public void setDrawNum(Integer drawNum) {
        this.drawNum = drawNum;
    }

    public Integer getCurNum() {
        return curNum;
    }

    public void setCurNum(Integer curNum) {
        this.curNum = curNum;
    }

    public Integer getSaleSwitch() {
        return saleSwitch;
    }

    public void setSaleSwitch(Integer saleSwitch) {
        this.saleSwitch = saleSwitch;
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

    public Integer getRemainNum() {
        if (curNum == null) {
            return 0;
        }
        return drawNum - curNum;
    }

}
