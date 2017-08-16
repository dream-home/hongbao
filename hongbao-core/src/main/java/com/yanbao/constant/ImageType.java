package com.yanbao.constant;

/**
 * 图片类型枚举类
 * 
 * @author zzwei
 * @date 2017年06月29日
 */
public enum ImageType {

	/** 身份证 */
	IDCARD(1, "身份证"),

	/** 资质 */
	LIENSCE(2, "资质"),

	/** 店铺 */
	STORE(3, "店铺"),

	/** 商品 */
	GOODS(4, "商品"),
	
	/** 代理资质 */
	LIENSC_EAgent(5, "代理资质");
	

	private final Integer code;

	private final String msg;

	private ImageType(Integer code, String msg) {
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
