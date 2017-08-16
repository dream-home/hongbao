package com.yanbao.constant;

/**
 * 性别枚举类
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum SexType {

	/** 男 */
	male(0, "男"),

	/** 女 */
	female(1, "女"),

	/** 保密 */
	other(2, "保密");

	private final Integer code;

	private final String msg;

	private SexType(Integer code, String msg) {
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
