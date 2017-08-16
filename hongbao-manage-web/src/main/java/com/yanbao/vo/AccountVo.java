package com.yanbao.vo;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2017年1月12日
 */
public class AccountVo implements Serializable {

	private static final long serialVersionUID = 2319684702269414795L;

	private String id;
	private String uid;
	private String Account;
	private String username;
	private String phone;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public static AccountVo create(String id,String uid,String account,String username,String phone){
		AccountVo result=new AccountVo();
		result.setId(id);
		result.setUid(uid);
		result.setAccount(account);
		result.setUsername(username);
		result.setPhone(phone);
		return result;
	}
	
	
}
