package com.yanbao.constant;

/**
 * 场景枚举类
 * 
 * @author jay.zheng
 * @date 2017年07月27日
 */
public enum ScenesType {

	/** 微店 */
	WEIXIN_STORE(1, "微店");

	private final Integer code;

	private final String msg;

	private ScenesType(Integer code, String msg) {
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
