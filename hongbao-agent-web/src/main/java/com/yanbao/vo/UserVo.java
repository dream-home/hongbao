package com.yanbao.vo;

import com.yanbao.core.model.CustomDateSerializer;
import com.yanbao.util.EmojiUtil;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author zhuzh
 * @date 2016年12月12日
 */
public class UserVo implements Serializable {

	private static final long serialVersionUID = -8261886280598614119L;

	private String token;

	private String id;
	/** UID */
	private Integer uid;
	/** 帐号 */
	private String account;
	/** 是否设置登录密码 */
	private Integer isSetPassword = 1;
	/** 是否设置支付密码 */
	private Integer isSetPayPwd = 1;
	/** 手机号 */
	private String phone;
	/** 邮箱 */
	private String email;
	/** 真实姓名 */
	private String userName;
	/** 昵称 */
	private String nickName;
	/** 性别：0：男，1：女，2：保密 */
	private Integer sex;
	/** 头像地址 */
	private String headImgUrl;
	/** 可用积分（总积分=可用积分+冻结积分） */
	private String score;
	/** 创建时间 */
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date createTime;
	/** 备注 */
	private String remark;
	/** 商铺Id */
	private String storeId;

	/**登录用户名**/
	private String loginName;
	/**密码**/
	private String password;
	/**登录验证码key**/
	private String key;
	/**登陆获取图片验证码**/
	private String picCode;
	/** 极光推送ID */
	private String registrationId;
	/**签到次数*/
	private Integer signCount;
	/**签到领取的积分*/
	private Integer signEP;
	/**个人总EP*/
	private Double exchangeEP;
	/**消费EP*/
	private Double consumeEP;
	/** 签到等级 */
	private Integer grade;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getIsSetPassword() {
		return isSetPassword;
	}

	public void setIsSetPassword(Integer isSetPassword) {
		this.isSetPassword = isSetPassword;
	}

	public Integer getIsSetPayPwd() {
		return isSetPayPwd;
	}

	public void setIsSetPayPwd(Integer isSetPayPwd) {
		this.isSetPayPwd = isSetPayPwd;
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
		userName = EmojiUtil.replaceEmoji(userName);
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		nickName = EmojiUtil.replaceEmoji(nickName);
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the picCode
	 */
	public String getPicCode() {
		return picCode;
	}

	/**
	 * @param picCode the picCode to set
	 */
	public void setPicCode(String picCode) {
		this.picCode = picCode;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public Integer getSignCount() {
		return signCount;
	}

	public void setSignCount(Integer signCount) {
		this.signCount = signCount;
	}

	public Integer getSignEP() {
		return signEP;
	}

	public void setSignEP(Integer signEP) {
		this.signEP = signEP;
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
}
