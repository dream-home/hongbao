package com.yanbao.core.model;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public class SimpleModel extends BaseModel {

	private static final long serialVersionUID = 1L;

	/** 创建时间 */
	@JsonSerialize(using = CustomDateSerializer.class) 
	protected Date createTime;

	/** 更新时间 */
	@JsonSerialize(using = CustomDateSerializer.class) 
	protected Date updateTime;

	/** 状态 */
	protected Integer status;

	/** 备注 */
	protected String remark;

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

}
