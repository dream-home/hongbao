package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * @author Pay - 1091945691@qq.com
 */
public class MenuPermission extends SimpleModel implements java.io.Serializable {
	
	/**类的版本号*/
	private static final long serialVersionUID = 2224420635887616L;

	private java.lang.String id;	/** 功能描述 */	private java.lang.String description;	/** 创建时间 */	private java.util.Date createTime;	/** 修改时间 */	private java.util.Date updateTime;	/** 状态：0：审核中，1：审核通过，2：审核不通过，3：关闭 */	private java.lang.Integer status;	/** 备注 */	private java.lang.String remark;	public java.lang.String getId() {	    return this.id;	}	public void setId(java.lang.String id) {	    this.id=id;	}	public java.lang.String getDescription() {	    return this.description;	}	public void setDescription(java.lang.String description) {	    this.description=description;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}	public java.lang.Integer getStatus() {	    return this.status;	}	public void setStatus(java.lang.Integer status) {	    this.status=status;	}	public java.lang.String getRemark() {	    return this.remark;	}	public void setRemark(java.lang.String remark) {	    this.remark=remark;	}	public String toString() {	    return "[" + "id:" + getId() +"," + "description:" + getDescription() +"," + "createTime:" + getCreateTime() +"," + "updateTime:" + getUpdateTime() +"," + "status:" + getStatus() +"," + "remark:" + getRemark() +"]";	}
}