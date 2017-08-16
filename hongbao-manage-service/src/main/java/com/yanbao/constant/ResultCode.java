package com.yanbao.constant;

/**
 * 返回码定义
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum ResultCode {

	/** 登录失效 */
	NO_LOGIN(1403, "登录失效，请重新登录");

	private final Integer code;

	private final String msg;

	private ResultCode(Integer code, String msg) {
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
