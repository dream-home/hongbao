package com.yanbao.vo;

import java.io.Serializable;

public class MenuPermissionVo implements Serializable {

	private static final long serialVersionUID = 8315942462674830954L;

	private String permissionId;
	private String description;
	public String getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
