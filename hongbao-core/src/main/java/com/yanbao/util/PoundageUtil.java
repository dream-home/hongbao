package com.yanbao.util;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * 四舍五入计算手续费
 *
 * @author zhuzh
 * @date 2016年12月14日
 */
public class PoundageUtil {

    /**
     * 手续费至少收一分钱
     */
    private static final Double MIN_POUNDAGE = 0d;

    /**
     * 默认保留两位小数位数，精确到分
     */
    private static final Integer DEFAULT_DECIMAL = 2;

    /**
     * @param score         积分
     * @param poundageScale 收费比例
     * @return
     */
    public static Double getPoundage(Double score, Double poundageScale) {
        return getPoundage(score, poundageScale, DEFAULT_DECIMAL);
    }

    /**
     * @param score         积分
     * @param poundageScale 收费比例
     * @param decimal       保留小数位数
     * @return
     */
    public static Double getPoundage(Double score, Double poundageScale, Integer decimal) {
        Double poundage = score * poundageScale;
        if (decimal == null) {
            decimal = DEFAULT_DECIMAL;
        }
        if (poundage < MIN_POUNDAGE) {
            poundage = MIN_POUNDAGE;
        } else { // 四舍五入计算手续费
            BigDecimal bd = new BigDecimal(poundage);
            poundage = bd.setScale(decimal, RoundingMode.HALF_EVEN).doubleValue();
        }
        return poundage;
    }



    /**
     * 加法
     * @param v1    加数
     * @param v2    加数
     * @param scale 表示表示需要精确到小数点以后几位。
     */

    public static double add(double v1, double v2, int scale)
    {
        if (scale < 0) {
            scale=1;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).setScale(scale,RoundingMode.HALF_EVEN).doubleValue();
    }
    /**
     * 减法
     * @param v1    被减数
     * @param v2    减数
     * @param scale 表示表示需要精确到小数点以后几位。
     */

    public static double subtract(double v1, double v2, int scale)
    {
        if (scale < 0) {
            scale=1;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).setScale(scale,RoundingMode.HALF_EVEN).doubleValue();
    }



    /**
     * 乘法
     * @param v1    被减数
     * @param v2    减数
     * @param scale 表示表示需要精确到小数点以后几位。
     */

    public static double multiply(double v1, double v2, int scale)
    {
        if (scale < 0) {
            scale=1;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).setScale(scale,RoundingMode.HALF_EVEN).doubleValue();
    }


    /**
     * 除法
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */

    public static double divide(double v1, double v2, int scale)
    {
        if (scale < 0) {
            scale=1;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, RoundingMode.HALF_EVEN).doubleValue();
    }

}
