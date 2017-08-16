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
public class WechatCommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(WechatCommonUtil.class);

    /**
     * 静默授权
     */
    public static final String BASE_SCOPE = "snsapi_base";

    /**
     * 用户点击授权
     */
    public static final String USERINFO_SCOPE = "snsapi_userinfo";
    /**  炎宝公众号  */
    /** 微信公众号APPID */
    public static final String YANBAO_PUBLIC_APPID = "wx32266be91b9e0a8f";
    /** 微信公众APP SECRET*/
    public static final String YANBAO_PUBLIC_SECRET = "d9b7e01273e3cf705e4c4bc66bd27514";
    /** 微信公众号key支付秘钥 */
    public static final String YANBAO_PUBLIC_KEY = "4KEFf84f7d7HJ35689a6535d5de3a9FC";
    /** 炎宝微信公众号商户ID */
    public static String YANBAO_PUBLIC_MCH_ID = "1337799001";




    /** 珠海斗拍公众号 */

    /** 微信公众号 APPID */
    public static final String ZHDP_PUBLIC_APPID = "wx30135d0dfc5e7350";
    /** 微信公众号 SECRET*/
    public static String ZHDP_PUBLIC_APPSECRET = "4671c87286c496f2d9709eb74631c46b";


    /** 珠海斗拍APP支付相关 */
    /** 微信应用APP APPID */
    public static String ZHDP_APP_APPID = "wx1581a2802e11162d";
    /** 微信应用商户ID */
    public static String ZHDP_APP_MCHID = "1425023102";
    /** 微信应用 APP key支付秘钥 */
    public static String ZHDP_APP_APPKEY = "ERGHBHF1581a2802e11162dYanbaodou";
    public static String ZHDP_APP_BODY = "斗拍商城支付";


    /** 深圳斗拍公众号 */

    /** 微信公众号 APPID */
    public static final String SZDP_PUBLIC_APPID = "wx3ae10652fe071975";
    /** 微信公众号 SECRET*/
    public static final String SZDP_PUBLIC_SECRET = "038979f404c5f587caadf9d262ade3c0";
    /** 微信公众号 商户ID */
    public static String SZDP_PUBLIC_MCH_ID = "1486167342";
    /** 微信公众号key支付秘钥 */
    public static final String SZDP_PUBLIC_KEY = "zwn91yve0dmgmpx0i36qmlkf4rcu1bfc";


    /** 深圳斗拍APP支付相关 */
    /** 微信应用APPID */
    public static String SZDP_APP_APPID = "wx7b629143e0c84ede";
    /** 微信应用APP SECRET*/
    public static String SZDP_APP_APPSECRET = "b0c2fc4e24a4458d849b404e5fe7d259";
    /** 微信应用APP商户ID */
    public static String SZDP_APP_MCHID = "1485042672";
    /** 微信应用 APP key支付秘钥 */
    public static String SZDP_APP_APPKEY = "2c3slt6c2ugcsc2ojnvmj57cia8exgjm";
    public static String SZDP_APP_BODY = "斗拍商城支付";

}
