package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zzwei
 * @date 2017年07月17日
 */
public class AddressVo implements Serializable {

	private static final long serialVersionUID = -8261886280598614119L;


	/** '省' */
	private String province;
	/** '市' */
	private String city;
	/** '区' */
	private String county;

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
}
