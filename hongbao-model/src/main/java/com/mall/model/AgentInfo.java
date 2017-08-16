package com.mall.model;

import com.yanbao.core.model.SimpleModel;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 代理商基本信息表
 *
 * @author jay.zheng
 * @date 2017/6/16
 */
public class AgentInfo extends SimpleModel {

    private static final long serialVersionUID = -7693797333371276572L;
    private Integer uid;// 'UID',
    private String agentId;//'代理商家编号',
    private String nickName;// '昵称',
    private Integer sex;// '性别：0：男，1：女，2：保密',
    private String headImgUrl;// '头像地址',
    private String loginName;// '帐号',
    private String password;// '密码md5+salt',
    private String payPassWord;// '支付密码',
    private String salt;// '盐',
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

    private Date loginTime;// '登录时间',
    private Integer errorCount;// '错误次数'


    //ep值
    private Double exchangeEP;

    //消费ep值
    private Double consumeEP;
    //斗斗
    private Double doudou;
    //可用积分
    private Double score;

    // '省代名字',
    private String agentProvinceName;
    // '市代名字',
    private String agentCityName;
    // '县代名字',
    private String agentCountryName;


    //查询过滤条件
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromTime;
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;

    //下次月结日的时间
    private Date statisticsTime;


    public Date getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(Date statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

    public String getAgentProvinceName() {
        return agentProvinceName;
    }

    public void setAgentProvinceName(String agentProvinceName) {
        this.agentProvinceName = agentProvinceName;
    }

    public String getAgentCityName() {
        return agentCityName;
    }

    public void setAgentCityName(String agentCityName) {
        this.agentCityName = agentCityName;
    }

    public String getAgentCountryName() {
        return agentCountryName;
    }

    public void setAgentCountryName(String agentCountryName) {
        this.agentCountryName = agentCountryName;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Double getExchangeEP() {
        return exchangeEP;
    }

    public void setExchangeEP(Double exchangeEP) {
        this.exchangeEP = exchangeEP;
    }

    public Double getConsumeEP() {
        return consumeEP;
    }

    public void setConsumeEP(Double consumeEP) {
        this.consumeEP = consumeEP;
    }

    public Double getDoudou() {
        return doudou;
    }

    public void setDoudou(Double doudou) {
        this.doudou = doudou;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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

    public String getPayPassWord() {
        return payPassWord;
    }

    public void setPayPassWord(String payPassWord) {
        this.payPassWord = payPassWord;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }
}
