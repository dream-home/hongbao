package com.yanbao.core.page;

import java.io.Serializable;

/**
 * 
 * @author zhuzh
 * @date 2016年11月30日
 */
public class JsonResult implements Serializable {

	private static final long serialVersionUID = -6154898354967117911L;

	private Integer code = 200;

	private String msg = "操作成功";

	private Object result;

	public JsonResult() {
		super();
	}

	public JsonResult(Object result) {
		super();
		this.result = result;
	}

	public JsonResult(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public JsonResult(Integer code, String msg, Object result) {
		this.code = code;
		this.msg = msg;
		this.result = result;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
