package com.mall.model;


import com.yanbao.core.model.SimpleModel;

/**
 * 协议
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class Agreement extends SimpleModel {

    private static final long serialVersionUID = -2719854113713989591L;
    /**
     * 用户协议
     */
    private String userAgreement;
    /**
     * 店铺协议
     */
    private String storeAgreement;

    public String getUserAgreement() {
        return userAgreement;
    }

    public void setUserAgreement(String userAgreement) {
        this.userAgreement = userAgreement;
    }

    public String getStoreAgreement() {
        return storeAgreement;
    }

    public void setStoreAgreement(String storeAgreement) {
        this.storeAgreement = storeAgreement;
    }

}
