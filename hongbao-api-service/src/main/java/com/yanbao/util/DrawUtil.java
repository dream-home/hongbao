package com.yanbao.util;

/**
 * 开奖计算逻辑
 * 
 * @author zhuzh
 * @date 2016年12月28日
 */
public class DrawUtil {

	/**
	 * 计算开奖积分，第一个为运气王
	 * 
	 * @param score
	 * @param scaleMin
	 * @param scaleMax
	 * @param size
	 * @param minScore
	 * @return
	 */
	public static double[] randomScoreArr(double score, double scaleMin, double scaleMax, int size, double minScore) {
		double[] arr = new double[size];
		Double arr1 = randomWinScore(score, scaleMin, scaleMax);
		arr[0] = arr1;
		size--;
		score = score - arr1;
		for (int i = size; i >= 1; i--) {
			Double randomScore = randomScore(score, i, 0.01);
			randomScore = PoundageUtil.getPoundage(randomScore, 1.0);
			arr[size - i + 1] = randomScore;
			score = PoundageUtil.getPoundage(score - randomScore, 1.0);
		}
		return arr;

	}

	/**
	 * 计算运气王积分
	 * 
	 * @param score
	 * @param WinnerScaleMin
	 * @param WinnerScaleMax
	 * @return
	 */
	public static Double randomWinScore(double score, double scaleMin, double scaleMax) {
		int min = (int) (scaleMin * 100);
		int max = (int) (scaleMax * 100);
		double scale = RandomUtil.randomInt(min, max) / 100.0;
		return PoundageUtil.getPoundage(score, scale);
	}

	/**
	 * 计算普通用户积分
	 * 
	 * @param score
	 * @param remainSize
	 * @param min
	 * @return
	 */
	private static Double randomScore(double score, int remainSize, double minScore) {
		if (remainSize == 1) {
			return score;
		}
		if (score / remainSize <= minScore) {
			return minScore;
		}
		int min = (int) (minScore * 100000);
		int max = (int) ((score / remainSize) * 100000 * 2);
		double randomScore = RandomUtil.randomInt(min, max) / 100000.0;
		return PoundageUtil.getPoundage(randomScore, 1.0);
	}

	public static void main(String[] args) {
		double[] arr = randomScoreArr(100, 0.6, 0.8, 5, 0.01);
		double count = 0d;
		for (int i = 0; i < arr.length; i++) {
			count += arr[i];
			if (i == 0) {
				System.out.print(arr[i]);
			} else {
				System.out.print(" + " + arr[i]);
			}
		}
		System.out.println(" = " + count);
	}
}
