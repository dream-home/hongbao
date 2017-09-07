package com.yanbao.util.refunds;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.yanbao.util.ToolUtil;
import com.yanbao.util.XMLUtil;

@Component
public class RefundsUtils { 
    public static TransferInfo buildCompanyPayMap(String appId, String mchid, String appopenid, String orderNo, Boolean isCheckUserName, String userName, Integer money, String desc, String ip) {
        String noneStr = ToolUtil.getUUID();
        SortedMap map = new TreeMap();
        map.put("mch_appid", appId);
        map.put("mchid", mchid);
        map.put("nonce_str", noneStr);
        map.put("partner_trade_no", orderNo);
        map.put("openid", appopenid);
        if (isCheckUserName) {
            map.put("check_name", "FORCE_CHECK");
            map.put("re_user_name", userName);
        } else {
            map.put("check_name", "NO_CHECK");
        }

        map.put("desc", desc);
        map.put("amount", money);
        map.put("spbill_create_ip", ip);
        map.put("amount", money);
        String sign = RefundsUtils.createSign(map);
        TransferInfo transferInfo = new TransferInfo();
        transferInfo.setMch_appid(appId);
        transferInfo.setMchid(mchid);
        transferInfo.setNonce_str(noneStr);
        transferInfo.setPartner_trade_no(orderNo);
        transferInfo.setOpenid(appopenid);
        transferInfo.setCheck_name((String) map.get("check_name"));
        transferInfo.setRe_user_name(userName);
        transferInfo.setDesc(desc);
        transferInfo.setAmount(money);
        transferInfo.setSpbill_create_ip(ip);
        transferInfo.setSign(sign);
        return transferInfo;
    }

    /**
     * 生成签名
     *
     * @return
     */
    public static String createSign(SortedMap<String, Object> packageParams) {
        // 根据规则创建可排序的map集合
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();// 字典序
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            // 为空不参与签名、参数名区分大小写
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        // 第二步拼接key，key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
//        sb.append("key=" + _AppKey);
//        sb.append("key=" + _AppKey);
        sb.append("key=" + "ERGHBHF1581a2802e11162dYanbaodou");
//        sb.append("key=" + "4KEFf84f7d7HJ35689a6535d5de3a9FC"); 
        System.out.println(sb.toString());
        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();// MD5加密
        System.out.println(sign);
        return sign;
    }



    public static void weixinCompanyPay(String certificatPath,String appId,String muchId,String appOpenId,String orderNo,Integer money,Boolean isCheckRealName,String desc,String ip) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(certificatPath));
        try {
            keyStore.load(instream, muchId.toCharArray());
        } finally {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, muchId.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
            TransferInfo transferInfo = RefundsUtils.buildCompanyPayMap(appId, muchId, appOpenId, orderNo, false, "", money, "test01", "113.74.9.11");
            String data = XMLUtil.objectToXml(transferInfo);
            System.out.println("00000000000000000000000000000000000000");
            System.out.println("data 微信提现请求数据 "+data );
            System.out.println("00000000000000000000000000000000000000");
            try {
                httppost.setEntity(new StringEntity(data, "utf-8"));
                CloseableHttpResponse response = httpclient.execute(httppost);
                try {
                    HttpEntity entity = response.getEntity();
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    if (entity != null) {
                        System.out.println("Response content length: " + entity.getContentLength());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                        String text;
                        while ((text = bufferedReader.readLine()) != null) {
                            System.out.println(text);
                        }
                    }
                    EntityUtils.consume(entity);
                } finally {
                    response.close();
                }
            } finally {
                httpclient.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
