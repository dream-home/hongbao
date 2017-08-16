package com.yanbao.constant;

/**
 * 店铺状态枚举类
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum GoodsDetailType {
	
	/** 审核不通过 */
	TEXT(1, "文本类型"),

	/** 图片 */
	IMG(2, "图片类型");



	private final Integer code;

	private final String msg;

	private GoodsDetailType(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
