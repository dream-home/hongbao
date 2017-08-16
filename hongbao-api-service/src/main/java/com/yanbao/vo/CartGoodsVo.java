package com.yanbao.vo;

/**
 *  购物车商品列表
 * 
 * @author zhuzh
 * @date 2016年12月7日
 */
public class CartGoodsVo {
	private String  goodsId;
	private String  goodsName;
	private String  icon;
	private Integer  num;
	private Double price;

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

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
	
