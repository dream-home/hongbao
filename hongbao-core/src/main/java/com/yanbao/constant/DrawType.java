package com.yanbao.constant;

/**
 * 用户竞拍状态枚举类
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum DrawType {

	/** 待开奖 */
	PENDING(0, "待开奖"),

	/** 已开奖 */
	FINISH(1, "已开奖"),

	/** 已撤销 */
	CANCEL(2, "已撤销");

	private final Integer code;

	private final String msg;

	private DrawType(Integer code, String msg) {
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
