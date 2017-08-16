package com.mall.model;


import com.yanbao.core.model.SimpleModel;

/**
 * Created by Administrator on 2017/7/26 0026.
 */
public class PartnerBillDetail extends SimpleModel {

    /**
     * 合伙人id
     */
    private String partnerId;
    /**
     * 推荐商家或会员id
     */
    private String userId;
    /**
     * 直推会员合伙人等级，普通会员为0，其他用户自己的grade'
     */
    private Integer userGrade;
    /**
     * 直推会员商家id，如果不是商家则为null
     */
    private String userStoreId;
    /**
     * 结算类型：1：提现业绩结算  2：ep业绩结算
     */
    private String type;
    /**
     * '结算单号'
     */
    private String orderNo;
    /**
     * 结算比例
     */
    private Double scale;
    /**
     * 结算金额：ep或余额
     */
    private Double amount;
    /**
     * '负责人姓名'
     */
    private String userName;
    /**
     * '手机号'
     */
    private String phone;
    /**
     * '地区id'
     */
    private String areaId;
    /**
     * '省代'
     */
    private String province;
    /**
     * '市代'
     */
    private String city;
    /**
     * '县代'
     */
    private String country;
    /**
     * '合伙人等级'
     */
    private Integer grade;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(Integer userGrade) {
        this.userGrade = userGrade;
    }

    public String getUserStoreId() {
        return userStoreId;
    }

    public void setUserStoreId(String userStoreId) {
        this.userStoreId = userStoreId;
    }
}
