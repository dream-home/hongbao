package com.yanbao.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;

/**
 * 代理登录vo类
 * Created by Administrator on 2017/6/16.
 */
public class AgentLoginVo implements Serializable {

    private static final long serialVersionUID = -8261886280592314119L;

    private String token;

    private String id;
    private Integer uid;// 'UID',
    private String agentId;//'代理商家编号',
    private String nickName;// '昵称',
    private Integer sex;// '性别：0：男，1：女，2：保密',
    private String headImgUrl;// '头像地址',
    private String phone;//'手机号',
    private String email;// '邮箱',
    private String userName;// '真实姓名',
    private String IDCard;// '身份证编号',
    private String bankCard;// '银行卡号',
    private String bankTypeCode;// '银行类型',
    private String bankType;// '银行中文名',
    private String bankBranch;// '开户支行中文名',
    private String company;// '公司名称',
    private String servicePhone;// '客服电话',
    private String address;// '地址',
    private Integer agentLevel;// '代理等级',
    private String licenseId;// '工商注册机构代码',
    private String agentAreaId;// '代理地区id',
    private String agentProvince;// '省代',
    private String agentCity;// '市代',
    private String agentCountry;// '县代',
    private String cardIconId;// '身份证照片id',
    private String licenseIconId;// '执照照片id',
    private Integer errorCount;// '错误次数'
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;//创建时间
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updateTime;//更新时间
    private Integer status;//状态
    private String remark;//备注

    /**登录用户名**/
    private String loginName;
    /**密码**/
    private String password;
    /**登录验证码key**/
    private String key;
    /**登陆获取图片验证码**/
    private String picCode;
    /**
     * 登录类型
     */
    private Integer agentType;
    /**
     * 权限
     */
    private List<Integer> menuPermissionsList;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankTypeCode() {
        return bankTypeCode;
    }

    public void setBankTypeCode(String bankTypeCode) {
        this.bankTypeCode = bankTypeCode;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(Integer agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getAgentAreaId() {
        return agentAreaId;
    }

    public void setAgentAreaId(String agentAreaId) {
        this.agentAreaId = agentAreaId;
    }

    public String getAgentProvince() {
        return agentProvince;
    }

    public void setAgentProvince(String agentProvince) {
        this.agentProvince = agentProvince;
    }

    public String getAgentCity() {
        return agentCity;
    }

    public void setAgentCity(String agentCity) {
        this.agentCity = agentCity;
    }

    public String getAgentCountry() {
        return agentCountry;
    }

    public void setAgentCountry(String agentCountry) {
        this.agentCountry = agentCountry;
    }

    public String getCardIconId() {
        return cardIconId;
    }

    public void setCardIconId(String cardIconId) {
        this.cardIconId = cardIconId;
    }

    public String getLicenseIconId() {
        return licenseIconId;
    }

    public void setLicenseIconId(String licenseIconId) {
        this.licenseIconId = licenseIconId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPicCode() {
        return picCode;
    }

    public void setPicCode(String picCode) {
        this.picCode = picCode;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
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

    public Integer getAgentType() {
        return agentType;
    }

    public void setAgentType(Integer agentType) {
        this.agentType = agentType;
    }

	public List<Integer> getMenuPermissionsList() {
		return menuPermissionsList;
	}

	public void setMenuPermissionsList(List<Integer> menuPermissionsList) {
		this.menuPermissionsList = menuPermissionsList;
	}

    
}
