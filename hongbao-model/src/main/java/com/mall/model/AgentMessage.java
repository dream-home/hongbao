package com.mall.model;

import com.yanbao.core.model.SimpleModel;

/**
 * @author Pay - 1091945691@qq.com
 */
public class AgentMessage extends SimpleModel implements java.io.Serializable {
	
	/**类的版本号*/
	private static final long serialVersionUID = 2225802986113024L;

	
	/** 代理ID */
	private java.lang.String agentId;
	/** 地区ID */
	private java.lang.String areaId; 
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
	public String toString() {
}