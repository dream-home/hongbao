package com.mall.model;

import java.util.Date;

import com.yanbao.core.model.SimpleModel;


/**
 * 商品表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class Goods extends SimpleModel {

    private static final long serialVersionUID = 6468123345520931799L;
    /**
     * 商品分类ID
     */
    private String goodsSortId;
    /**
     * 商铺Id
     */
    private String storeId;
    /**
     * 商铺名称
     */
    private String storeName;
    /**
     * 名称
     */
    private String name;
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
     * 商品介绍
     */
    private String detail;
    /**
     * 商品类型：0：系统发布：1：商家发布
     */
    private Integer goodsType;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 当前期数编号
     */
    private Integer curIssueNo;
    /**
     * 当前期数Id
     */
    private String curIssueId;
    /**
     * 是否置顶：0：否，1：是
     */
    private Integer isTop;
    /**
     * 是否推荐：0：否；1：是
     */
    private Integer isRecommend;
    /**
     * 是否委托出售：0：否，1：是
     */
    private Integer saleSwitch;
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
     * Ep折扣率
     */
    private Double discountEP = 0d;
    /**
     * 商家原价
     */
    private Double originalPrice;
    /**
     * 审核状态 1：未审核 2：审核通过 3：审核不通过
     */
    private Integer verify;
    /**
     * 累计销售商品数量
     */
    private Integer sumSaleCount;
    
    private Date fromTime;
    
    private Date stopTime;

    /**
     * @return the originalPrice
     */
    public Double getOriginalPrice() {
        return originalPrice;
    }

    /**
     * @param originalPrice the originalPrice to set
     */
    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getGoodsSortId() {
        return goodsSortId;
    }

    public void setGoodsSortId(String goodsSortId) {
        this.goodsSortId = goodsSortId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getCurIssueNo() {
        return curIssueNo;
    }

    public void setCurIssueNo(Integer curIssueNo) {
        this.curIssueNo = curIssueNo;
    }

    public String getCurIssueId() {
        return curIssueId;
    }

    public void setCurIssueId(String curIssueId) {
        this.curIssueId = curIssueId;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getSaleSwitch() {
        return saleSwitch;
    }

    public void setSaleSwitch(Integer saleSwitch) {
        this.saleSwitch = saleSwitch;
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

    public Double getDiscountEP() {
        return discountEP;
    }

    public void setDiscountEP(Double discountEP) {
        this.discountEP = discountEP;
    }

	public Integer getVerify() {
		return verify;
	}

	public void setVerify(Integer verify) {
		this.verify = verify;
	}

    public Integer getSumSaleCount() {
        return sumSaleCount;
    }

    public void setSumSaleCount(Integer sumSaleCount) {
        this.sumSaleCount = sumSaleCount;
    }

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
    
}
