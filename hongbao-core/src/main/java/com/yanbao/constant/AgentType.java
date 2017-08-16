package com.yanbao.constant;

/**
 * 判断代理还是员工登录
 * Created by Administrator on 2017/6/22.
 */
public enum AgentType {

    AGENT_TYPE(1, "代理登录"),

    STAFF_TYPE(2, "员工登录");

    private final Integer code;

    private final String msg;

    private AgentType(Integer code, String msg) {
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
