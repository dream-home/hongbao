package com.yanbao.constant;

/**
 * 流水类型枚举类
 *
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum RecordType {

    /**
     * 充值
     */
    RECHARGE(0, "充值"),
    /**
     * 兑换
     */
    EXCHANGE(1, "兑换"),
    /**
     * 赠送
     */
    DONATE(2, "赠送/收到"),
    /**
     * 斗拍消费
     */
    DRAW(3, "斗拍消费"),
    /**
     * 斗拍中奖
     */
    DRAWWIN(4, "斗拍中奖"),
    /**
     * 委托出售
     */
    SALE(5, "委托出售"),
    /**
     * 积分购买
     */
    BUY(6, "积分购买"),
    /**
     * 退款
     */
    ROLLBACK(7, "退款"),
    /**
     * 分销奖励
     */
    AWARD(8, "分销奖励"),
    /**
     * 系统维护/商家经费
     */
    SYSTEM_FEE(9, "商家服务费"),
    /**
     * 商家销售收益
     */
    STORE_INCOME(10, "商家销售收益"),
    /**
     * 直接购买
     */
    PURCHASE(11, "直接购买"),
    /**
     * 商家赠送EP值
     */
    SENDEP(12, "商家赠送EP值"),
    /**
     * 签到所得
     */
    SIGNEP(13, "签到所得"),
    /**
     * 续费消费EP值
     */
    CONSUMEEP(14, "消费EP值"),
    /**
     * 邀请好友赠送EP值
     */
    INVITEREP(15, "邀请好友赠送EP值"),
    /**
     * 绑定手机号赠送EP值
     */
    BINDPHONEEP(16, "绑定手机号赠送EP值 "),
    /**
     * 绑定手机号系统支付EP值
     */
    SYSBINDPHONEEP(17, "绑定手机号系统支付EP值"),
    /**
     * EP商品兑换
     */
    EXCHANGEEP(18, "EP商品兑换"),
    /**
     * 商家扫码支付
     */
    SCANCODE_STORE(19, "扫码支付"),
    /**
     * 用户扫码支付
     */
    SCANCODE_USER(20, "扫码支付"),
    /**
     * 扫码支付赠送EP
     */
    SCANCODE_EP(21, "扫码支付赠送EP"),
    /**
     * 区域维护费
     */
    SYS_TRADE_RATE(22, "区域维护费"),
    /**
     * 商家二维码-用户扫码支付
     */
    STORE_SCANCODE_USER(23, "商家二维码-用户扫码支付"),
    /**
     * 商家二维码-商家收款
     */
    STORE_SCANCODE_STORE(24, "商家二维码-商家收款"),
    /**
     * 商家二维码-赠送EP
     */
    STORE_SCANCODE_EP(25, "商家二维码-赠送EP"),

    /**
     * 合伙人-支付
     */
    JOIN_PAY(26, "加入合伙人-支付"),
    /**
     * 签到补贴金额
     */
    SUBSIDY_SIGNEP(27, "签到补贴金额"),
    /**
     * Ep折扣优惠
     */
    USER_DISCOUNT_EP(28,"Ep折扣优惠"),
    /**
     * 斗斗签到
     */
    SIGN_DOUDOU(29,"斗斗签到"),
    /**
     * EP兑换斗斗
     */
    EP_TO_DOUDOU(30, "EP兑换斗斗"),
    /**
     * 邀请好友赠送斗斗
     */
    INVITERDOUDOU(31, "邀请好友赠送斗斗"),
    /**
     * 绑定手机号赠送斗斗
     */
    BINDPHONEDOUDOU(32, "绑定手机号赠送斗斗"),
    /**
     * 直接购买赠送斗斗(不使用ep折扣优惠的情况下)
     */
    PURCHASE_DOUDOU(33, "直接购买商品赠送斗斗"),
    /**
     * 面对面扫码支付赠送斗斗
     */
    SCANCODE_DOUDOU(34, "面对面扫码支付赠送斗斗"),
    /**
     * 商家固定二维码扫码支付赠送斗斗
     */
    STORE_SCANCODE_DOUDOU(35, "商家固定二维码扫码支付赠送斗斗"),
    /**
     * 合伙人业绩结算
     */
    PARTNER_GAIN_SCORE(36, "合伙人业绩结算"),
    /**
     * 合伙人-支付EP
     */
    JOIN_PAY_EP(100, "加入合伙人-支付EP");

    private final Integer code;

    private final String msg;

    private RecordType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static RecordType fromCode(Integer code) {
        try {
            for(RecordType recordType :RecordType.values()){
                if(recordType.getCode().intValue() == code.intValue()){
                    return recordType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static void main(String[] args) {
        System.out.println("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr8F4Z8FI+xDhJAKKq1lcUSC/GKlPJs6XwqR0fdQRtDnsnzoaT8DD2tX3azGXb3B3V8VVnMHlhfbXG+/k0RFJ0RdW0fvxpDfbutsKWbSzyR6jC5l9UaA4fB4A/ORzXsn4rljo6e9ORusLfcSUQu/uwtFUWrwwIhOVbL6fOKE2DCwetWGkcWeC3KmcgBUxcObyNTl4dTtTuMGbY0Un0jlS1+/qTqCW7E9uS52qKS76Tx5uJoD6l1zxiX4TfIKwUqpCXQDpDcHLjl0huTmTWCt1Hpop4NWXBNSrItAK2047gWTaQcRCwgkZhDYBl/CGesPPqQbrCyDkQpPXQThX8F5iFQIDAQAB".equals("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgf2aSgode8emw1QoZa/42YuxtUvUMp9roiOHkM72r6We49xhW+Fj0Zfw/nH+tAy32sXErdz4WRxUkMdUvaHRqPcH+LhVnpgnwB88P6lPIK3o9ln+otOCTWR2S7FXN5LWb0JX7nL8ygu087ch0gVqjYdxxJ2d4nW8qZUryYCDv/F0X2NWc7bjNkfNVVhqWCuGH8Y55eLU79vFYucEemmq4Wy28p793kontadEOt+GpE+iKMHz4lLvWluTlzCVTEbGBeUk6p5x9AsPrDGcVDhpipLxCTpomo46+ZZuD3CSWltsRD6C4NvvPEiMjtiSvklLB2r0ReGiUTpnITCRr2jZYQIDAQAB"));
    }

}
