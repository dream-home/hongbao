package com.yanbao.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public final class FormatUtils {

	private static int DEFAULT_SCALE = 2;

	/**
	 * 金额四舍五入后再格式化为字符串（千位不显示）<br>
	 */
	public static String roundFormatDecimal(BigDecimal amount) {
		return roundFormatDecimal(amount, DEFAULT_SCALE);
	}

	/**
	 * 金额四舍五入后再格式化为字符串（千位不显示）<br>
	 * 显示格式: ####0.00 , 小数位数根据scale来决定
	 */
	public static String roundFormatDecimal(BigDecimal amount, int scale) {
		if (0 > scale) {
			throw new RuntimeException("invalid scale:" + scale);
		}
		if (amount == null) {
			return "0";
		}
		String pattern = "###0";
		for (int i = 0; i < scale; i++) {
			if (0 == i) {
				pattern += ".";
			}
			pattern += "0";
		}
		amount = amount.setScale(scale, RoundingMode.HALF_UP);
		DecimalFormat inst = new DecimalFormat(pattern);
		return inst.format(amount);
	}

	/**
	 * 金额格式化为字符串（千位不显示）<br>
	 */
	public static String formatDouble(Double amount) {
		return formatDouble(amount, DEFAULT_SCALE);
	}

	/**
	 * 金额格式化为字符串（千位不显示）<br>
	 * 显示格式: ####0.00 , 小数位数根据scale来决定
	 */
	public static String formatDouble(Double amount, int scale) {
		if (0 > scale) {
			throw new RuntimeException("invalid scale:" + scale);
		}
		if (amount == null) {
			return "0";
		}
		String pattern = "###0";
		for (int i = 0; i < scale; i++) {
			if (0 == i) {
				pattern += ".";
			}
			pattern += "0";
		}
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(amount);
	}

}
