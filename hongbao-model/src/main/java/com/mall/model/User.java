package com.mall.model;

import com.yanbao.core.model.CustomDateSerializer;
import com.yanbao.core.model.SimpleModel;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 用户表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class User extends SimpleModel {

    private static final long serialVersionUID = -2932280543213916052L;
    /**
     * UID
     */
    private Integer uid;
    /**
     * 帐号
     */
    private String account;
    /**
     * 密码md5+salt
     */
    private String password;
    /**
     * 支付密码
     */
    private String payPwd;
    /**
     * 微信openId
     */
    private String weixin;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 真实姓名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别：0：男，1：女，2：保密
     */
    private Integer sex;
    /**
     * 头像地址
     */
    private String headImgUrl;
    /**
     * 盐
     */
    private String salt;
    /**
     * 可用积分（总积分=可用积分+冻结积分）
     */
    private Double score;
    /**
     * 冻结积分（总积分=可用积分+冻结积分
     */
    private Double freezeScore;
    /**
     * 一度推荐人
     */
    private String firstReferrer;
    /**
     * 二度推荐人
     */
    private String secondReferrer;
    /**
     * 三度推荐人
     */
    private String thirdReferrer;
    /**
     * 商铺Id
     */
    private String storeId;
    /**
     * 邀请码
     */
    private Integer inviteCode;
    /**
     * 是否客服帐号：0：否，1：是
     */
    private Integer isKF;
    /**
     * 用户分组：A/B/C
     */
    private String groupType;
    /**
     * A组下线统计
     */
    private Integer groupChildCountA;
    /**
     * B组下线统计
     */
    private Integer groupChildCountB;
    /**
     * C组下线统计
     */
    private Integer groupChildCountC;
    /**
     * 极光推送ID
     */
    private String registrationId;
    /**
     * 个人总EP
     */
    private Double exchangeEP;
    /**
     * 消费EP
     */
    private Double consumeEP;
    /**
     * 签到等级
     */
    private Integer grade;
    /**
     * 剩余签到次数
     */
    private Integer remainSign;
    /**
     * 合伙人数量统计一部
     */
    private Double performanceOne;
    /**
     * 合伙人数量统计二部
     */
    private Double performanceTwo;
    /**
     * 合伙人数量统计三部
     */
    private Double performanceThree;
    /**
     * 用户签到时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date signTime;
    /**
     * 累计最大赠送EP
     */
    private Double bindEP;
    /**
     * 统计校验
     */
    private Integer checked;
    /**
     * 所属层级
     */
    private Integer levles;
    /***登录时间**/
    private Date loginTime;
    /***openId**/
    private String openId;
    /**
     * 个人总斗斗数量
     */
    private Double doudou;
    /**
     * 斗斗用户签到时间
     */
    @JsonSerialize(using = com.yanbao.core.model.CustomDateSerializer.class)
    private Date douSignTime;
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
     * 区域代码
     */
    private String areaId;
    /**
     * 累计最大赠送斗斗
     */
    private Double bindDoudou;
    /**
     * 加入合伙人时间
     */
    private Date joinPartnerTime;

    /**
     * 非数据库字段----------------------------------------------------
     */
    /**
     * 用户详细地址
     */
    private String addr;

    private Date fromTime;

    private Date toTime;

    /**
     * 炎宝开放平台用户oldUnionId
     */
    private String oldUnionId;

    public Double getBindEP() {
        return bindEP;
    }

    /**
     * @param bindEP the bindEP to set
     */
    public void setBindEP(Double bindEP) {
        this.bindEP = bindEP;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getFreezeScore() {
        return freezeScore;
    }

    public void setFreezeScore(Double freezeScore) {
        this.freezeScore = freezeScore;
    }

    public String getFirstReferrer() {
        return firstReferrer;
    }

    public void setFirstReferrer(String firstReferrer) {
        this.firstReferrer = firstReferrer;
    }

    public String getSecondReferrer() {
        return secondReferrer;
    }

    public void setSecondReferrer(String secondReferrer) {
        this.secondReferrer = secondReferrer;
    }

    public String getThirdReferrer() {
        return thirdReferrer;
    }

    public void setThirdReferrer(String thirdReferrer) {
        this.thirdReferrer = thirdReferrer;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Integer getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(Integer inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getIsKF() {
        return isKF;
    }

    public void setIsKF(Integer isKF) {
        this.isKF = isKF;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Integer getGroupChildCountA() {
        return groupChildCountA;
    }

    public void setGroupChildCountA(Integer groupChildCountA) {
        this.groupChildCountA = groupChildCountA;
    }

    public Integer getGroupChildCountB() {
        return groupChildCountB;
    }

    public void setGroupChildCountB(Integer groupChildCountB) {
        this.groupChildCountB = groupChildCountB;
    }

    public Integer getGroupChildCountC() {
        return groupChildCountC;
    }

    public void setGroupChildCountC(Integer groupChildCountC) {
        this.groupChildCountC = groupChildCountC;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getRemainSign() {
        return remainSign;
    }

    public void setRemainSign(Integer remainSign) {
        this.remainSign = remainSign;
    }

    public Double getPerformanceOne() {
        return performanceOne;
    }

    public void setPerformanceOne(Double performanceOne) {
        this.performanceOne = performanceOne;
    }

    public Double getPerformanceTwo() {
        return performanceTwo;
    }

    public void setPerformanceTwo(Double performanceTwo) {
        this.performanceTwo = performanceTwo;
    }

    public Double getPerformanceThree() {
        return performanceThree;
    }

    public void setPerformanceThree(Double performanceThree) {
        this.performanceThree = performanceThree;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Integer getLevles() {
        return levles;
    }

    public void setLevles(Integer levles) {
        this.levles = levles;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Double getDoudou() {
        return doudou;
    }

    public void setDoudou(Double doudou) {
        this.doudou = doudou;
    }

    public Date getDouSignTime() {
        return douSignTime;
    }

    public void setDouSignTime(Date douSignTime) {
        this.douSignTime = douSignTime;
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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Double getBindDoudou() {
        return bindDoudou;
    }

    public void setBindDoudou(Double bindDoudou) {
        this.bindDoudou = bindDoudou;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    public Date getJoinPartnerTime() {
        return joinPartnerTime;
    }

    public void setJoinPartnerTime(Date joinPartnerTime) {
        this.joinPartnerTime = joinPartnerTime;
    }

    public String getOldUnionId() {
        return oldUnionId;
    }

    public void setOldUnionId(String oldUnionId) {
        this.oldUnionId = oldUnionId;
    }
}
