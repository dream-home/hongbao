package com.yanbao.controller;

import com.alibaba.fastjson.JSON;
import com.mall.model.Parameter;
import com.mall.model.SecondCallBack;
import com.mall.model.User;
import com.mall.model.WalletRecharge;
import com.yanbao.constant.*;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.util.alipay.AliPayUtils;
import com.yanbao.util.h5.GenerateH5Order;
import com.yanbao.util.h5.IWxAction;
import com.yanbao.util.h5.WxH5CallBack;
import com.yanbao.util.wechatpay.GenerateOrder;
import com.yanbao.vo.WalletVo;
import com.yanbao.vo.WxH5OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/join")
public class JoinController {
    private static final Logger logger = LoggerFactory.getLogger(JoinController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private WalletRechargeService walletRechargeService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private PayCallbackService payCallbackService;
    @Autowired
    private PayDistributionService payDistributionService;
    @Value("${join_weixin_notifyUrl}")
    private String joinWeixinNotifyUrl;
    @Value("${join_alipay_notifyUrl}")
    private String joinAlipayNotifyUrl;
    @Value("${environment}")
    private String environment;
    @Value("${wxStoreH5_join_weixin_notifyUrl}")
    private String wxStoreH5joinWeixinNotifyUrl;
    @Autowired
    private SecondCallBackService secondCallBackService;
    @Autowired
    private OrderTypeService orderTypeService;


    /**
     * 普通会员加入合伙人---生成支付单
     */
    @ResponseBody
    @RequestMapping(value = "/partnerorder", method = RequestMethod.POST)
    public JsonResult buildPayOrder(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        String tokens = TokenUtil.getToken(request);
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        Boolean isCompeteInfo = userService.isCompleteInfo(user);
        if (!isCompeteInfo) {
            return new JsonResult(0, "加入合伙人前,请先去账户中心完善资料");
        }
        //支付来源
        int source = vo.getSource().intValue();
        //如果discountEP>0 就是用兑换EP
        Double discountEP = vo.getDiscountEP();
        //支付来源是否合法有效
        boolean isVaildPay = source == BankCardType.ALIPAY.getCode().intValue() || source == BankCardType.WECHATPAY.getCode().intValue() || source == BankCardType.BALANCE.getCode().intValue();
        if (!isVaildPay) {
            return new JsonResult(1, "目前仅支持支付宝、微信支付、余额付款");
        }
        //获取支付的三级分销比例和ep比例
        ParamUtil util = ParamUtil.getIstance();
        if (util == null) {
            return new JsonResult(6, "获取系统设置参数失败");
        }
        Double joinEp = ToolUtil.parseDouble(util.get(Parameter.JOINEP), 0d);
        Double joinRmbScale = ToolUtil.parseDouble(util.get(Parameter.JOINRMBSCALE), 0d);
        Double exchangeEp = user.getExchangeEP();
        Double needEP = joinEp - joinEp * joinRmbScale / 100;
        needEP = PoundageUtil.getPoundage(needEP, 1d, 2);
        Double realMoney = 0d;
        if (discountEP > 0) {
            if (exchangeEp >= needEP) {
                //EP足额，以EP为主
                realMoney = PoundageUtil.getPoundage(joinEp * joinRmbScale / 100, 1d, 2);
            } else if (exchangeEp < needEP && exchangeEp > 0) {
                needEP = exchangeEp;
                realMoney = PoundageUtil.getPoundage(joinEp - needEP, 1d, 2);
            } else {
                realMoney = joinEp;
                needEP = 0d;
            }
        } else {
            realMoney = joinEp;
            needEP = 0d;
        }

        /*余额支付时先进行一些基本判断*/
        if (source == BankCardType.BALANCE.getCode().intValue()) {
            if (StringUtils.isBlank(user.getPayPwd())) {
                return new JsonResult(2, "请先设置支付密码");
            }
            if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
                return new JsonResult(3, "支付密码不正确");
            }
            if (vo.getScore() < 0) {
                return new JsonResult(4, "请输入合法有效的付款金额");
            }
            if (user.getScore() == null || user.getScore() < realMoney) {
                return new JsonResult(5, "您的余额不足");
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // 生成预订单   score :EP+现金         discountEP ：ep抵扣金额     confirmScore:扣除三级分销和手续费的金额
        WalletRecharge model = new WalletRecharge();
        model.setUserId(token.getId());
        model.setStoreUserId(vo.getStoreUserId());
        model.setScore(joinEp);
        model.setConfirmScore(joinEp);
        model.setBusinessSendEp(0d);
        model.setDiscountEP(needEP);
        model.setFirstReferrerScale(PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.JOINFIRSTREFERRERSCALE), 0d), 100, 4));
        model.setSecondReferrerScale(PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.JOINSECONDREFERRERSCALE), 0d), 100, 4));
        model.setThirdReferrerScale(PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.JOINTHIRDREFERRERSCALE), 0d), 100, 4));
        //获取设置参数
        double tradeRate = 0;
        //手续费 = 三级分销+ep赠送+平台抽佣
        double poundage = PoundageUtil.getPoundage(model.getScore() * (model.getBusinessSendEp() + model.getFirstReferrerScale() + model.getSecondReferrerScale() + model.getThirdReferrerScale()), 1d);
        poundage = poundage + (model.getConfirmScore() - poundage) * tradeRate;
        poundage = PoundageUtil.getPoundage(poundage, 1d);
        model.setPoundage(poundage);

        if (source == BankCardType.ALIPAY.getCode().intValue()) {
            model.setSource(BankCardType.JOIN_ALIPAY.getCode());
            model.setStoreUserId(Constant.SYSTEM_USERID);
            model.setRemark("APP支付宝加入合伙人支付");
            walletRechargeService.add(model);
            result.put("orderNo", model.getOrderNo());
            result.put("userId", token.getId());
            result.put("returnUrl", environment);
            result.put("orderTitle", "加入合伙人支付");
            result.put("tranType", 60);

            //原生支付
            String notifyUrl = "";
            SecondCallBack secondCallBack = secondCallBackService.getById(BankCardType.JOIN_ALIPAY.getCode() + "");
            if (secondCallBack == null || ToolUtil.isEmpty(secondCallBack.getReturnUrl())) {
                return new JsonResult(-1, "支付回调参数设置不合法");
            }
            if ("test".equals(environment)) {
                notifyUrl = secondCallBack.getTestReturnUrl();
            } else if ("online".equals(environment)) {
                notifyUrl = secondCallBack.getReturnUrl();
            }
            orderTypeService.add(model.getOrderNo(), BankCardType.JOIN_ALIPAY.getCode(), "支付宝加入合伙人原生支付", tokens);
            String orderInfo = AliPayUtils.alipayPreOrderForApp(model.getOrderNo(), notifyUrl, realMoney, "斗拍商城支付");
            result.put("orderInfo", orderInfo);

        } else if (source == BankCardType.WECHATPAY.getCode().intValue()) {
            model.setSource(BankCardType.JOIN_WEIXIN.getCode());
            model.setStoreUserId(Constant.SYSTEM_USERID);
            model.setRemark("APP微信加入合伙人支付");
            model.setScenes(vo.getScenes());
            walletRechargeService.add(model);
            String ip = vo.getIp();
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr();
            }
            vo.setScore(PoundageUtil.getPoundage(vo.getScore(), 1d, 2));
            realMoney = PoundageUtil.getPoundage(realMoney, 1d, 2);
            String sScore = vo.getScore() * 100 + "";
            String realMoneyStr = realMoney * 100 + "";
            String money = realMoneyStr.substring(0, sScore.indexOf("."));
            //扫码支付
            String attach = token.getId() + "@" + BankCardType.JOIN_WEIXIN.getCode();
            //场景：APP加入合伙人
            if (ToolUtil.isEmpty(vo.getScenes()) || vo.getScenes().intValue() != ScenesType.WEIXIN_STORE.getCode().intValue()) {
                GenerateOrder generateOrder = new GenerateOrder();
                Map<String, String> generate = generateOrder.generate(money, ip, attach, model.getOrderNo(), joinWeixinNotifyUrl);
                result.put("generate", generate);
                //场景：微店加入合伙人
            } else if (vo.getScenes().intValue() == ScenesType.WEIXIN_STORE.getCode().intValue()) {
                GenerateH5Order order = new GenerateH5Order();
                Map<String, String> wxMap = order.generate(money, ip, attach, model.getOrderNo(), user.getOpenId(), wxStoreH5joinWeixinNotifyUrl);
                Map<String, String> generate = new HashMap<String, String>();
                generate.put("appid", wxMap.get("appId"));
                generate.put("noncestr", wxMap.get("nonceStr"));
                generate.put("timestamp", wxMap.get("timeStamp"));
                generate.put("package", wxMap.get("package"));
                generate.put("prepayid", wxMap.get("prepayid"));
                generate.put("sign", wxMap.get("paySign"));
                result.put("generate", generate);
            }
        } else if (source == BankCardType.BALANCE.getCode().intValue()) {
            model.setStoreUserId(Constant.SYSTEM_USERID);
            model.setSource(BankCardType.JOIN_BALANCE.getCode());
            model.setStatus(RechargeType.PENDING.getCode());
            model.setRemark("APP余额加入合伙人支付");
            walletRechargeService.add(model);
            //余额支付直接调用三级分销等业务
            boolean flag = walletRechargeService.joinPartnerHandler(user, model.getOrderNo());
            result.put("isPaySuccess", flag);
        }
        result.put("orderNo", model.getOrderNo());
        result.put("payTime", model.getCreateTime().getTime());
        result.put("joinPrice", model.getScore());
        return new JsonResult(result);
    }


    /**
     * 加入合伙人支付支付宝回调
     */
    @ResponseBody
    @RequestMapping(value = "/alipaycallback", method = RequestMethod.POST)
    public JsonResult faceAipayCallback(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);
        logger.error("回调参数vo：" + JSON.toJSONString(vo));
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(2, "订单号参数错误");
        }
        if (vo.getSource() != BankCardType.JOIN_ALIPAY.getCode().intValue()) {
            return new JsonResult(1, "回调类型错误");
        }
        User user = userService.getById(token.getId());
        WalletRecharge model = walletRechargeService.getByOrderNo(vo.getOrderNo(), user.getId());
        if (model == null) {
            return new JsonResult(2, "预付单不存在");
        }
        if (model.getSource().intValue() == BankCardType.JOIN_ALIPAY.getCode().intValue()) {
            //支付宝面对面扫码支付
            walletRechargeService.joinPartnerHandler(user, vo.getOrderNo());
        } else {
            logger.error("999999 ：数据库支付回调类型配置错误，请检查");
        }
        // 操作成功返回用户当前积分
        Map<String, Object> result = new HashMap<String, Object>();
        user = userService.getById(token.getId());
        result.put("score", FormatUtils.formatDouble(user.getScore()));
        //成功回调后记录
        payCallbackService.doCallBackAction(model.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), true);
        return new JsonResult(result);
    }


    /**
     * 加入合伙人支付微信支付回调
     */
    @ResponseBody
    @RequestMapping(value = "/wxcallback", method = RequestMethod.POST)
    public void wxScanCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resXml = HttpsUtil.getXmlFromRequest(request);
        String msg = "";
        String orderNo = "";
        //开始前记录回调
        payCallbackService.doCallBackAction(null, request.getRequestURI(), resXml + "微信支付加入合伙人", false);
        logger.error("***********call***********");
        logger.error(resXml);
        logger.error("***********wxCallback***********");
        WalletRecharge recharge = null;
        if (StringUtils.isEmpty(resXml)) {
            logger.info("微信返回XML数据为空");
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";
            msg += "微信返回XML数据为空 " + resXml;
            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        }
        Map<String, String> m = new HashMap<String, String>();
        m = XMLUtil.doXMLParse(resXml);
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
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
        String signStr = generateOrder.createSign(packageParams);
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
            //微信验签成功回调
            payCallbackService.doCallBackAction(orderNo, request.getRequestURI(), resXml, false);
            if ("SUCCESS".equals((String) packageParams.get("result_code")) && userId != null) {
                recharge = walletRechargeService.getByOrderNo(orderNo, userId);
                boolean b = fee == null || recharge == null;
                boolean b1 = true;
                if (fee != null && recharge != null) {
                    double fee1 = PoundageUtil.getPoundage(fee.doubleValue(), 1d);
                    double confirmScore = PoundageUtil.getPoundage(recharge.getConfirmScore() * 100, 1d);
                    b1 = confirmScore != fee1 || recharge.getStatus() != RechargeType.PENDING.getCode() || !StringUtils.isEmpty(value);
                }
                if (b && b1) {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    msg += "微信回调不符合业务规则 ";
                    msg += resXml;
                } else {
                    Boolean flag = RedisLock.redisLock(key, orderNo, 6);
                    User user = userService.getById(userId);
                    //处理商家固定扫码支付成功后的业务
                    if (flag) {
                        walletRechargeService.joinPartnerHandler(user, orderNo);
                    }
                    logger.error("支付成功");
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    msg += "支付成功";
                }
            } else {
                logger.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                msg += "支付失败,错误信息：" + packageParams.get("err_code");
            }
        }
        //成功回调后记录
        payCallbackService.doCallBackAction(orderNo, request.getRequestURI(), JSON.toJSONString(recharge) + "   " + msg, true);
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
     * 加入合伙人支付微信支付回调-微店回调
     */
    @ResponseBody
    @RequestMapping(value = "/wxStoreH5Callback", method = RequestMethod.POST)
    public void wxStoreH5Callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WxH5CallBack.wxCallback(request, response, payCallbackService, new IWxAction() {
            @Override
            public String doAction(String orderNo, String userId, Double fee, int payType) throws Exception {
                WalletRecharge recharge = walletRechargeService.getByOrderNo(orderNo, userId);
                boolean b = fee == null || recharge == null;
                boolean b1 = true;
                String key = RedisKey.WEIXIN_CALLBACK.getKey() + orderNo;
                String value = Strings.get(key);
                String resXml = "";
                String msg = "";
                if (fee != null && recharge != null) {
                    double fee1 = PoundageUtil.getPoundage(fee.doubleValue(), 1d);
                    double confirmScore = PoundageUtil.getPoundage(recharge.getConfirmScore() * 100, 1d);
                    b1 = confirmScore != fee1 || recharge.getStatus() != RechargeType.PENDING.getCode() || !StringUtils.isEmpty(value);
                }
                if (b && b1) {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    msg += "微信回调不符合业务规则 ";
                    msg += resXml;
                } else {
                    Boolean flag = RedisLock.redisLock(key, orderNo, 6);
                    User user = userService.getById(userId);
                    //处理商家固定扫码支付成功后的业务
                    if (flag) {
                        walletRechargeService.joinPartnerHandler(user, orderNo);
                    }
                    logger.error("支付成功");
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    msg += "支付成功";
                }
                logger.info(msg);
                return JSON.toJSONString(recharge);
            }
        });
    }

    /**
     * 微信加入合伙人支付对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/query/wxjoinorder", method = RequestMethod.POST)
    public JsonResult queryScanCodeWxOrder(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "orderNo不能为空");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户不存在");
        }
        String orderNo = vo.getOrderNo();
        WalletRecharge recharge = walletRechargeService.getByOrderNo(orderNo, user.getId());
        if (recharge == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "订单不存在");
        }
        if (StringUtils.isEmpty(recharge.getStoreUserId())) {
            return new JsonResult("此订单不是扫码支付订单，接口调用错误");
        }
        if (recharge.getStatus().intValue() == RechargeType.PENDING.getCode().intValue()) {
            Boolean isSucess = WechatUtil.isAppPaySucess(orderNo);
            String key = RedisKey.WEIXIN_CALLBACK.getKey() + orderNo;
            Boolean flag = RedisLock.redisLock(key, orderNo, 7);
            if (flag && isSucess) {
                walletRechargeService.joinPartnerHandler(user, orderNo);
                return new JsonResult(ResultCode.SUCCESS.getCode(), "扫码订单支付确认回调成功");
            }
        }
        if (recharge.getStatus().intValue() == RechargeType.TRANSFER_SUCCESS.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "扫码订单支付微信已经回调成功");
        }
        logger.error("0000000000----> 微信扫码支付失败");
        //成功回调后记录
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo) + " 微信扫码支付对账查询回调失败", true);
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }


    /**
     * 支付宝加入合伙人支付对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/query/alipayjoinorder", method = RequestMethod.POST)
    public JsonResult queryAlipayScanCodeWxOrder(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "orderNo不能为空");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
        }
        String orderNo = vo.getOrderNo();
        WalletRecharge recharge = walletRechargeService.getByOrderNo(orderNo, user.getId());
        if (recharge == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "订单不存在");
        }
        if (StringUtils.isEmpty(recharge.getStoreUserId())) {
            return new JsonResult("此订单不是扫码支付订单，接口调用错误");
        }
        if (recharge.getStatus().intValue() == RechargeType.PENDING.getCode().intValue()) {
            Boolean isSucess = AliPayUtils.isSZAliPaySucess(orderNo);
//            Boolean isSucess = WechatUtil.isAppScanAliPaySucess(orderNo);
            String key = RedisKey.WEIXIN_CALLBACK.getKey() + orderNo;
            Boolean flag = RedisLock.redisLock(key, orderNo, 7);
            if (flag && isSucess) {
                walletRechargeService.joinPartnerHandler(user, orderNo);
                return new JsonResult(ResultCode.SUCCESS.getCode(), "扫码订单支付回调成功");
            }
        }
        if (recharge.getStatus().intValue() == RechargeType.TRANSFER_SUCCESS.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "扫码订单支付宝已经回调成功");
        }
        logger.error("0000000000----> 支付失败");
        //成功回调后记录
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo) + " 支付宝扫码支付对账查询回调失败", true);
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }


    public static void main(String[] args) throws IOException, JDOMException {
    /*	GenerateOrder generateOrder = new GenerateOrder();
        StringBuffer sb= generateOrder.getRequestXml("https://api.mch.weixin.qq.com/pay/orderquery",generateOrder.buildQueryOrderInfo("20170401193127001037"));
		QueryResponseInfoVo responseInfoVo= generateOrder.getQueryOrderResponseInfo(sb);
		Map map = XMLUtil.doXMLParse(sb.toString());
		System.out.println(generateOrder.createSign(new TreeMap<>(map)));
		System.out.println("***********");
		System.out.println(generateOrder.createSign(new TreeMap<String,String>(map)));
*/
        //System.out.println(WechatUtil.isAppScanAliPaySucess("20170428115454480065"));
        String msg = "jfldsjflsdjflsjf@5";
        System.out.println(msg.split("@")[0]);
        System.out.println(msg.split("@")[1]);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sdf.format(new Date()));
    }


}
