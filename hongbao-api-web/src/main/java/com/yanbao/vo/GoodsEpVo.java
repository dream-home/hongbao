package com.yanbao.vo;

import java.util.List;

import com.yanbao.core.model.SimpleModel;

/**
 * ep商品表
 * 
 * @author zzwei
 * @date 2017年03月03日
 */
public class GoodsEpVo extends SimpleModel {

	private static final long serialVersionUID = 6468123345520931799L;
	/** 商品分类ID */
	private String goodsSortId;
	/** 商品EP*/
	private Double price;
	/** 名称 */
	private String name;
	/** 图片 */
	private String icon;
	/** 商品介绍 */
	private String detail;
	/** 图片列表 */
	private List<ImageVo> icons;
	/** 库存 */
	private Double stock;

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public String getGoodsSortId() {
		return goodsSortId;
	}
	public void setGoodsSortId(String goodsSortId) {
		this.goodsSortId = goodsSortId;
	}
	
	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}
	/**
	 * @return the icons
	 */
	public List<ImageVo> getIcons() {
		return icons;
	}
	/**
	 * @param icons the icons to set
	 */
	public void setIcons(List<ImageVo> icons) {
		this.icons = icons;
	}
	
}
