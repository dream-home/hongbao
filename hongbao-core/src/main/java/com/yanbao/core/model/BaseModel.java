package com.yanbao.core.model;

import java.io.Serializable;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
