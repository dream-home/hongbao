package com.yanbao.constant;

/**
 * 兑换流程枚举类
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum ExchangeType {

	/** 待审核 */
	PENDING(0, "待审核"),

	/** 审核通过 */
	CHECKED(1, "审核通过"),

	/** 审核不通过 */
	UNCHECK(2, "审核不通过"),

	/** 兑换成功 */
	SUCCESS(3, "兑换成功"),

	/** 兑换失败 */
	FAILED(4, "兑换失败");

	private final Integer code;

	private final String msg;

	private ExchangeType(Integer code, String msg) {
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
