package com.yanbao.constant;

/**
 * 兑换流程枚举类
 *
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum RechargeType {

    /**
     * 待付款
     */

    PENDING(0, "待付款"),
    /**
     * 已付款
     */
    CHECKED(1, "已付款"),

    /**
     * 充值成功
     */
    SUCCESS(2, "充值成功"),

    /**
     * 扫码支付成功（会有个中间状态 2 走默认充值流程）
     */
    TRANSFER_SUCCESS(3, "支付成功"),
    /**
     * 销售业绩向上统计60层成功
     */
    PERFORMANCE_SUCCESS(4, "销售业绩向上统计60层成功"),
    /**
     * 销售业绩向上统计60层失败
     */
    PERFORMANCE_FAIL(5, "销售业绩向上统计60层失败");

    private final Integer code;

    private final String msg;

    private RechargeType(Integer code, String msg) {
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
