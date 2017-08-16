package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2017年1月12日
 */
public class ParameterVo implements Serializable {

	private static final long serialVersionUID = 2319684702269414795L;

	private String id;
	private Double epCount;
	private String password;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getEpCount() {
		return epCount;
	}
	public void setEpCount(Double epCount) {
		this.epCount = epCount;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	
}
