package com.yanbao.core.model;

import java.io.Serializable;

public class Token implements Serializable {

	private static final long serialVersionUID = -8000170763642555845L;

	/** 用户ID */
	private String id;
	/** 用户名 */
	private String userName;
	/** 用户昵称 */
	private String nickName;
	/** 登录时间 */
	private Long time;
	/** 登录类型 */
	private Integer loginType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
}
