package com.yanbao.constant;

/**
 * 期数竞拍状态枚举类
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum IssueType {

	/** 竞拍中 */
	PENDING(0, "竞拍中"),

	/** 待开奖 */
	PROCESSING(1, "待开奖"),

	/** 已开奖 */
	FINISH(2, "已开奖");

	private final Integer code;

	private final String msg;

	private IssueType(Integer code, String msg) {
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
