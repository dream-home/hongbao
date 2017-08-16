package com.yanbao.constant;

/**
 * 二维码参数枚举类 <br/>
 *
 * @author zcj
 * @date 2017年05月17日
 */
public enum PayDistributionType {

    /**
     * 面对面扫码
     */
    member(1, "面对面扫码"),

    /**
     * 商户二维码支付
     */
    store(2, "商户二维码支付");

    private final Integer code;

    private final String msg;

    private PayDistributionType(Integer code, String msg) {
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
