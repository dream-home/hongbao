package com.yanbao.core.exception;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * 异常基类
 * 
 * @author ZHUZIHUI
 * @date 2016年12月8日
 */
public abstract class CoreException extends RuntimeException {

	private static final long serialVersionUID = -1873407323834369421L;

	private static MessageSourceAccessor accessor;

	private final int code;

	public CoreException(int code, Object[] args, String message, Throwable cause) {
		super(resolveMessage(message, code, args), cause);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public final static void setAccessor(MessageSourceAccessor accessor) {
		CoreException.accessor = accessor;
	}

	private static String resolveMessage(String message, int code, Object[] args) {
		if (null != message) {
			return message;
		}
		String resolvedMessage = null;
		try {
			if (null == args) {
				resolvedMessage = accessor.getMessage(String.valueOf(code));
			} else {
				resolvedMessage = accessor.getMessage(String.valueOf(code), args);
			}
		} catch (NoSuchMessageException ex) {
		}
		return resolvedMessage;
	}
}
