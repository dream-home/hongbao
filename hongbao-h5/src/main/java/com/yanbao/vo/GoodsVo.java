package com.yanbao.vo;

import java.util.List;

import com.yanbao.core.model.SimpleModel;

/**
 * 商品表
 * 
 * @author zhuzh
 * @date 2016年12月7日
 */
public class GoodsVo extends SimpleModel { 

	private static final long serialVersionUID = 6468123345520931799L;
	/** 商品分类ID */
	private String goodsSortId;
	/** 名称 */
	private String name;
	/** 图片 */
	private String icon;
	/** 商品介绍 */
	private String detail;
	/** 当前期数编号 */
	private Integer curIssueNo;
	/** 当前期数Id */
	private String curIssueId;
	/** 商品价格 */
	private Double price;
	/** 竞拍价 */
	private Double drawPrice;
	/** 参与竞拍人数 */
	private Integer drawNum;
	/** 当前竞拍人数 */
	private Integer curNum;
	/** 已参加竞拍 */
	private Integer isDraw = 0;
	/** 是否委托出售：0：否，1：是 */
	private Integer saleSwitch;
	/** 参与记录 */
	private List<DrawUserVo> drawUsers;
	/** 图片列表 */
	private List<ImageVo> icons;
	/** 商家赠送Ep */
	private Double businessSendEp = 0d;
	/** 商品库存 */
	private Integer stock = 0;
	/** 一级分销比例 */
	private Double firstReferrerScale = 0d;
	/** 二级分销比例 */
	private Double secondReferrerScale = 0d;
	/** 三级分销比例 */
	private Double thirdReferrerScale = 0d;
	/**商品原价*/
	private Double originalPrice;
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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

	public Integer getIsDraw() {
		return isDraw;
	}

	public void setIsDraw(Integer isDraw) {
		this.isDraw = isDraw;
	}

	public Integer getSaleSwitch() {
		return saleSwitch;
	}

	public void setSaleSwitch(Integer saleSwitch) {
		this.saleSwitch = saleSwitch;
	}

	public List<DrawUserVo> getDrawUsers() {
		return drawUsers;
	}

	public void setDrawUsers(List<DrawUserVo> drawUsers) {
		this.drawUsers = drawUsers;
	}

	public List<ImageVo> getIcons() {
		return icons;
	}

	public void setIcons(List<ImageVo> icons) {
		this.icons = icons;
	}

	public Double getBusinessSendEp() {
		return businessSendEp;
	}

	public void setBusinessSendEp(Double businessSendEp) {
		this.businessSendEp = businessSendEp;
	}

	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
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
}
