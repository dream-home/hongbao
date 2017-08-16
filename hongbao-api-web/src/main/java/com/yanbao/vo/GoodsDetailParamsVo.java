package com.yanbao.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情类
 * @date 2016年12月7日 
 */
public class GoodsDetailParamsVo implements Serializable {

	private static final long serialVersionUID = -8041418455885782640L;
	/** 商品id */
	private String goodsId;
	
	private List<GoodsDetailVo> goodsDetail;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public List<GoodsDetailVo> getGoodsDetail() {
		return goodsDetail;
	}

	public void setGoodsDetail(List<GoodsDetailVo> goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

	
	
	
}
