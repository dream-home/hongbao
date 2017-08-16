package com.yanbao.constant;

import org.apache.poi.util.SystemOutLogger;

/**
 * 银行卡类型/充值来源枚举类
 *
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum BankCardType {

    /**
     * 银行卡
     */
    UNIONPAY(0, "银行卡"),

    /**
     * 支付宝
     */
    ALIPAY(1, "支付宝"),

    /**
     * 微信支付
     */
    WECHATPAY(2, "微信支付"),

    /**
     * 余额支付
     */
    BALANCE(3, "余额支付"),
    /**
     * 支付宝发起扫码支付
     */
    ALIPAY_STORE_SCAN(4, "支付宝发起扫码支付"),

    /**
     * 微信发起扫码支付
     */
    WECHATPAY_STORE_SCAN(5, "微信发起扫码支付"),


    SCAN_CODE_ALIPAY(11, "支付宝扫码支付"),

    SHARE_ALIPAY(12, "支付宝分享支付"),

    PURCHASE_ALIPAY(13,"直接购买之支付宝支付"),

    STORE_SCAN_APP_ALIPAY(14,"商家二维码APP支付宝支付"),

    STORE_SCAN_PAGE_ALIPAY(15,"商家二维码网页支付宝支付"),

    JOIN_ALIPAY(16, "支付宝加入合伙人支付"),

    SCAN_CODE_WEIXIN(21, "微信扫码支付"),

    SHARE_WEIXIN(22, "微信分享支付"),

    PURCHASE_WEIXIN(23,"直接购买之微信支付"),

    STORE_SCAN_APP_WEIXIN(24,"商家二维码APP微信支付"),

    STORE_SCAN_PAGE_WEIXIN(25,"商家二维码网页微信支付"),

    JOIN_WEIXIN(26, "微信加入合伙人支付"),

    SCAN_CODE_BALANCE(31, "余额扫码支付"),

    PURCHASE_BALANCE(32, "余额直接购买支付"),

    STORE_SCAN_CODE_BALANCE(33, "商家二维码余额支付"),

    JOIN_BALANCE(34, "余额加入合伙人支付"),

    ADMIN_RECHARGE(99, "管理员后台充值");



    private final Integer code;

    private final String msg;

    private BankCardType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public static BankCardType fromCode(Integer code) {
        try {
            for(BankCardType bankCardType :BankCardType.values()){
                if(bankCardType.getCode().intValue() == code.intValue()){
                    return bankCardType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        int source=25;
        System.out.println( BankCardType.fromCode(source).getMsg());
    }
}
