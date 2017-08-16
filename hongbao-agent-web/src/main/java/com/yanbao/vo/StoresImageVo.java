package com.yanbao.vo;

import java.io.Serializable;
import java.util.List;

public class StoresImageVo implements Serializable {

	private static final long serialVersionUID = 8315942462674860954L;
	
	/**
     * 商品分类ID
     */
    private String storeId;
    /**
     * 商铺名称
     */
    private String storeName;
    /**
     * 默认图片
     */
    private String icon;
    /**
     * 关联商品
     */
	private List<GoodsVo> goods;
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public List<GoodsVo> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsVo> goods) {
		this.goods = goods;
	}
	
	
}
