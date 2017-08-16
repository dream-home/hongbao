package com.yanbao.constant;

/**
 * 竞拍中奖状态枚举类
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum GoodsWinType {

	/** 待处理 */
	PENDING(0, "待处理"),

	/** 已出售 */
	SALED(1, "已出售"),

	/** 已购买，待发货 */
	BUYED(2, "待发货"),

	/** 已发货 */
	DELIVERED(3, "已发货"),

	/** 已删除 */
	DEL(4, "已删除");

	private final Integer code;

	private final String msg;

	private GoodsWinType(Integer code, String msg) {
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
