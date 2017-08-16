package com.yanbao.core.exception;

/**
 * 所有抛出给调用端的异常请用此异常包装并指定异常编码
 * 
 * @author ZHUZIHUI
 * @date 2016年12月8日
 */
public class CommonException extends CoreException {

	private static final long serialVersionUID = 8239059414649367918L;

	public CommonException(int code) {
		super(code, null, null, null);
	}

	public CommonException(int code, Object[] args) {
		super(code, args, null, null);
	}

	public CommonException(int code, String message) {
		super(code, null, message, null);
	}

	public CommonException(int code, String message, Throwable cause) {
		super(code, null, message, cause);
	}

	public CommonException(int code, Throwable cause) {
		super(code, null, null, cause);
	}
}
