package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 商品类型表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class GoodsSort extends SimpleModel {

    private static final long serialVersionUID = 3682914148654318534L;
    /**
     * 名称
     */
    private String name;
    /**
     * 图片
     */
    private String icon;
    /**
     * 排序号
     */
    private Integer rank;
    /**
     * 商品类目
     */
    private String goodSortType;

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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

	public String getGoodSortType() {
		return goodSortType;
	}

	public void setGoodSortType(String goodSortType) {
		this.goodSortType = goodSortType;
	}


}
