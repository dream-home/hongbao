package com.yanbao.vo;

import java.io.Serializable;
import java.util.List;

public class AgentStaffVo implements Serializable {

	private static final long serialVersionUID = 8315942462674830954L;
	
	private String agentId;
	private String staffId;
	private String staffAccount;
	private String staffName;
	private String staffPassword;
	private Integer status;
	private List<MenuPermissionVo> permission;
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getStaffAccount() {
		return staffAccount;
	}
	public void setStaffAccount(String staffAccount) {
		this.staffAccount = staffAccount;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getStaffPassword() {
		return staffPassword;
	}
	public void setStaffPassword(String staffPassword) {
		this.staffPassword = staffPassword;
	}
	public List<MenuPermissionVo> getPermission() {
		return permission;
	}
	public void setPermission(List<MenuPermissionVo> permission) {
		this.permission = permission;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
