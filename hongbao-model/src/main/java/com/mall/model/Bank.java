package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 银行类
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class Bank extends SimpleModel {

    private static final long serialVersionUID = 7010695334996359784L;
    /**
     * 银行名称
     */
    private String name;
    /**
     * 银行icon
     */
    private String bankIcon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankIcon() {
        return bankIcon;
    }

    public void setBankIcon(String bankIcon) {
        this.bankIcon = bankIcon;
    }
}
