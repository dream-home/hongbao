package com.mall.model;

import com.yanbao.core.model.BaseModel;

public class SecondCallBack extends BaseModel{

	private String id;
	   
	private String returnUrl;
	
	private String remark;
	
	private Integer source;

	private String testReturnUrl;

	public String getTestReturnUrl() {
		return testReturnUrl;
	}

	public void setTestReturnUrl(String testReturnUrl) {
		this.testReturnUrl = testReturnUrl;
	}

	/**
	 * @return the source
	 */
	public Integer getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Integer source) {
		this.source = source;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the returnUrl
	 */
	public String getReturnUrl() {
		return returnUrl;
	}
	
	/**
	 * @param returnUrl the returnUrl to set
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

