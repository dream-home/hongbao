package com.yanbao.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zzwei
 * @version v1.0
 * @date 2017年2月15日
 */

public class LotteryUtil {
	/**
	 * @param orignalRates
	 *            原始的概率列表
	 */
	public static int lottery(List<Double> orignalRates) {
		if (orignalRates == null || orignalRates.isEmpty()) {
			return -1;
		}
		int size = orignalRates.size();
		double sumRate = 0d;
		for (double rate : orignalRates) {
			sumRate += rate;
		}
		List<Double> sortOrignalRates = new ArrayList<Double>(size);
		Double tempSumRate = 0d;
		for (double rate : orignalRates) {
			tempSumRate += rate;
			sortOrignalRates.add(tempSumRate / sumRate);
		}
		double nextDouble = Math.random();
		sortOrignalRates.add(nextDouble);
		Collections.sort(sortOrignalRates);
		return sortOrignalRates.indexOf(nextDouble);
	}

	public static int getWinIndex(double num, int special, double percent) {
		List<Double> orignalRates = new ArrayList<Double>();
		for (int i = 0; i < num; i++) {
			if (i < special) {
				orignalRates.add((1 / num) * (1 + percent));
			} else {
				orignalRates.add(1 / num);
			}
		}
		int orignalIndex = LotteryUtil.lottery(orignalRates);
		return orignalIndex;
	}
}
