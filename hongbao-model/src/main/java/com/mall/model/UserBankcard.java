package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * 银行卡表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class UserBankcard extends SimpleModel {

    private static final long serialVersionUID = 7665767193214955210L;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 银行ID
     */
    private String bankId;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 卡类型：0：银行卡，1：支付宝，2：微信支付
     */
    private Integer type;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;
    /**
     * 银行卡号/支付宝账号/微信支付账号
     */
    private String cardNo;
    /**
     * 是否默认：0：否，1：是
     */
    private Integer isDefault;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

}
