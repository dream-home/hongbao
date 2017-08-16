package com.yanbao.vo;

import java.util.List;

import com.yanbao.core.model.SimpleModel;

public class GoodIconsVo extends SimpleModel {
	private static final long serialVersionUID = 6468123345520931799L;
	/** 商品分类ID */
	private String goodsSortId;
	/** 名称 */
	private String name;
	/** 图片 */
	private String icon;
	/** 商品介绍 */
	private String detail;
	/** 商品价格 */
	private Double price;
	/** 图片列表 */
	private List<String> icons;
	
	/**商品库存  */
	private Integer stock;
	
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	/** 图片ID*/
	private List<String> iconKey;
	
	
	
	
	
	
	
	public List<String> getIconKey() {
		return iconKey;
	}
	public void setIconKey(List<String> iconKey) {
		this.iconKey = iconKey;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public List<String> getIcons() {
		return icons;
	}
	public void setIcons(List<String> icons) {
		this.icons = icons;
	}
	
	
	
	

}
