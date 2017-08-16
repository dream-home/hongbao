package com.yanbao.constant;

/**
 * ep兑换斗斗开关
 *
 * @author zzwei
 * @date 2017年6月17日
 */
public enum SwitchType {

    /**
     * 关
     */
    OFF(0, "关"),
    /**
     *开
     */
    ON(1, "开");

    private final Integer code;

    private final String msg;

    private SwitchType(Integer code, String msg) {
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
