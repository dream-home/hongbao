package com.yanbao.vo;

import java.io.Serializable;

/**
 * 商品销售统计
 * 
 * @author zhuzh
 * @date 2016年12月7日
 */
public class GoodsSalesVo implements Serializable {

	private static final long serialVersionUID = 8161946523476024256L;
	/** id */
	private String id;
	/** 名称 */
	private String name;
	/** 图片 */
	private String icon;
	/** 参与记录 */
	private Integer total;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
