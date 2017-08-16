package com.yanbao.constant;

/**
 * 订单类型枚举类 <br/>
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum OrderType {

	DRAW(0, "竞拍中奖"),

	PURCHASE(1, "直接购买"),

	EXCHANGEEP(2, "EP兑换"),

	CARTPAY(3, "购物车购买"),

	EXCHANGE_EPTO_DOUDOU(2, "EP兑换斗斗"),;

	private final Integer code;

	private final String msg;

	private OrderType(Integer code, String msg) {
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
