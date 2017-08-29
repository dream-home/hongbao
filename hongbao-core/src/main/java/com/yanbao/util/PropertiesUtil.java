package com.yanbao.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class PropertiesUtil {
    static Locale locale_cn = new Locale("zh", "CN");
    static ResourceBundle bundle = ResourceBundle.getBundle("config", locale_cn);

    public static String AK = bundle.getString("AK");
    public static String SK = bundle.getString("SK");

    public static String getDomain(String bucket) {
        return bundle.getString(bucket);
    }

    //配置商户号
    public static String getAppId() {
        return bundle.getString("APP_ID");
    }

    //配置App私钥
    public static String getAppPrivateKey() {
        return bundle.getString("APP_PRIVATE_KEY");
    }

    //配置支付宝公钥
    public static String getAliPayPublicKey() {
        return bundle.getString("ALIPAY_PUBLIC_KEY");
    }

    //配置支付宝地址
    public static String getAppUrl() {
        return bundle.getString("appUrl");
    }

    //配置收款账户
    public static String getSellerId() {
        return bundle.getString("seller_id");
    }
}
