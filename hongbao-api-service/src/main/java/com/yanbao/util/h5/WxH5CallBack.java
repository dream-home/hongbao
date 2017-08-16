package com.yanbao.util.h5;

import com.alibaba.fastjson.JSON;
import com.yanbao.constant.RedisKey;
import com.yanbao.redis.Strings;
import com.yanbao.service.PayCallbackService;
import com.yanbao.util.HttpsUtil;
import com.yanbao.util.XMLUtil;
import com.yanbao.util.wechatpay.GenerateOrder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.util.*;

/**
 * @author jay.zheng
 * @date 2017/7/26
 */
public class WxH5CallBack {

    private static final Logger logger = LoggerFactory.getLogger(WxH5CallBack.class);

    public static void wxCallback(HttpServletRequest request, HttpServletResponse response, PayCallbackService payCallbackService, IWxAction action) throws Exception {
        String orderNo = "";
        String msg = "";
        String params = "";
        String resXml = HttpsUtil.getXmlFromRequest(request);
        //开始前记录回调
        if (payCallbackService != null) {
            payCallbackService.doCallBackAction(null, request.getRequestURI(), "微信支付回调,订单号详情见xml: " + resXml, false);
        }
        if (StringUtils.isEmpty(resXml)) {
            logger.info("微信返回XML数据为空");
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";
            msg += "微信返回XML数据为空";
            msg += resXml;
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        }
        Map<String, String> m = new HashMap<String, String>();
        m = XMLUtil.doXMLParse(resXml);
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);
            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        //验签
        GenerateOrder generateOrder = new GenerateOrder();
        logger.error("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        logger.error("H5支付回调验签参数：packageParams");
        logger.error(JSON.toJSONString(packageParams));
        logger.error("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        String signStr = generateOrder.createH5SignWithObjectKV(packageParams);
        String sign = (String) packageParams.get("sign");
        String attach = (String) packageParams.get("attach");
        if (StringUtils.isEmpty(sign) || !signStr.equals(sign)) {
            logger.info("支付失败,错误信息,验签失败");
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";
            msg += "支付失败,错误信息,验签失败";
            msg += resXml;
            logger.info(msg);
        } else {
            String userId = "";
            int payType = 0;
            if (!StringUtils.isEmpty(attach)) {
                if (attach.split("@").length > 1) {
                    payType = Integer.valueOf(attach.split("@")[1]);
                }
                userId = attach.split("@")[0];
            }
            orderNo = (String) packageParams.get("out_trade_no");
            Double fee = Double.valueOf((String) packageParams.get("total_fee"));
            logger.error(JSON.toJSONString(packageParams));
            logger.error("****************************************");
            System.out.println("微信返回参数：" + JSON.toJSONString(packageParams));
            logger.error("****************************************");

            if ("SUCCESS".equals((String) packageParams.get("result_code")) && userId != null) {
                params = action.doAction(orderNo, userId, fee, payType);
            } else {
                logger.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                msg += "微信分享支付回调失败,错误信息：" + packageParams.get("err_code");
                msg += resXml;
                logger.info(msg);
            }
        }
        //成功回调后记录
        if (payCallbackService != null) {
            payCallbackService.doCallBackAction(orderNo, request.getRequestURI(), params, true);
        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
}
