package com.yanbao.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mall.model.WalletBill;


/**
 * @author YIYIRUI
 * @date 2017年06月05日
 */
public interface IncomeStatisticsService {
    public static String AllStoreids = "AllStoreids";//当前统计MAP中所有商铺ID的KEY
    public static String WalletBillDetail = "WalletBillDetail";//当前统计MAP中统计完WalletBillDetail对象的KEY
    public static String AllGoodsWin = "AllGoodsWin";//当前统计MAP中统计完GoodsWins集合的KEY
    public static String AllWalletRecharge = "AllWalletRecharge";//当前统计MAP中统计完WalletRecharge集合的KEY
    
    //查询所有的统计数据
    List<WalletBill> getAllinfo() throws Exception;
    
    //开始所有统计
    List<WalletBill> IncomeStatistics(Date starttime, Date endtime) throws Exception;

    //直接购买:H5微信分享购买
    public Map<String, Object> h5Wechat(Date starttime, Date endtime);

    //直接购买：APP余额支付
    public Map<String, Object> appBalance(Date starttime, Date endtime);

    //直接购买:APP微信支付
    public Map<String, Object> appWechat(Date starttime, Date endtime);

    //直接购买:APP支付宝支付
    public Map<String, Object> appAliPay(Date starttime, Date endtime);

    //面对面扫码支付：APP余额支付
    public Map<String, Object> faceAppPay(Date starttime, Date endtime);

    //面对面扫码支付:APP微信支付
    public Map<String, Object> faceAppWechat(Date starttime, Date endtime);

    //面对面扫码支付:APP支付宝支付
    public Map<String, Object> faceAppAliPay(Date starttime, Date endtime);

    //商家二维码扫码支付：APP余额支付
    public Map<String, Object> storeAppPay(Date starttime, Date endtime);

    //商家二维码扫码支付：APP微信支付
    public Map<String, Object> storeAppWechat(Date starttime, Date endtime);

    //商家二维码扫码支付：APP支付宝支付
    public Map<String, Object> storeAppAliPay(Date starttime, Date endtime);

    //商家二维码扫码支付：H5微信直接扫码支付
    public Map<String, Object> storeH5Wechat(Date starttime, Date endtime);

    //商家二维码扫码支付：H5微信直接扫码支付
    public Map<String, Object> storeH5AliPay(Date starttime, Date endtime);


}
