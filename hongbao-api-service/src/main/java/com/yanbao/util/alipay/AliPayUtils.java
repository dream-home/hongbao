package com.yanbao.util.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.yanbao.util.HttpUtil;
import com.yanbao.util.PropertiesUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zzwei on 2017/8/29 0029.
 */
public class AliPayUtils {
    private static Log log = LogFactory.getLog(AliPayUtils.class);

    /**
     * 支付宝网页支付生成预付单
     *
     * @param orderNo
     * @param notifyUrl
     * @param money
     * @param orderTitle
     * @return
     * @throws Exception
     */
    public static String alipayPreOrderForWap(String orderNo, String notifyUrl, Double money, String orderTitle) {
        try {
//            notifyUrl="http://doupaimall.com/api/alipay/notify";
            notifyUrl="https://www.6pyun.com/api/alipay/notify";
            Map<String, String> map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("total_amount", money.toString());
            map.put("subject", orderTitle);
            map.put("seller_id", PropertiesUtil.getSellerId());
            map.put("product_code", "QUICK_WAP_PAY");
            //支付宝SDK调用充值接口
            AlipayClient alipayClient = new DefaultAlipayClient(PropertiesUtil.getAppUrl(), PropertiesUtil.getAppId(), PropertiesUtil.getAppPrivateKey(), "json", "UTF-8", PropertiesUtil.getAliPayPublicKey(), "RSA2"); //获得初始化的AlipayClient
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
            alipayRequest.setNotifyUrl(notifyUrl);//在公共参数中设置回跳和通知地址
            alipayRequest.setBizContent(JSON.toJSONString(map));//填充业务参数
            String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            //返回表单对象
            return form;
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("支付宝网页支付，生成预付单失败");
            return null;
        } finally {
        }
    }


    /**
     * APP支付 支付宝生成预付单
     *
     * @param orderNo
     * @param notifyUrl
     * @param money
     * @param orderTitle
     * @return
     * @throws Exception
     */
    public static String alipayPreOrderForApp(String orderNo, String notifyUrl, Double money, String orderTitle) {
        notifyUrl="http://doupaimall.com/api/alipay/notify";
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(PropertiesUtil.getAppUrl(), PropertiesUtil.getAppId(), PropertiesUtil.getAppPrivateKey(), "json", "UTF-8", PropertiesUtil.getAliPayPublicKey(), "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("斗拍商城支付");
        model.setSubject(orderTitle);
        model.setOutTradeNo(orderNo);
        model.setTimeoutExpress("600m");
        model.setTotalAmount(money.toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setSellerId(PropertiesUtil.getSellerId());
        log.error("88888888888888888888888888888888889888888888888888888888888888888888888888888888888888888888888888888888888");
        log.error(JSON.toJSONString(model));
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if (response != null) {
                log.error("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
                log.error("支付宝原生支付预付单信息："+response.getBody());
                log.error("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
                return response.getBody();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("支付宝app支付，生成预付单失败");
            return null;
        }
        return null;
    }


    /**
     * 支付宝支付异步通知验签
     *
     * @param request
     * @throws IOException
     */
    public static Map nofifyMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap();
        try {
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String paramName = (String) iter.next();
                String paramValue = ((String[]) requestParams.get(paramName))[0];
                //System.out.println("收到参数："+paramName + " = "+ paramValue );
                 /*若你在notify_url后添加了自定义参数如http://www.alipay.com?a=a,请不要加入params*/
                params.put(paramName, paramValue);
            }
            //调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(params, PropertiesUtil.getAliPayPublicKey(), "UTF-8", "RSA2");
            //获取订单号
            String tranNo = params.get("out_trade_no");
            //获取交易状态
            String responseCode = params.get("trade_status");
            map.put("orderNo", tranNo);
            map.put("sign", false);
            if (signVerified) {
                map.put("sign", true);
                // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
                log.error("*****************支付宝回调  params************************");
                log.error(JSON.toJSONString(params));
                log.error("******************************************");
                if ("TRADE_SUCCESS".equals(responseCode)) {
                    map.put("responseCode", "success");
                } else {
                    map.put("responseCode", "failure");
                }
            } else {
                // TODO 验签失败则记录异常日志，并在response中返回failure.
                log.error("验签失败，订单号为:" + params.get("out_trade_no"));
            }
            return map;
        } catch (Exception e) {
            log.error("支付宝通知验签异常" + e);
            e.printStackTrace();
            return null;
        }
    }


    /**深圳支付宝app支付对账**/
    public static Boolean  isSZAliPaySucess(String tradeNo)  {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(PropertiesUtil.getAppUrl(), PropertiesUtil.getAppId(), PropertiesUtil.getAppPrivateKey(), "json", "UTF-8", PropertiesUtil.getAliPayPublicKey(), "RSA2");
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("斗拍商城支付");
            model.setOutTradeNo(tradeNo);
            model.setSellerId(PropertiesUtil.getSellerId());
            request.setBizModel(model);
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                return  true;
            } else {
                return  false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        Double money =0d;
//        alipayPreOrderForApp("201782245552014400","",money,"斗拍商城支付");
        System.out.println(isSZAliPaySucess("20170905111427369006"));
    }
}
