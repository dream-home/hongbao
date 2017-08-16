package com.yanbao.constant;

/**
 * 用户类型
 *
 * @author zzwei
 * @date 2017年6月17日
 */
public enum UserType {

    /**
     * 普通会员
     */
    COMMON(1, "普通会员"),
    /**
     * 商家
     */
    STORE(2, "商家"),
    /**
     * 代理
     */
    AGENT(3, "商家");

    private final Integer code;

    private final String msg;

    private UserType(Integer code, String msg) {
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
