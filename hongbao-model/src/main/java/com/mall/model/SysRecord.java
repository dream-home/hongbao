package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * @author zyc
 * @date 2017-03-06 17:13
 */

public class SysRecord extends SimpleModel {

    private String orderNo;

    private String userId;

    private Double score;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


}
