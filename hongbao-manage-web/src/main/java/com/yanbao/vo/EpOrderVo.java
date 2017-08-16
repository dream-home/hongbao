package com.yanbao.vo;

import java.io.Serializable;

import com.mall.model.EpRecord;

/**
 * 
 */
public class EpOrderVo extends EpRecord implements Serializable {

	private AccountVo user;

	public AccountVo getUser() {
		return user;
	}

	public void setUser(AccountVo user) {
		this.user = user;
	}
	
	
}
