package com.yanbao.constant;

/**
 * 签到类型
 *
 * @author zzwei
 * @date 2017年06月17日
 */
public enum SignType {

    /**
     * 斗斗签到
     */
    DOUDOU_SIGN(1, "斗斗签到"),

    /**
     * 合伙人签到
     */
    PARTENER_SIGN(2, "合伙人签到"),

    /**
     * EP兑换成斗斗
     */
    EP_TO_DOU(3, "EP兑换成斗斗"),
    /**
     * 初次加入合伙人800EP转斗斗
     */
    JOIN_VIP_EP_TO_DOU(4, "升级VIP"),
    /**
     * 合伙人升级
     */
    UPGRADE(5, "合伙人升级"),
    /**
     * 邀请绑定赠送斗斗
     */
    BINDPHONE(6, "邀请绑定赠送斗斗"),
    /**
     * 注册绑定赠送斗斗
     */
    REGISTERPHONE(7, "注册绑定赠送斗斗"),
    /**
     * 直接购买赠送斗斗
     */
    PURCHASE_DOUDOU(8, "直接购买赠送斗斗"),
    /**
     * 商家二维码赠送斗斗
     */
    STORE_SCANCODE_DOUDOU(9, "商家二维码赠送斗斗"),
    /**
     * 面对面扫码赠送斗斗
     */
    SCANCODE_DOUDOU(10, "面对面码赠送斗斗");

    private final Integer code;

    private final String msg;

    private SignType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static SignType fromCode(Integer code) {
        try {
            for (SignType signType : SignType.values()) {
                if (signType.getCode().intValue() == code.intValue()) {
                    return signType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
