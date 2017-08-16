package com.yanbao.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yanbao.constant.BankCardType;
import com.yanbao.constant.GoodsWinType;
import com.yanbao.constant.RedisKey;
import com.yanbao.constant.ResultCode;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.mall.model.GoodsWin;
import com.mall.model.User;
import com.yanbao.redis.Strings;
import com.yanbao.service.GoodsWinService;
import com.yanbao.service.MallStoreService;
import com.yanbao.service.PayCallbackService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletRechargeService;
import com.yanbao.util.HttpsUtil;
import com.yanbao.util.RedisLock;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.WechatUtil;
import com.yanbao.util.XMLUtil;
import com.yanbao.util.wechatpay.GenerateOrder;
import com.yanbao.vo.QueryResponseInfoVo;
import com.yanbao.vo.WxH5OrderVo;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private WalletRechargeService walletRechargeService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private MallStoreService mallStoreService;
    @Autowired
    private PayCallbackService payCallbackService;



/*	*//**
     * 支付宝分享回调
     *//*
    @ResponseBody
	@RequestMapping(value = "/share/alipayCallback", method = RequestMethod.POST)
	public JsonResult rechargeCallback(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
		Token token = TokenUtil.getSessionUser(request);
		if (StringUtils.isEmpty(vo.getOrderNo())) {
			return new JsonResult(2, "订单号参数错误");
		}
		//开始前记录回调
		payCallbackService.doCallBackAction(vo.getOrderNo(),request.getRequestURI(), JSON.toJSONString(vo),false);
		if (vo.getSource() != BankCardType.ALIPAY.getCode() && vo.getSource() != BankCardType.WECHATPAY.getCode()) {
			return new JsonResult(1, "目前仅支持支付宝、微信支付付款");
		}
		String key = RedisKey.WEIXIN_CALLBACK.getKey() + vo.getOrderNo();
		String value = Strings.get(key);
		User user = userService.getById(token.getId());
		GoodsWin goodsWin = goodsWinService.getByOrderNo(vo.getOrderNo(),user.getId());
		if (goodsWin==null){
			return new JsonResult(2, "订单不存在");
		}
		if (goodsWin==null || goodsWin.getStatus()!= GoodsWinType.PENDING.getCode().intValue() || !StringUtils.isEmpty(value) || vo.getSource()!=BankCardType.SHARE_ALIPAY.getCode().intValue()) {
			return new JsonResult(3, "回调失败");
		}else{
			Strings.setEx(RedisKey.WEIXIN_CALLBACK.getKey()+vo.getOrderNo(), RedisKey.WEIXIN_CALLBACK.getSeconds(), vo.getOrderNo());
			mallStoreService.purchaseGoodsWinbyH5(vo.getOrderNo(),user.getId());
		}
		// 操作成功返回用户当前积分
		Map<String, Object> result = new HashMap<String, Object>();
		user = userService.getById(token.getId());
		result.put("score", FormatUtils.formatDouble(user.getScore()));
		//成功回调后记录
		payCallbackService.doCallBackAction(vo.getOrderNo(),request.getRequestURI(), JSON.toJSONString(vo),true);
		return new JsonResult(result);
	}*/


    /**
     * 微信分享支付回调
     */
    @ResponseBody
    @RequestMapping(value = "/share/wxCallback", method = RequestMethod.POST)
    public void wxCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderNo="";
        String msg = "";
        GoodsWin goodsWin = null;
        String resXml = HttpsUtil.getXmlFromRequest(request);
        //开始前记录回调
        payCallbackService.doCallBackAction(null, request.getRequestURI(),"微信分享支付回调,订单号详情见xml: "+ resXml, false);
        logger.error("***********call***********");
        logger.error(resXml);
        logger.error("***********wxCallback***********");
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
        String signStr = generateOrder.createH5SignWithObjectKV(packageParams);
        String sign = (String) packageParams.get("sign");
        String attach = (String) packageParams.get("attach");
        if (StringUtils.isEmpty(sign) || !signStr.equals(sign)) {
            logger.info("支付失败,错误信息,验签失败");
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";
            msg += "支付失败,错误信息,验签失败";
            msg += resXml;
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
            String key = RedisKey.WEIXIN_CALLBACK.getKey() + orderNo;
            String value = Strings.get(key);
            logger.error(JSON.toJSONString(packageParams));
            logger.error("****************************************");
            System.out.println("微信返回参数：" + JSON.toJSONString(packageParams));
            logger.error("****************************************");

            if ("SUCCESS".equals((String) packageParams.get("result_code")) && userId != null) {
                goodsWin = goodsWinService.getByOrderNo(orderNo, userId);
                if (fee == null || goodsWin == null || goodsWin.getStatus() != GoodsWinType.PENDING.getCode().intValue() || !StringUtils.isEmpty(value) || payType != BankCardType.SHARE_WEIXIN.getCode().intValue()) {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    msg += "微信分享支付回调失败，不符合业务回调规则";
                    msg += resXml;
                } else {
                    Strings.setEx(RedisKey.WEIXIN_CALLBACK.getKey() + orderNo, RedisKey.WEIXIN_CALLBACK.getSeconds(), orderNo);
                    User user = userService.getById(userId);
                    mallStoreService.purchaseGoodsWinbyH5(orderNo, userId);
                    logger.error("支付成功");
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    msg += "微信分享支付回调支付成功";
                    msg += resXml;
                }
            } else {
                logger.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                msg += "微信分享支付回调失败,错误信息：" + packageParams.get("err_code");
                msg += resXml;
            }
        }
        //成功回调后记录
        payCallbackService.doCallBackAction(orderNo, request.getRequestURI(), JSON.toJSONString(goodsWin), true);
        logger.error("*******************100000*********************");
        logger.error("*******************100000********************");
        logger.error(resXml);
        logger.error("****************************************");
        logger.error("****************************************");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 微信分享购买对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/query/wxh5order", method = RequestMethod.POST)
    public JsonResult queryH5WxOrder(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户不存在");
        }
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "orderNo不能为空");
        }
        GoodsWin goodsWin = goodsWinService.getByOrderNo(vo.getOrderNo(), user.getId());
        if (goodsWin == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "订单不存在");
        }

        if (goodsWin.getStatus().intValue() != GoodsWinType.PENDING.getCode()) {
            logger.error(goodsWin.getOrderNo() + "：订单状态不是待付款，不需要处理");
            return new JsonResult(ResultCode.SUCCESS.getCode(), "微信分享订单已支付成功");
        }
        Boolean isSucess = WechatUtil.isH5PaySucess(vo.getOrderNo());
        String key = RedisKey.HANDLE_CALLBACK.getKey() + vo.getOrderNo();
        Boolean flag = RedisLock.redisLock(key, vo.getOrderNo(), 2);
        if (flag && isSucess) {
            mallStoreService.purchaseGoodsWinbyH5(vo.getOrderNo(), user.getId());
            return new JsonResult(ResultCode.SUCCESS.getCode(), "微信分享订单支付确认回调成功");
        }
        if (goodsWin.getStatus().intValue() == GoodsWinType.BUYED.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "微信分享订单微信已经回调成功");
        }
        //成功回调后记录
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo) + " 回调失败", true);
        logger.error("0000000000---> 微信分享购买支付失败");
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }


    public static void main(String[] args) throws IOException, JDOMException {
        GenerateOrder generateOrder = new GenerateOrder();
        StringBuffer sb = generateOrder.getRequestXml("https://api.mch.weixin.qq.com/pay/orderquery", generateOrder.buildQueryOrderInfo("20170401193127001037"));
        QueryResponseInfoVo responseInfoVo = generateOrder.getQueryOrderResponseInfo(sb);
        Map map = XMLUtil.doXMLParse(sb.toString());
        //System.out.println(generateOrder.createSign(new TreeMap<>(map)));
        System.out.println("***********");
        System.out.println(generateOrder.createSign(new TreeMap<String, String>(map)));

    }


}
