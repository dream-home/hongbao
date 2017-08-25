package com.yanbao.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yanbao.core.model.CustomDateSerializer;
import com.mall.model.Bank;
import com.mall.model.UserAddress;
import com.mall.model.UserBankcard;
import com.yanbao.util.EmojiUtil;

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

	/** 银行卡列表 */
	List<Bank> bankList;

	/** 用户银行卡 */
	UserBankcard userBankcard;

	/** 用户收货地址 */
	UserAddress userAddress;
	
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
	/**个人总斗斗数量*/
	private Double doudou;
	private AddressVo vo;
	/**是否绑定微信*/
	private Integer isBindWeixin=0;
	/**是否完善资料*/
	private Integer isCompleteInfo=0;
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
	 * 是否弹窗显示用户绑定微信
	 */
	private Integer isBindSHWeiXin;

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

	public Integer getIsBindWeixin() {
		return isBindWeixin;
	}

	public void setIsBindWeixin(Integer isBindWeixin) {
		this.isBindWeixin = isBindWeixin;
	}

	public Integer getIsCompleteInfo() {
		return isCompleteInfo;
	}

	public void setIsCompleteInfo(Integer isCompleteInfo) {
		this.isCompleteInfo = isCompleteInfo;
	}

	public AddressVo getVo() {
		return vo;
	}

	public void setVo(AddressVo vo) {
		this.vo = vo;
	}

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

	public List<Bank> getBankList() {
		return bankList;
	}

	public void setBankList(List<Bank> bankList) {
		this.bankList = bankList;
	}

	public UserBankcard getUserBankcard() {
		return userBankcard;
	}

	public void setUserBankcard(UserBankcard userBankcard) {
		this.userBankcard = userBankcard;
	}

	public UserAddress getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
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

	public Double getDoudou() {
		return doudou;
	}

	public void setDoudou(Double doudou) {
		this.doudou = doudou;
	}

	public Integer getIsBindSHWeiXin() {
		return isBindSHWeiXin;
	}

	public void setIsBindSHWeiXin(Integer isBindSHWeiXin) {
		this.isBindSHWeiXin = isBindSHWeiXin;
	}
}
