package com.yanbao.util;

import org.springframework.transaction.jta.WebSphereUowTransactionManager;

import java.util.UUID;

/**
 * UUID生成器
 * 
 * @author zhuzh
 * @date 2016年12月7日
 */
public class UUIDUtil {

	/**
	 * 生成一个UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	/**
	 * 生成多个UUID
	 * 
	 * @param number
	 * @return
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}

}
