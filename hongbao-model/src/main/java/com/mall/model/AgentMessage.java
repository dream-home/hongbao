package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * @author Pay - 1091945691@qq.com
 */
public class AgentMessage extends SimpleModel implements java.io.Serializable {
	
	/**类的版本号*/
	private static final long serialVersionUID = 2225802986113024L;

		/** 用户ID即商家用户id */	private java.lang.String userId;	/** 消息标题 */	private java.lang.String title;	/** 消息类型：1：店铺申请（包含了资质认证） */	private java.lang.Integer type;	/** 消息详情 */	private java.lang.String detail;	/** 创建时间 */	private java.util.Date createTime;	/** 修改时间 */	private java.util.Date updateTime;	/** 状态：0：未读，1：已读 */	private java.lang.Integer status;	/** 备注 */	private java.lang.String remark;
	/** 代理ID */
	private java.lang.String agentId;
	/** 地区ID */
	private java.lang.String areaId; 	public java.lang.String getId() {	    return this.id;	}	public void setId(java.lang.String id) {	    this.id=id;	}	public java.lang.String getUserId() {	    return this.userId;	}	public void setUserId(java.lang.String userId) {	    this.userId=userId;	}	public java.lang.String getTitle() {	    return this.title;	}	public void setTitle(java.lang.String title) {	    this.title=title;	}	public java.lang.Integer getType() {	    return this.type;	}	public void setType(java.lang.Integer type) {	    this.type=type;	}	public java.lang.String getDetail() {	    return this.detail;	}	public void setDetail(java.lang.String detail) {	    this.detail=detail;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}	public java.lang.Integer getStatus() {	    return this.status;	}	public void setStatus(java.lang.Integer status) {	    this.status=status;	}	public java.lang.String getRemark() {	    return this.remark;	}	public void setRemark(java.lang.String remark) {	    this.remark=remark;	}
	public java.lang.String getAgentId() {
		return agentId;
	}
	public void setAgentId(java.lang.String agentId) {
		this.agentId = agentId;
	}
	public java.lang.String getAreaId() {
		return areaId;
	}
	public void setAreaId(java.lang.String areaId) {
		this.areaId = areaId;
	}
	public String toString() {	    return "[" + "id:" + getId() +"," + "userId:" + getUserId() +"," + "title:" + getTitle() +"," + "type:" + getType() +"," + "detail:" + getDetail() +"," + "createTime:" + getCreateTime() +"," + "updateTime:" + getUpdateTime() +"," + "status:" + getStatus() +"," + "remark:" + getRemark() +"]";	}
}