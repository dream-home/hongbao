package com.yanbao.constant;

/**
 * 审核枚举类
 * 
 * @author zzwei
 * @date 2017年07月10日
 */
public enum VerifyType {

	/** 1：未审核 */
	CHECKING(1, "未审核"),

	/** 2：审核通过 */
	CHECKED(2, "审核通过"),

	/**3：审核不通过'*/
	REJECTED(3, "审核不通过");

	private final Integer code;

	private final String msg;

	private VerifyType(Integer code, String msg) {
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
