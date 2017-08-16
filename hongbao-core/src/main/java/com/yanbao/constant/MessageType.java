package com.yanbao.constant;

/**
 * 消息类型枚举类 <br/>
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum MessageType {

	SYSTEM(0, "系统消息 "),

	EXPENSE(1, "消费消息"),

	WIN(2, "中拍消息"),

	PURCHASE(3, "直接购买"),

	RECHARGE(4, "充值提醒"),

	EXCHANGE(5, "兑换提醒"),

	DONATE(6, "获赠提醒"),

	SHIPPED(7, "发货通知"),

	SENDEP(8, "获赠EP"),

	ORDER(9, "订单处理"),

	EXCHANGEEP(10, "EP兑换"),

	SCANCODE(11, "扫码支付"),

	SCANCODE_EP(12, "扫码赠送EP"),

	STORE_SCANCODE_USER(13, "商家二维码-扫码支付"),

	STORE_SCANCODE_STORE(14, "商家二维码-商家收款"),

	STORE_SCANCODE_EP(15, "商家二维码-赠送EP"),

	JOIN_PAY(16, "加入合伙人");

	private final Integer code;

	private final String msg;

	private MessageType(Integer code, String msg) {
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
