package com.yanbao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class PasswordUtil {

	/**
	 * 判断是否相同的数字或者字母，如111111，aaaaaa
	 */
	public static boolean isAllEqualStr(String pwd) {
		boolean flag = true;
		char str = pwd.charAt(0);
		for (int i = 0, len = pwd.length(); i < len; i++) {
			if (str != pwd.charAt(i)) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 * 判断是否纯数字
	 */
	public static boolean isAllNumeric(String pwd) {
		boolean flag = true;
		for (int i = 0, len = pwd.length(); i < len; i++) {
			if (!Character.isDigit(pwd.charAt(i))) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 * 判断是否连续的数字，如123456
	 */
	public static boolean isOrderNumeric(String pwd) {
		boolean flag = true;
		if (isAllNumeric(pwd)) {
			for (int i = 0, len = pwd.length(); i < len; i++) {
				if (i > 0) {
					int num = Integer.parseInt(pwd.charAt(i) + "");
					int num_prev = Integer.parseInt(pwd.charAt(i - 1) + "");
					if (num != num_prev + 1) {
						flag = false;
						break;
					}
				}
			}
		} else {
			flag = false;
		}
		return flag;
	}
	
	public static boolean checkPass(String pass){
		String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(pass);
		return matcher.matches();
	}
}
