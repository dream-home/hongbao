package com.mall.model;


import com.yanbao.core.model.SimpleModel;

/**
 * 订单回调类型配置表
 *
 * @date 2018年08月29日
 */
public class OrderTypeModel extends SimpleModel {

    private static final long serialVersionUID = -7693797776373276572L;
    /**
     * 业务类型
     */
    private Integer type;
    /**
     * token
     */
    private String token;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
