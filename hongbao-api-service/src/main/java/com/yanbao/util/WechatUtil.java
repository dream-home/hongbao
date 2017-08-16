package com.yanbao.util;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.yanbao.util.h5.GenerateH5Order;
import com.yanbao.util.h5.ResponseInfo;
import com.yanbao.util.wechatpay.GenerateOrder;
import com.yanbao.util.wechatpay.WeChatPay;
import com.yanbao.vo.QueryResponseInfoVo;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zzwei on 2017/4/21 0021.
 */
public class WechatUtil {
 /*   public static final String YANBAO_APPID = "wx32266be91b9e0a8f";
    public static final String YANBAO_KEY = "4KEFf84f7d7HJ35689a6535d5de3a9FC";
    public static final String YANBAO_SECRET = "d9b7e01273e3cf705e4c4bc66bd27514";

    public static String _appid = WeChatPay.APP_ID;// 微信APPID
    public static String _MchId = WeChatPay.MCH_ID;// 微信商户ID(斗拍)
    public static String _YANBAO_MCH_ID = WeChatPay.YANBAO_MCH_ID;// 微信商户ID（炎宝）
    public static String _AppKey = WeChatPay.APP_KEY; // 微信APPkey
    public static String _NOTIFY_URL_H5 = WeChatPay.NOTIFY_URL_H5;//H5微信支付回调地址
    public static String _body = "斗拍商城支付";*/

    public static String _NOTIFY_URL_H5 = "http://m.yanbaocoin.cn/m/wallet/share/wxCallback";//H5微信支付回调地址

    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);




    /**微信app支付对账**/
    public static Boolean  isAppPaySucess(String orderNo) throws Exception {
        GenerateOrder generateOrder = new GenerateOrder();
        try {
            StringBuffer sb= generateOrder.getRequestXml("https://api.mch.weixin.qq.com/pay/orderquery",generateOrder.buildQueryOrderInfo(orderNo));
            QueryResponseInfoVo responseInfo= generateOrder.getQueryOrderResponseInfo(sb);
            if (responseInfo==null){
                return false;
            }
            if (responseInfo.getTrade_state().equals("SUCCESS")  && "SUCCESS".equals(responseInfo.getResult_code())){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**微信公众号支付对账H5**/
    public static Boolean  isH5PaySucess(String orderNo) throws Exception {
        GenerateH5Order generateH5Order = new GenerateH5Order();
        try {
            StringBuffer sb= generateH5Order.getRequestXml("https://api.mch.weixin.qq.com/pay/orderquery",generateH5Order.buildQueryOrderInfo(orderNo));
            QueryResponseInfoVo responseInfo= generateH5Order.getQueryOrderResponseInfo(sb);
            if (responseInfo==null){
                return false;
            }
            if (responseInfo.getTrade_state().equals("SUCCESS")  && "SUCCESS".equals(responseInfo.getResult_code())){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
        return false;
    }


    /**支付宝充值支付对账**/
    /**支付宝app支付对账**/
    public static Boolean  isAppScanAliPaySucess(String tradeNo)  {
        try {
            String url ="http://120.24.234.115:8090/pay/payAlipaySDK/queryAliPayState?tradeNo="+tradeNo;
            JSONObject jsonObject = HttpUtil.doGetRequest(url);
            System.out.println(jsonObject);
            if (jsonObject==null){
                return false;
            }
            if (jsonObject.get("code").equals("0000")){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void main(String[] args) {

        try {
//            System.out.println(isAppScanAliPaySucess("20170527214534521045"));
//            System.out.println(isAppScanAliPaySucess("20170620205729487153"));
//            System.out.println(isAppPaySucess("20170620222010725155"));
            System.out.println(isH5PaySucess("20170810102249343001"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
