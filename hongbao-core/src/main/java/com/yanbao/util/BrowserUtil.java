package com.yanbao.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author zhuzh
 * @date 2016年12月23日
 */
public class BrowserUtil {

	public static final Boolean isWechat(HttpServletRequest request) {
		Boolean flag = false;
		String ua = request.getHeader("user-agent").toLowerCase();
		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
			flag = true;
		}
		return flag;
	}
}
