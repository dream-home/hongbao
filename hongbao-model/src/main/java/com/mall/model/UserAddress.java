package com.mall.model;

import com.yanbao.core.model.SimpleModel;
import com.yanbao.util.EmojiUtil;

/**
 * 用户收货地址表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class UserAddress extends SimpleModel {

    private static final long serialVersionUID = 24149337328130052L;
    /** 用户ID */
    private String userId;
    /** 收件人信息 */
    private String receiveName;
    /** 收件人联系号码 */
    private String receivePhone;
    /** 省 */
    private String province;
    /** 市 */
    private String city;
    /** 县 */
    private String county;
    /** 详细地址 */
    private String addr;
    /** 是否默认地址：0：否，1：是 */
    private Integer isDefault;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        addr = EmojiUtil.replaceEmoji(addr, "");
        this.addr = addr;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }
}
