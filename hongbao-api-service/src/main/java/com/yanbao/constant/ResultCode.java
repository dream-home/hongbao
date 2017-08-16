package com.yanbao.constant;

/**
 * 返回码定义
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum ResultCode {
	/** 失败 */
	ERROR(9999, "失败"),

	/** 成功 **/
	SUCCESS(200, "成功"),

	/** 无令牌 */
	NO_TOKEN(0, "登录失效"),

	/** 登录失效 */
	NO_LOGIN(-1, "登录失效，请重新登录");

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
