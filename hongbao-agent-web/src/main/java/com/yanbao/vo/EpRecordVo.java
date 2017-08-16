package com.yanbao.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/4.
 */
public class EpRecordVo implements Serializable {


    /** 1:普通会员  2：商家  3：代理商 */
    private java.lang.Integer userType;
    /** 订单号 */
    private java.lang.String orderNo;
    /** 送用户Id */
    private java.lang.String sendUserId;
    /** 收用户Id */
    private java.lang.String receiveUserId;
    /** 积分 */
    private java.lang.Double ep;
    /** 流水类型：1：商家赠送EP，2：EP商城兑换，3：面对面扫码折扣，4：商家二维码折扣；5：直接购买折扣  6：新用户绑定手机号获赠EP  7：新用户绑定手机号，推荐人获赠EP  8:EP兑换斗斗 9：公司管理员充值  10：代理互转  11：代理转会员 12：EP充值扣除 */
    private java.lang.Integer recordType;
    /** 流水类型 */
    private java.lang.String recordTypeDesc;
    /** 创建时间 */
    private java.util.Date createTime;
    /** 修改时间 */
    private java.util.Date updateTime;
    /** 状态 */
    private java.lang.Integer status;
    /** 备注 */
    private java.lang.String remark;
    /**  */
    private java.lang.String province;
    /**  */
    private java.lang.String city;
    /**  */
    private java.lang.String county;
    /**  */
    private java.lang.String addressId;
    /**
     * 赠送会员名称
     */
    private String sendUserName;
    /**
     * 收入会员名称
     */
    private String receiveUserName;

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public Double getEp() {
        return ep;
    }

    public void setEp(Double ep) {
        this.ep = ep;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getRecordTypeDesc() {
        return recordTypeDesc;
    }

    public void setRecordTypeDesc(String recordTypeDesc) {
        this.recordTypeDesc = recordTypeDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }
}
