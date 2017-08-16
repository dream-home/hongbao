package com.yanbao.constant;

/**
 * @author jay.zheng
 * @date 2017/6/17
 */
public enum EPRecordType {

    /**
     * 商家赠送EP值
     */
    SENDEP(1, "商家赠送EP值"),
    /**
     * EP商城兑换
     */
    MALL_EXCHANGE(2, "EP商城兑换 "),
    /**
     * 面对面扫码折扣
     */
    FACE_SCAN_DISCOUNT(3, "面对面扫码折扣 "),
    /**
     * 商家二维码折扣
     */
    STORE_SCAN_DISCOUNT(4, "商家二维码折扣 "),
    /**
     * 直接购买折扣
     */
    SHOPPING_DISCOUNT(5, "直接购买折扣 "),
    /**
     * 新用户绑定手机号获赠EP
     */
    BIND_PHONE_USER_SENDEP(6, "新用户完善资料获赠EP "),
    /**
     * 新用户绑定手机号推荐人获赠EP
     */
    BIND_PHONE_RECOMMEND_SENDEP(7, "新用户完善资料推荐人获赠EP "),
    /**
     * EP兑换斗斗
     */
    EP_TO_DOUDOU(8, "EP兑换斗斗"),
    /**
     * 公司管理员充值
     */
    ADMIN_RECHARGE(9, "公司管理员充值"),
    /**
     * 代理互转
     */
    AGENT_TO_AGENT(10, "代理互转"),
    /**
     * 代理转会员
     */
    AGENT_TO_USER(11, "代理转会员"),
    /**
     * EP充值扣除
     */
    EP_RECHARGE_REDUCE(12, "EP充值扣除"),
    /**
     * 加入合伙人
     */
    JOIN_PARTNER(13, "加入合伙人"),
    /**
     * 合伙人签到
     */
    PARTNER_SIGNIN(14, "合伙人签到"),
    /**
     * 斗斗签到
     */
    DOUDOU_SIGNIN(15, "斗斗签到"),
    /**
     * 面对面扫码支付赠送EP
     */
    SCANCODE_EP(16, "面对面扫码支付赠送EP"),
    /**
     * 商家二维码-赠送EP
     */
    STORE_SCANCODE_EP(17, "商家二维码-赠送EP"),

    /**
     * 直接购买统计ep
     */
    //STORE_SEND_EP(18, "直接购买统计ep"),

    /**
     * 加入合伙人，面对面扫码支付、商家二维码扫码支付,后台记录，不给用户展示，1680，只是代理统计用到
     */
    JOIN_SCAN_EP(19, "加入合伙人使用扫码支付消费ep");

    private final Integer code;

    private final String msg;

    private EPRecordType(Integer code, String msg) {
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
