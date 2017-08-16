package com.yanbao.vo;

import java.io.Serializable;

public class GoodsVo implements Serializable {

	private static final long serialVersionUID = 8315942462674860954L;
	 /**
     * 商品Id
     */
    private String goodsId;
    
    /**
     * 商铺Id
     */
    private String storeId;
    
    /**
     * 图片
     */
    private String icon;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
    
}
