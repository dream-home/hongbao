package com.yanbao.constant;

/**
 * 标准状态枚举类 <br/>
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum StatusType {

	/** 否/失败/禁用/下架 /未读 */
	FALSE(0, "否"),

	/** 是/成功/启用/上架 /已读 */
	TRUE(1, "是"),

	/** 已删除 */
	DEL(2, "已删除"),
	
	/** 用户数据分组异常 */
	EXCEPTION(20, "用户数据分组异常");

	private final Integer code;

	private final String msg;

	private StatusType(Integer code, String msg) {
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
