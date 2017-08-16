package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zzwei
 * @date 2017年07月12日
 */
public class UserV42Vo implements Serializable {
	/** 手机号 */
	private String phone;
	/** 地区代码 */
	private String areaId;
	/** 省 */
	private String province;
	/** 市 */
	private String city;
	/** 区 */
	private String county;
	/** 短信验证码 */
	private String smsCode;
	/**登录密码**/
	private String password;
	/**支付密码**/
	private String payPwd;

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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
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
}
