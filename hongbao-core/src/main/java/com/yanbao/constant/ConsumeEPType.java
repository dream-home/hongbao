package com.yanbao.constant;

/**
 * 消费ep流水类型
 *
 * @author zzwei
 * @date 2017年06月17日
 */
public enum ConsumeEPType {

    /**
     * EP商城兑换
     */
    MALL_EXCHANGE(1, "EP商城兑换 "),

    /**
     * 升级续费
     */
    UPGRADE(2, "升级续费"),

    /**
     * EP兑换斗斗
     */
    EP_TO_DOU(3, "EP兑换斗斗"),

    /**
     * 加入合伙人
     */
    JOIN_PARTNER(4, "加入合伙人");

    private final Integer code;

    private final String msg;

    private ConsumeEPType(Integer code, String msg) {
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
