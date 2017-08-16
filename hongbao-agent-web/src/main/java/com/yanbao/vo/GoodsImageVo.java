package com.yanbao.vo;

import java.io.Serializable;

public class GoodsImageVo implements Serializable {

	private static final long serialVersionUID = 8315942462674860954L;
	
	/**
     * 图片ID
     */
    private String imgId;
    /**
     * 图片路径
     */
    private String path;
    /**
     * 默认图片
     */
    private String goodsId;
    /**
     * 图片类型
     */
    private Integer type;

	/**
	 * 商品名称
	 */
	private String goodName;
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
}
