package com.yanbao.controller;

import com.alibaba.fastjson.JSON;
import com.mall.model.*;
import com.yanbao.constant.*;
import com.yanbao.core.model.Token;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.redis.Strings;
import com.yanbao.service.*;
import com.yanbao.util.*;
import com.yanbao.util.alipay.AliPayUtils;
import com.yanbao.util.alipay.Alipay;
import com.yanbao.util.alipay.OrderInfoUtil2_0;
import com.yanbao.util.h5.GenerateH5Order;
import com.yanbao.util.h5.IWxAction;
import com.yanbao.util.h5.WxH5CallBack;
import com.yanbao.util.wechatpay.GenerateOrder;
import com.yanbao.vo.WalletRechargeVo;
import com.yanbao.vo.WalletV42Vo;
import com.yanbao.vo.WalletVo;
import com.yanbao.vo.WxH5OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private WalletDonateService walletDonateService;
    @Autowired
    private WalletRechargeService walletRechargeService;
    @Autowired
    private WalletExchangeService walletExchangeService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private UserBankcardService userBankcardService;
    @Autowired
    private GoodsIssueDetailService goodsIssueDetailService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private PayCallbackService payCallbackService;
    @Autowired
    private PayDistributionService payDistributionService;
    @Autowired
    private SecondCallBackService secondCallBackService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Value("${recharge_weixin_notifyUrl}")
    private String recharge_weixin_notifyUrl;
    @Value("${scan_weixin_notifyUrl}")
    private String scanWeixinNotifyUrl;
    @Value("${store_scan_weixin_notifyUrl}")
    private String storeScanWeixinNotifyUrl;
    @Value("${wxH5_store_weixin_notifyUrl}")
    private String wxH5storeWeixinNotifyUrl;

    //    @Value("${store_scan_alipay_notifyUrl}")
//    private String storeScanAlipayNotifyUrl;
//    @Value("${store_scan_alipay_byalipay_notifyUrl}")
//    private String storeScanAlipayByAlipayNotifyUrl;
//    @Value("${scan_alipay_notifyUrl}")
//    private String scanAlipayNotifyUrl;
    @Value("${environment}")
    private String environment;

    /**
     * 积分赠送/转账
     */
    @ResponseBody
    @RequestMapping(value = "/donate", method = RequestMethod.POST)
    public JsonResult donate(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        // 检查后台是否开启赠送开关
        ParamUtil util = ParamUtil.getIstance();
        if (ToolUtil.parseInt(util.get(Parameter.DONATESWICH), 0) != StatusType.TRUE.getCode()) {
            return new JsonResult(0, "赠送功能暂未开启");
        }
        Token token = TokenUtil.getSessionUser(request);
        if (StringUtils.isBlank(vo.getDonateTo()) || vo.getScore() == null) {
            return new JsonResult(1, "参数异常");
        }
        //判断赠送金额是否是100的整数倍
        if (vo.getScore() % 100 != 0) {
            return new JsonResult(9, "赠送余额必须是100的整数倍");
        }
        double donateMin = ToolUtil.parseDouble(util.get(Parameter.DONATEMIN), 100d);
        double donateMax = ToolUtil.parseDouble(util.get(Parameter.DONATEMAX), 10000d);
        if (vo.getScore() < donateMin || vo.getScore() > donateMax) {
            return new JsonResult(2, "赠送余额必须在[" + donateMin + "," + donateMax + "]之间");
        }
        if (StringUtils.isBlank(vo.getPayPwd())) {
            return new JsonResult(3, "支付密码不能为空");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getPayPwd())) {
            return new JsonResult(4, "请先设置支付密码");
        }
        if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
            return new JsonResult(5, "支付密码不正确");
        }
        if (user.getScore() == null || user.getScore() < vo.getScore()) {
            return new JsonResult(6, "您的余额不足");
        }
        User condition = new User();
        if (vo.getDonateTo().length() < 11) { // UID
            condition.setUid(Integer.parseInt(vo.getDonateTo()));
        } else { // 手机号
            condition.setPhone(vo.getDonateTo());
        }
        User donateUser = userService.getByCondition(condition);
        if (null == donateUser) {
            return new JsonResult(7, "赠送用户不存在");
        }
        if (user.getId().equals(donateUser.getId())) {
            return new JsonResult(8, "不能赠送给自己");
        }
        walletDonateService.donateHandler(user, donateUser, vo.getScore());
        // 操作成功返回用户当前积分
        Map<String, Object> result = new HashMap<String, Object>();
        user = userService.getById(token.getId());
        result.put("score", FormatUtils.formatDouble(user.getScore()));
        return new JsonResult(result);
    }

    /**
     * 积分赠送/转账用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/donate/userinfo", method = RequestMethod.GET)
    public JsonResult donateUserInfo(HttpServletRequest request, WalletVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User condition = new User();
        if (vo.getDonateTo().length() < 11) { // UID
            condition.setUid(Integer.parseInt(vo.getDonateTo()));
        } else { // 手机号
            condition.setPhone(vo.getDonateTo());
        }
        User donateUser = userService.getByCondition(condition);
        if (null == donateUser) {
            return new JsonResult(0, "赠送用户不存在");
        }
        if (token.getId().equals(donateUser.getId())) {
            return new JsonResult(1, "不能赠送给自己");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("nickName", donateUser.getNickName());
        result.put("userName", donateUser.getUserName());
        return new JsonResult(result);
    }

    /**
     * 积分兑换/提现
     */
    @ResponseBody
    @RequestMapping(value = "/exchange", method = RequestMethod.POST)
    public JsonResult exchange(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        // 检查后台是否开启兑换开关
        ParamUtil util = ParamUtil.getIstance();
        if (ToolUtil.parseInt(util.get(Parameter.EXCHANGESWITCH), 0) != StatusType.TRUE.getCode()) {
            return new JsonResult(0, "兑换功能暂未开启");
        }

        Token token = TokenUtil.getSessionUser(request);
        double exchangeMin = ToolUtil.parseDouble(util.get(Parameter.EXCHANGEMIN), 100d);
        double exchangeMax = ToolUtil.parseDouble(util.get(Parameter.EXCHANGEMAX), 10000d);
        if (vo.getScore() == null || vo.getScore() < exchangeMin || vo.getScore() > exchangeMax) {
            return new JsonResult(1, "单笔兑换积分必须在[" + exchangeMin + "," + exchangeMax + "]之间");
        }
        if (StringUtils.isBlank(vo.getPayPwd())) {
            return new JsonResult(2, "支付密码不能为空");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getPayPwd())) {
            return new JsonResult(3, "请先设置支付密码");
        }
        if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
            return new JsonResult(4, "支付密码不正确");
        }
        if (user.getScore() == null || user.getScore() < vo.getScore()) {
            return new JsonResult(5, "您的余额不足");
        }
        List<UserBankcard> userBankcardList = userBankcardService.getList(token.getId());
        if (userBankcardList == null || userBankcardList.size() <= 0 || StringUtils.isBlank(userBankcardList.get(0).getCardNo())) {
            return new JsonResult(6, "请先设置银行卡");
        }
        walletExchangeService.exchangeHandler(user, vo.getScore(), userBankcardList.get(0));
        // 操作成功返回用户当前积分
        Map<String, Object> result = new HashMap<String, Object>();
        user = userService.getById(token.getId());
        result.put("score", FormatUtils.formatDouble(user.getScore()));
        //邮箱推送
        emailService.sendEmail("斗拍推送", "系统有一条新的提现消息，请注意查看", util.get(Parameter.MAILPEOPLE));
        return new JsonResult(result);
    }

    /**
     * @param setting
     */
    private void setDefaultSetting(SysSetting setting) {
        setting.setDonateSwich(0);
        setting.setDonateMax(10000d);
        setting.setDonateMin(10d);
        setting.setExchangeSwitch(0);
        setting.setExchangeMax(10000d);
        setting.setExchangeMin(100d);
        setting.setExchangePoundageScale(0.05d);
    }

    /**
     * 积分充值
     */
    @ResponseBody
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public JsonResult recharge(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        String tokens = TokenUtil.getToken(request);
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (vo.getSource() != BankCardType.ALIPAY.getCode() && vo.getSource() != BankCardType.WECHATPAY.getCode()) {
            return new JsonResult(1, "目前仅支持支付宝、微信支付付款");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // 生成预充值订单
        WalletRecharge model = new WalletRecharge();
        model.setUserId(token.getId());
        model.setScore(vo.getScore());
        model.setConfirmScore(vo.getScore());
        model.setSource(vo.getSource());
        model.setScenes(vo.getScenes());
        walletRechargeService.add(model);
        result.put("orderNo", model.getOrderNo());
        result.put("userId", token.getId());

        logger.debug("*******************************");

        logger.debug(JSON.toJSONString(vo));
        logger.debug("  *******************************");

        if (vo.getSource() == BankCardType.ALIPAY.getCode()) {
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(Alipay.APP_ID, vo.getScore());
            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            String sign = OrderInfoUtil2_0.getSign(params, Alipay.RSA_PRIVATE);
//            final String orderInfo = orderParam + "&" + sign;
            String notifyUrl = "";
            SecondCallBack secondCallBack = secondCallBackService.getById(BankCardType.ALIPAY.getCode() + "");
            if (secondCallBack == null || ToolUtil.isEmpty(secondCallBack.getReturnUrl())) {
                return new JsonResult(-1, "支付回调参数设置不合法");
            }
            if ("test".equals(environment)) {
                notifyUrl = secondCallBack.getTestReturnUrl();
            } else if ("online".equals(environment)) {
                notifyUrl = secondCallBack.getReturnUrl();
            }
            Double payMoney = PoundageUtil.getPoundage(vo.getScore(), 1d, 2);
            orderTypeService.add(model.getOrderNo(), BankCardType.ALIPAY.getCode(), "支付宝充值原生支付", tokens);
            String orderInfo = AliPayUtils.alipayPreOrderForApp(model.getOrderNo(), notifyUrl, payMoney, "斗拍商城支付");
            result.put("orderInfo", orderInfo);
            result.put("returnUrl", environment);
            result.put("orderTitle", "斗拍商城充值");
            result.put("tranType", 20);
        } else if (vo.getSource() == BankCardType.WECHATPAY.getCode()) {
            //场景：APP余额充值
            if (ToolUtil.isEmpty(vo.getScenes()) || vo.getScenes().intValue() != ScenesType.WEIXIN_STORE.getCode().intValue()) {
                String ip = vo.getIp();
                if (StringUtils.isBlank(ip)) {
                    ip = request.getRemoteAddr();
                }
                GenerateOrder generateOrder = new GenerateOrder();
                String sScore = vo.getScore() * 100 + "";
                Map<String, String> generate = generateOrder.generate(sScore.substring(0, sScore.indexOf(".")), ip, token.getId(), model.getOrderNo(), recharge_weixin_notifyUrl);
                result.put("generate", generate);
                //场景：微店余额充值
            } else if (vo.getScenes().intValue() == ScenesType.WEIXIN_STORE.getCode().intValue()) {
                String ip = request.getRemoteAddr();
                String attach = token.getId() + "@" + BankCardType.WECHATPAY.getCode();
                GenerateH5Order order = new GenerateH5Order();
                double price = PoundageUtil.getPoundage(vo.getScore() * 100, 1d, 2);
                String m = price + "";
                String money = m.substring(0, m.indexOf("."));
                Map<String, String> wxMap = order.generate(money, ip, attach, model.getOrderNo(), user.getOpenId(), wxH5storeWeixinNotifyUrl);
                Map<String, String> generate = new HashMap<String, String>();
                generate.put("appid", wxMap.get("appId"));
                generate.put("noncestr", wxMap.get("nonceStr"));
                generate.put("timestamp", wxMap.get("timeStamp"));
                generate.put("package", wxMap.get("package"));
                generate.put("prepayid", wxMap.get("prepayid"));
                generate.put("sign", wxMap.get("paySign"));
                result.put("generate", generate);
            }
        }

        return new JsonResult(result);
    }


    /**
     * 积分充值回调
     */
    @ResponseBody
    @RequestMapping(value = "/recharge/callback", method = RequestMethod.POST)
    public JsonResult rechargeCallback(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(2, "订单号参数错误");
        }
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);

        if (vo.getSource() != BankCardType.ALIPAY.getCode() && vo.getSource() != BankCardType.WECHATPAY.getCode() && vo.getSource() != BankCardType.SCAN_CODE_ALIPAY.getCode().intValue() && vo.getSource().intValue() != BankCardType.SCAN_CODE_WEIXIN.getCode().intValue()) {
            return new JsonResult(1, "目前仅支持支付宝、微信支付付款");
        }
        User user = userService.getById(token.getId());
        WalletRecharge model = walletRechargeService.getByOrderNo(vo.getOrderNo(), user.getId());
        if (model == null) {
            return new JsonResult(2, "预付单不存在");
        }
        //支付宝充值
        walletRechargeService.rechargeHandler(user, vo.getOrderNo());
        // 操作成功返回用户当前积分
        Map<String, Object> result = new HashMap<String, Object>();
        user = userService.getById(token.getId());
        result.put("score", FormatUtils.formatDouble(user.getScore()));
        //成功回调后记录
        payCallbackService.doCallBackAction(model.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), true);
        return new JsonResult(result);
    }


    /**
     * 面对面扫码支付支付宝回调
     */
    @ResponseBody
    @RequestMapping(value = "/face/alipaycallback", method = RequestMethod.POST)
    public JsonResult faceAipayCallback(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(3, "订单号参数错误");
        }
        if (vo.getSource() != BankCardType.ALIPAY.getCode() && vo.getSource() != BankCardType.WECHATPAY.getCode() && vo.getSource() != BankCardType.SCAN_CODE_ALIPAY.getCode().intValue() && vo.getSource().intValue() != BankCardType.SCAN_CODE_WEIXIN.getCode().intValue()) {
            return new JsonResult(1, "目前仅支持支付宝、微信支付付款");
        }
        User user = userService.getById(token.getId());
        WalletRecharge model = walletRechargeService.getByOrderNo(vo.getOrderNo(), user.getId());
        if (model == null) {
            return new JsonResult(2, "预付单不存在");
        }
        if (model.getSource().intValue() == BankCardType.SCAN_CODE_ALIPAY.getCode().intValue()) {
            //支付宝面对面扫码支付
            walletRechargeService.scanCodeHandler(user, vo.getOrderNo());
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
     * 商家二维码-支付宝回调
     */
    @ResponseBody
    @RequestMapping(value = "/storeAlipay/callback", method = RequestMethod.POST)
    public JsonResult storeAlipayCallback(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(1, "订单号不能为空");
        }
        Set<Integer> flag = new HashSet();
        flag.add(BankCardType.STORE_SCAN_APP_ALIPAY.getCode());
        if (!flag.contains(vo.getSource())) {
            logger.error("alipaycallback 支付宝回调类型错误  " + vo.getSource());
            return new JsonResult(1, "支付宝回调类型错误");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }

        WalletRecharge model = walletRechargeService.getByOrderNo(vo.getOrderNo(), token.getId());
        if (model == null) {
            return new JsonResult(2, "预付单不存在");
        }

        //商家固定扫码发起支付宝支付
        walletRechargeService.storeScanCodeHandler(user, vo.getOrderNo());

        //成功回调后记录
        payCallbackService.doCallBackAction(model.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), true);
        return new JsonResult();
    }

    /**
     * 商家二维码-支付宝回调(支付宝客户端直接发起的)
     */
    @ResponseBody
    @RequestMapping(value = "/storeAlipayByAlipay/callback", method = RequestMethod.POST)
    public JsonResult storeAlipayByAlipayCallback(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(1, "订单号不能为空");
        }
        Set<Integer> flag = new HashSet();
        flag.add(BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode());
        if (!flag.contains(vo.getSource())) {
            logger.error("alipaycallback 支付宝回调类型错误  " + vo.getSource());
            return new JsonResult(1, "支付宝回调类型错误");
        }
        WalletRecharge model = walletRechargeService.getUserOrderByOrderNo(vo.getOrderNo());
        if (model == null) {
            return new JsonResult(2, "预付单不存在");
        }
        //商家固定扫码发起支付宝支付
        walletRechargeService.storeScanCodeHandler(null, vo.getOrderNo());
        //成功回调后记录
        payCallbackService.doCallBackAction(model.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), true);
        return new JsonResult();
    }


    /**
     * 积分充值回调(手工调用，备用)
     */
    @ResponseBody
    @RequestMapping(value = "/handle/callback", method = RequestMethod.GET)
    public JsonResult handleCallback(HttpServletRequest request, String userId, String orderNo, String flag) throws Exception {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(orderNo)) {
            return new JsonResult("orderNo or 或者 userId不能为空");
        }
        //开始前记录回调
        payCallbackService.doCallBackAction(orderNo, request.getRequestURI(), JSON.toJSONString(request.getParameterMap()), false);
        if (!"2000000".equals(flag)) {
            return new JsonResult("参数不合法");
        }
        String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
        String value = Strings.get(key);
        if (!StringUtils.isEmpty(value)) {
            return new JsonResult("不要重复调用");
        }
        Strings.setEx(RedisKey.HANDLE_CALLBACK.getKey() + orderNo, RedisKey.HANDLE_CALLBACK.getSeconds(), orderNo);
        User user = userService.getById(userId);
        walletRechargeService.rechargeHandler(user, orderNo);
        // 操作成功返回用户当前积分
        Map<String, Object> result = new HashMap<String, Object>();
        user = userService.getById(userId);
        result.put("score", FormatUtils.formatDouble(user.getScore()));
        //成功回调后记录
        payCallbackService.doCallBackAction(orderNo, request.getRequestURI(), JSON.toJSONString(request.getParameterMap()), true);
        return new JsonResult(result);
    }


    /**
     * 微信充值回调
     */
    @ResponseBody
    @RequestMapping(value = "/recharge/wxCallback", method = RequestMethod.POST)
    public void wxCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resXml = "";
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s;
        String orderNo = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        Map<String, String> m = new HashMap<String, String>();
        m = XMLUtil.doXMLParse(sb.toString());
        //开始前记录回调
        payCallbackService.doCallBackAction(null, request.getRequestURI(), "微信支付：充值、买对面扫码，商家扫码订单微信原始参数: " + JSON.toJSONString(m), false);
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
        String signStr = generateOrder.createSignWithObjectKV(packageParams);
        String sign = (String) packageParams.get("sign");
        String attach = (String) packageParams.get("attach");
        String msg = "";
        WalletRecharge recharge = null;
        if (StringUtils.isEmpty(sign) || !signStr.equals(sign)) {
            logger.info("支付失败,错误信息,验签失败");
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";
            msg = "支付失败,错误信息,验签失败";
        } else {
            String userId = "";
            int payType = 0;
            if (!StringUtils.isEmpty(attach)) {
                if (attach.split("@").length > 1) {
                    //根据@进行分割,获取payType
                    payType = Integer.parseInt(attach.split("@")[1]);
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
                recharge = walletRechargeService.getByOrderNo(orderNo, userId);
                double fee1 = PoundageUtil.getPoundage(fee.doubleValue(), 1d);
                double confirmScore = PoundageUtil.getPoundage(recharge.getConfirmScore() * 100, 1d);
                boolean b = fee == null || recharge == null;
                boolean b1 = true;
                if (!b) {
                    b1 = confirmScore != fee1 || recharge.getStatus() != RechargeType.PENDING.getCode() || !StringUtils.isEmpty(value);
                }

                if (b && b1) {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    msg = "回调业务boolean值：" + b + " 为true，不符合处理回调业务规则" + resXml;
                } else {
                    logger.error("*******************100000*********************");
                    logger.error("*******************100000********************");
                    logger.error(JSON.toJSONString(recharge));
                    logger.error("****************************************");
                    logger.error("****************************************");
                    Strings.setEx(RedisKey.WEIXIN_CALLBACK.getKey() + orderNo, RedisKey.WEIXIN_CALLBACK.getSeconds(), orderNo);
                    User user = userService.getById(userId);
                    if (payType == 2 && recharge.getSource().intValue() == BankCardType.SCAN_CODE_WEIXIN.getCode().intValue()) {
                        //处理面对面扫码支付业务
                        walletRechargeService.scanCodeHandler(user, orderNo);
                        msg += "处理面对面扫码支付业务";
                    } else if (payType == 5) {
                        //处理商家固定扫码支付业务
                        walletRechargeService.storeScanCodeHandler(user, orderNo);
                        msg += "处理商家固定扫码支付业务";
                    } else {
                        //处理用户充值业务
                        walletRechargeService.rechargeHandler(user, orderNo);
                        msg += "处理用户充值业务";
                    }
                    logger.error("支付成功");
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    msg += "支付成功  " + resXml;
                }
            } else {
                logger.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                msg += "支付失败,错误信息：" + packageParams.get("err_code") + resXml;
            }
        }

        //成功回调后记录
        payCallbackService.doCallBackAction(orderNo, request.getRequestURI(), JSON.toJSONString(recharge) + "  " + msg, true);
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
     * 微信充值回调-微店微信回调
     */
    @ResponseBody
    @RequestMapping(value = "/recharge/wxStoreH5Callback", method = RequestMethod.POST)
    public void wxStoreH5Callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WxH5CallBack.wxCallback(request, response, payCallbackService, new IWxAction() {
            @Override
            public String doAction(String orderNo, String userId, Double fee, int payType) throws Exception {
                WalletRecharge recharge = walletRechargeService.getByOrderNo(orderNo, userId);
                String key = RedisKey.WEIXIN_CALLBACK.getKey() + orderNo;
                String value = Strings.get(key);
                double fee1 = PoundageUtil.getPoundage(fee.doubleValue(), 1d);
                double confirmScore = PoundageUtil.getPoundage(recharge.getConfirmScore() * 100, 1d);
                boolean flag = fee == null || recharge == null || confirmScore != fee1 || recharge.getStatus() != RechargeType.PENDING.getCode() || !StringUtils.isEmpty(value);
                String resXml = "";
                String msg = "";
                if (flag) {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    msg = "回调业务boolean值：" + flag + " 为true，不符合处理回调业务规则" + resXml;
                    logger.error(msg);
                } else {
                    logger.error("*******************100000*********************");
                    logger.error("*******************100000********************");
                    logger.error(JSON.toJSONString(recharge));
                    logger.error("****************************************");
                    logger.error("****************************************");
                    Strings.setEx(RedisKey.WEIXIN_CALLBACK.getKey() + orderNo, RedisKey.WEIXIN_CALLBACK.getSeconds(), orderNo);
                    User user = userService.getById(userId);
                    if (payType == BankCardType.WECHATPAY.getCode().intValue() && recharge.getSource().intValue() == BankCardType.WECHATPAY.getCode().intValue()) {
                        //处理用户充值业务
                        walletRechargeService.rechargeHandler(user, orderNo);
                        msg += "处理用户充值业务";
                    }
                    logger.error("支付成功");
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    msg += "支付成功  " + resXml;
                    logger.info(msg);
                }
                return JSON.toJSONString(recharge);
            }
        });
    }

    /**
     * 积分钱包-最新流水
     */
    @ResponseBody
    @RequestMapping(value = "/record/latest", method = RequestMethod.GET)
    public JsonResult latestRecord(HttpServletRequest request, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        List<WalletRecord> result = walletRecordService.getLatestList(token.getId());
        return new JsonResult(result);
    }

    /**
     * 积分流水
     */
    @ResponseBody
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public JsonResult record(HttpServletRequest request, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        String[] recordTypes = request.getParameterValues("recordType");
        PageResult<WalletRecord> result = walletRecordService.getPage(token.getId(), recordTypes, page);
        return new JsonResult(result);
    }

    /**
     * 积分流水详情
     */
    @ResponseBody
    @RequestMapping(value = "/record/detail", method = RequestMethod.GET)
    public JsonResult recordDetail(HttpServletRequest request, Integer recordType, String orderNo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (recordType == null || StringUtils.isBlank(orderNo)) {
            return new JsonResult(0, "参数异常");
        }
        Object result = null;
        if (RecordType.RECHARGE.getCode() == recordType) {
            result = walletRechargeService.getByOrderNo(orderNo, token.getId());

        } else if (RecordType.EXCHANGE.getCode() == recordType) {
            result = walletExchangeService.getByOrderNo(orderNo, token.getId());

        } else if (RecordType.DONATE.getCode() == recordType) {
            result = walletDonateService.getByOrderNo(orderNo, token.getId());

        } else if (RecordType.DRAW.getCode() == recordType) {
            result = goodsIssueDetailService.getByOrderNo(orderNo, token.getId());

        } else if (RecordType.DRAWWIN.getCode() == recordType) {
            result = goodsIssueDetailService.getByOrderNo(orderNo, token.getId());

        } else if (RecordType.SALE.getCode() == recordType) {
            result = goodsWinService.getByOrderNo(orderNo, token.getId());

        } else if (RecordType.BUY.getCode() == recordType) {
            result = goodsWinService.getByOrderNo(orderNo, token.getId());

        } else if (RecordType.ROLLBACK.getCode() == recordType) {
            result = goodsIssueDetailService.getByOrderNo(orderNo, token.getId());

        } else {
            return new JsonResult(0, "此流水类型无详情查询");
        }
        return new JsonResult(result);
    }

    /**
     * 面对面扫码-确认生成支付单
     */
    @ResponseBody
    @RequestMapping(value = "/buildPayOrder", method = RequestMethod.POST)
    public JsonResult buildPayOrder(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        String tokens = TokenUtil.getToken(request);
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        //支付来源
        int source = vo.getSource().intValue();
        //支付来源是否合法有效
        boolean isVaildPay = source == BankCardType.ALIPAY.getCode().intValue() || source == BankCardType.WECHATPAY.getCode().intValue() || source == BankCardType.BALANCE.getCode().intValue();
        if (!isVaildPay) {
            return new JsonResult(1, "目前仅支持支付宝、微信支付、余额付款");
        }

        //获取支付的三级分销比例和ep比例
        PayDistribution payDistribution = payDistributionService.getByUserId(vo.getStoreUserId(), PayDistributionType.member.getCode());
        if (payDistribution == null) {
            return new JsonResult(6, "商家未设置付款参数");
        }

        Map<String, Object> result = new HashMap<String, Object>();
        // 生成预订单
        WalletRecharge model = new WalletRecharge();
        model.setUserId(token.getId());
        model.setStoreUserId(vo.getStoreUserId());
        model.setScore(vo.getScore());

        //保存ep折扣优惠
        Double totalDiscountEP = 0d;
        //定义实付金额
        Double countMoney = 0d;
        //判断是否使用ep折扣优惠，如果使用，不会进行分销
        if (vo.getDiscountEP() == null || vo.getDiscountEP() == 0) {
            //未使用ep折扣优惠，有分销进行分成
            model.setFirstReferrerScale(payDistribution.getFirstReferrerScale());
            model.setSecondReferrerScale(payDistribution.getSecondReferrerScale());
            model.setThirdReferrerScale(payDistribution.getThirdReferrerScale());
            model.setBusinessSendEp(payDistribution.getBusinessSendEp());
            model.setDiscountEP(0d);
            model.setDoudou(payDistribution.getBusinessSendEp());
            countMoney = vo.getScore();
            model.setRemark("无EP折扣");
        } else {
            //使用ep折扣优惠,没有分销分成
            Map<String, Double> countMap = walletRechargeService.countWalletMoney(user, vo.getStoreUserId(), PayDistributionType.member.getCode(), vo.getScore());
            totalDiscountEP = countMap.get("totalDiscountEP");
            countMoney = countMap.get("countMoney");
            model.setFirstReferrerScale(0d);
            model.setSecondReferrerScale(0d);
            model.setThirdReferrerScale(0d);
            model.setBusinessSendEp(0d);
            model.setDiscountEP(totalDiscountEP);
            model.setDoudou(0d);
            model.setRemark("有EP折扣");
        }
        model.setConfirmScore(countMoney);
        countMoney = PoundageUtil.getPoundage(countMoney, 1d, 2);
        /*余额支付时先进行一些基本判断*/
        if (source == BankCardType.BALANCE.getCode().intValue()) {
            if (StringUtils.isBlank(user.getPayPwd())) {
                return new JsonResult(2, "请先设置支付密码");
            }
            if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
                return new JsonResult(3, "支付密码不正确");
            }
            if (countMoney < 0) {
                return new JsonResult(4, "请输入合法有效的付款金额");
            }
            if (user.getScore() == null || user.getScore() < countMoney) {
                return new JsonResult(5, "您的积分不足");
            }
        }

        //获取设置参数
        double tradeRate = PoundageUtil.divide(ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.TRADERATE), 0d), 100, 4);
        //手续费 = 三级分销+ep赠送+平台抽佣
        double poundage = PoundageUtil.getPoundage(model.getScore() * (model.getBusinessSendEp() + model.getFirstReferrerScale() + model.getSecondReferrerScale() + model.getThirdReferrerScale()) / 100d, 1d);
        poundage = poundage + (model.getConfirmScore() - poundage) * tradeRate;
        poundage = PoundageUtil.getPoundage(poundage, 1d);
        model.setPoundage(poundage);

        if (source == BankCardType.ALIPAY.getCode().intValue()) {
            model.setSource(BankCardType.SCAN_CODE_ALIPAY.getCode());
            model.setRemark("APP支付宝扫码支付：" + model.getRemark());
            walletRechargeService.add(model);

            //原生支付
            String notifyUrl = "";
            SecondCallBack secondCallBack = secondCallBackService.getById(BankCardType.SCAN_CODE_ALIPAY.getCode() + "");
            if (secondCallBack == null || ToolUtil.isEmpty(secondCallBack.getReturnUrl())) {
                return new JsonResult(-1, "支付回调参数设置不合法");
            }
            if ("test".equals(environment)) {
                notifyUrl = secondCallBack.getTestReturnUrl();
            } else if ("online".equals(environment)) {
                notifyUrl = secondCallBack.getReturnUrl();
            }
            orderTypeService.add(model.getOrderNo(), BankCardType.SCAN_CODE_ALIPAY.getCode(), "支付宝面对面扫码原生支付", tokens);
            String orderInfo = AliPayUtils.alipayPreOrderForApp(model.getOrderNo(), notifyUrl, countMoney, "斗拍商城支付");
            result.put("orderInfo", orderInfo);

            result.put("realPayPrice", countMoney);
            result.put("orderNo", model.getOrderNo());
            result.put("userId", token.getId());
            result.put("returnUrl", environment);
            result.put("orderTitle", "面对面支付");
            result.put("tranType", 10);
        } else if (source == BankCardType.WECHATPAY.getCode().intValue()) {
            model.setSource(BankCardType.SCAN_CODE_WEIXIN.getCode());
            model.setRemark("APP微信扫码支付：" + model.getRemark());
            walletRechargeService.add(model);
            String ip = vo.getIp();
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr();
            }
            GenerateOrder generateOrder = new GenerateOrder();
            countMoney = PoundageUtil.getPoundage(countMoney, 1d, 2);
            String sScore = countMoney * 100 + "";

            //扫码支付
            String attach = token.getId() + "@" + BankCardType.WECHATPAY.getCode();
            Map<String, String> generate = generateOrder.generate(sScore.substring(0, sScore.indexOf(".")), ip, attach, model.getOrderNo(), scanWeixinNotifyUrl);
            result.put("generate", generate);
        } else if (source == BankCardType.BALANCE.getCode().intValue()) {
            model.setSource(BankCardType.SCAN_CODE_BALANCE.getCode());
            model.setStatus(RechargeType.SUCCESS.getCode());
            model.setRemark("APP余额扫码支付：" + model.getRemark());
            walletRechargeService.add(model);
            //余额支付直接调用三级分销等业务
            walletRechargeService.scanCodeHandler(user, model.getOrderNo());
        }
        result.put("orderNo", model.getOrderNo());
        result.put("payTime", model.getCreateTime().getTime());
        return new JsonResult(result);
    }

    /**
     * 商家收款码-确认生成支付单
     */
    @ResponseBody
    @RequestMapping(value = "/storeBuildPayOrder", method = RequestMethod.POST)
    public JsonResult storeBuildPayOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody WalletVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        String tokens = TokenUtil.getToken(request);
        logger.error("9999999999999999999999999999999999 storeBuildPayOrder 99999999999999999999999999999999999999999999");
        logger.error(JSON.toJSONString(vo));
        logger.error("999999999999999999999999999999999999999999999999999999999999999999999999999999");
        //支付来源
        int source = vo.getSource().intValue();
        //二维码类型
        int codeType = vo.getCodeType().intValue();
        //支付来源是否合法有效
        boolean isVaildPay = source == BankCardType.ALIPAY.getCode().intValue() || source == BankCardType.WECHATPAY.getCode().intValue() || source == BankCardType.BALANCE.getCode().intValue() ||
                source == BankCardType.ALIPAY_STORE_SCAN.getCode().intValue() || source == BankCardType.WECHATPAY_STORE_SCAN.getCode().intValue();
        if (!isVaildPay) {
            return new JsonResult(1, "目前仅支持支付宝、微信支付、余额付款");
        }

        //判断是否是商家扫码支付宝支付，如果是，user就默认为空
        User user = null;
        if (source != BankCardType.ALIPAY_STORE_SCAN.getCode().intValue()) {
            user = userService.getById(token.getId());
            if (null == user) {
//                logger.error(String.format("Illegal user id[%s]", token.getId()));
//                throw new IllegalArgumentException();
                return new JsonResult(1,  "用户登录失效");
            }
        }

        //获取支付的三级分销比例和ep比例(根据codeType类型来判断)
        PayDistribution payDistribution = null;
        if (codeType == PayDistributionType.store.getCode().intValue()) {

            //商家固定二维码扫码发起支付
            payDistribution = payDistributionService.getByUserId(vo.getStoreUserId(), PayDistributionType.store.getCode());
        }
        if (payDistribution == null) {
            return new JsonResult(6, "商家未设置付款参数");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // 生成预订单
        WalletRecharge model = new WalletRecharge();
        if (source == BankCardType.ALIPAY_STORE_SCAN.getCode().intValue()) {
            model.setUserId(null);
        } else {
            model.setUserId(token.getId());
        }
        model.setStoreUserId(vo.getStoreUserId());
        model.setScore(vo.getScore());

        //保存ep折扣优惠
        Double totalDiscountEP = 0d;
        //定义实付金额
        Double countMoney = 0d;
        //判断是否使用ep折扣优惠，如果使用，不会进行分销
        if (vo.getDiscountEP() == null || vo.getDiscountEP() == 0) {
            //未使用ep折扣优惠，有分销进行分成
            model.setFirstReferrerScale(payDistribution.getFirstReferrerScale());
            model.setSecondReferrerScale(payDistribution.getSecondReferrerScale());
            model.setThirdReferrerScale(payDistribution.getThirdReferrerScale());
            model.setBusinessSendEp(payDistribution.getBusinessSendEp());
            model.setDiscountEP(0d);
            model.setDoudou(payDistribution.getBusinessSendEp());
            model.setRemark("无EP抵扣");
            countMoney = vo.getScore();
        } else {
            //使用ep折扣优惠,没有分销分成
            Map<String, Double> countMap = walletRechargeService.countWalletMoney(user, vo.getStoreUserId(), PayDistributionType.store.getCode(), vo.getScore());
            totalDiscountEP = countMap.get("totalDiscountEP");
            countMoney = countMap.get("countMoney");
            model.setFirstReferrerScale(0d);
            model.setSecondReferrerScale(0d);
            model.setThirdReferrerScale(0d);
            model.setBusinessSendEp(0d);
            model.setDoudou(0d);
            model.setRemark("有EP抵扣");
            model.setDiscountEP(totalDiscountEP);
        }

        //设置实付金额
        model.setConfirmScore(countMoney);
        countMoney = PoundageUtil.getPoundage(countMoney, 1d, 2);
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
            if (user.getScore() == null || user.getScore().intValue() < vo.getScore().intValue()) {
                return new JsonResult(5, "您的积分不足");
            }
        }

        //获取设置参数
        ParamUtil util = ParamUtil.getIstance();
        double tradeRate = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.TRADERATE), 0d), 100, 4);
        //手续费 = 三级分销+ep赠送+平台抽佣
        double poundage = PoundageUtil.getPoundage(model.getScore() * (model.getBusinessSendEp() + model.getFirstReferrerScale() + model.getSecondReferrerScale() + model.getThirdReferrerScale()) / 100d, 1d);
        poundage = poundage + (model.getConfirmScore() - poundage) * tradeRate;
        poundage = PoundageUtil.getPoundage(poundage, 1d);
        model.setPoundage(poundage);

        if (source == BankCardType.ALIPAY.getCode().intValue()) {
            //APP端商家二维码发起支付宝支付
            model.setSource(BankCardType.STORE_SCAN_APP_ALIPAY.getCode());
            model.setRemark("APP端商家二维码发起支付宝支付:" + model.getRemark());
            walletRechargeService.add(model);

            //原生支付

            String notifyUrl = "";
            SecondCallBack secondCallBack = secondCallBackService.getById(BankCardType.STORE_SCAN_APP_ALIPAY.getCode() + "");
            if (secondCallBack == null || ToolUtil.isEmpty(secondCallBack.getReturnUrl())) {
                return new JsonResult(-1, "支付回调参数设置不合法");
            }
            if ("test".equals(environment)) {
                notifyUrl = secondCallBack.getTestReturnUrl();
            } else if ("online".equals(environment)) {
                notifyUrl = secondCallBack.getReturnUrl();
            }
            orderTypeService.add(model.getOrderNo(), BankCardType.STORE_SCAN_APP_ALIPAY.getCode(), "支付宝APP内发起商家二维码扫码原生支付", tokens);
            String orderInfo = AliPayUtils.alipayPreOrderForApp(model.getOrderNo(), notifyUrl, countMoney, "斗拍商城支付");
            result.put("orderInfo", orderInfo);


            result.put("realPayPrice", countMoney);
            result.put("returnUrl", environment);
            result.put("orderNo", model.getOrderNo());
            result.put("userId", token.getId());
            result.put("orderTitle", "线下支付");
            result.put("tranType", 40);
        } else if (source == BankCardType.WECHATPAY.getCode().intValue()) {
            //APP端商家固定二维码扫码发起微信支付
            model.setSource(BankCardType.STORE_SCAN_APP_WEIXIN.getCode());
            model.setRemark("APP端商家固定二维码扫码发起微信支付:" + model.getRemark());
            walletRechargeService.add(model);
            String ip = vo.getIp();
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr();
            }
            GenerateOrder generateOrder = new GenerateOrder();
            countMoney = PoundageUtil.getPoundage(countMoney, 1d, 2);
            String sScore = countMoney * 100 + "";
            //扫码支付
            //判断是面对面扫码，还是商家固定二维码扫码
            String attach = "";
            attach = token.getId() + "@" + BankCardType.WECHATPAY_STORE_SCAN.getCode();
            Map<String, String> generate = generateOrder.generate(sScore.substring(0, sScore.indexOf(".")), ip, attach, model.getOrderNo(), scanWeixinNotifyUrl);
            result.put("generate", generate);

        } else if (source == BankCardType.BALANCE.getCode().intValue()) {
            //APP端商家固定二维码扫码发起余额支付
            model.setSource(BankCardType.STORE_SCAN_CODE_BALANCE.getCode());
            model.setRemark("APP端商家固定二维码扫码发起余额支付:" + model.getRemark());
            walletRechargeService.add(model);
            //余额支付直接调用三级分销等业务
            walletRechargeService.storeScanCodeHandler(user, model.getOrderNo());

        } else if (source == BankCardType.ALIPAY_STORE_SCAN.getCode().intValue()) {
            //支付宝客户端商家固定二维码发起支付宝支付
            model.setSource(BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode());
            model.setRemark("支付宝客户端商家固定二维码发起支付宝支付");
            walletRechargeService.add(model);


            //支付宝网页支付
            String notifyUrl = "";
            SecondCallBack secondCallBack = secondCallBackService.getById(BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode() + "");
            if (secondCallBack == null || ToolUtil.isEmpty(secondCallBack.getReturnUrl())) {
                return new JsonResult(-1, "支付回调参数设置不合法");
            }
            if ("test".equals(environment)) {
                notifyUrl = secondCallBack.getTestReturnUrl();
            } else if ("online".equals(environment)) {
                notifyUrl = secondCallBack.getReturnUrl();
            }
            orderTypeService.add(model.getOrderNo(), BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode(), "支付宝支付宝客户端直接发起商家二维码扫码网页支付", tokens);
            String orderInfo = AliPayUtils.alipayPreOrderForWap(model.getOrderNo(), notifyUrl, model.getScore(), "斗拍商城支付");
            result.put("orderInfo", orderInfo);

            result.put("returnUrl", environment);
            result.put("orderNo", model.getOrderNo());
            result.put("userId", null);
            response.reset();
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(orderInfo);
            //直接将完整的表单html输出到页面
            writer.flush();
//            return new JsonResult(result);
        } else if (source == BankCardType.WECHATPAY_STORE_SCAN.getCode().intValue()) {
            //商家固定二维码发起微信扫码(公众号支付)
            if (StringUtils.isEmpty(user.getOpenId())) {
                logger.error(String.format("Illegal user openId，公众号支付获取用户openid失败 ,userId 为：[%s]", user.getId()));
                throw new IllegalArgumentException("公众号支付获取用户openid失败");
            }
            model.setSource(BankCardType.STORE_SCAN_PAGE_WEIXIN.getCode());
            model.setRemark("商家固定二维码发起微信扫码(公众号支付)");
            walletRechargeService.add(model);
            String ip = vo.getIp();
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr();
            }
            String attach = token.getId() + "@" + BankCardType.WECHATPAY_STORE_SCAN.getCode();
            GenerateH5Order order = new GenerateH5Order();
            vo.setScore(PoundageUtil.getPoundage(vo.getScore(), 1d, 2));
            String m = vo.getScore() * 100 + "";
            String money = m.substring(0, m.indexOf("."));
            Map<String, String> wxMap = order.generate(money, ip, attach, model.getOrderNo(), user.getOpenId(), storeScanWeixinNotifyUrl);
            Map<String, String> generate = new HashMap<>();
            generate.put("appid", wxMap.get("appId"));
            generate.put("noncestr", wxMap.get("nonceStr"));
            generate.put("timestamp", wxMap.get("timeStamp"));
            generate.put("package", wxMap.get("package"));
            generate.put("prepayid", wxMap.get("prepayid"));
            generate.put("sign", wxMap.get("paySign"));
            result.put("generate", generate);
        }
        result.put("orderNo", model.getOrderNo());
        result.put("payTime", model.getCreateTime().getTime());
        logger.error("00000000000000000000000000000000000");
        logger.error("商家二维码生成预付单参数  ");
        logger.error("result : " + result);
        logger.error("00000000000000000000000000000000000");
        return new JsonResult(result);
    }



    /**
     * 商家收款码-确认生成支付单 支付宝网页支付
     */
    @ResponseBody
    @RequestMapping(value = "/storeBuildPayPagerder", method = RequestMethod.GET)
    public void storeBuildPayPagerder(HttpServletRequest request, HttpServletResponse response,  WalletVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        String tokens = TokenUtil.getToken(request);
        logger.error("9999999999999999999999999999999999 storeBuildPayOrder 99999999999999999999999999999999999999999999");
        logger.error(JSON.toJSONString(vo));
        logger.error("999999999999999999999999999999999999999999999999999999999999999999999999999999");
        //支付来源
        int source = vo.getSource().intValue();
        //二维码类型
        int codeType = vo.getCodeType().intValue();
        //支付来源是否合法有效
        boolean isVaildPay = source == BankCardType.ALIPAY.getCode().intValue() || source == BankCardType.WECHATPAY.getCode().intValue() || source == BankCardType.BALANCE.getCode().intValue() ||
                source == BankCardType.ALIPAY_STORE_SCAN.getCode().intValue() || source == BankCardType.WECHATPAY_STORE_SCAN.getCode().intValue();
        if (!isVaildPay) {
           logger.error("目前仅支持支付宝、微信支付、余额付款");
        }

        //判断是否是商家扫码支付宝支付，如果是，user就默认为空
        User user = null;
        if (source != BankCardType.ALIPAY_STORE_SCAN.getCode().intValue()) {
            user = userService.getById(token.getId());
            if (null == user) {
                logger.error(String.format("Illegal user id[%s]", token.getId()));
                throw new IllegalArgumentException();
            }
        }

        //获取支付的三级分销比例和ep比例(根据codeType类型来判断)
        PayDistribution payDistribution = null;
        if (codeType == PayDistributionType.store.getCode().intValue()) {
            //商家固定二维码扫码发起支付
            payDistribution = payDistributionService.getByUserId(vo.getStoreUserId(), PayDistributionType.store.getCode());
        }
        if (payDistribution == null) {
            logger.error( "商家未设置付款参数");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // 生成预订单
        WalletRecharge model = new WalletRecharge();
        if (source == BankCardType.ALIPAY_STORE_SCAN.getCode().intValue()) {
            model.setUserId(null);
        } else {
            model.setUserId(token.getId());
        }
        model.setStoreUserId(vo.getStoreUserId());
        model.setScore(vo.getScore());

        //保存ep折扣优惠
        Double totalDiscountEP = 0d;
        //定义实付金额
        Double countMoney = 0d;
        //判断是否使用ep折扣优惠，如果使用，不会进行分销
        if (vo.getDiscountEP() == null || vo.getDiscountEP() == 0) {
            //未使用ep折扣优惠，有分销进行分成
            model.setFirstReferrerScale(payDistribution.getFirstReferrerScale());
            model.setSecondReferrerScale(payDistribution.getSecondReferrerScale());
            model.setThirdReferrerScale(payDistribution.getThirdReferrerScale());
            model.setBusinessSendEp(payDistribution.getBusinessSendEp());
            model.setDiscountEP(0d);
            model.setDoudou(payDistribution.getBusinessSendEp());
            model.setRemark("无EP抵扣");
            countMoney = vo.getScore();
        } else {
            //使用ep折扣优惠,没有分销分成
            Map<String, Double> countMap = walletRechargeService.countWalletMoney(user, vo.getStoreUserId(), PayDistributionType.store.getCode(), vo.getScore());
            totalDiscountEP = countMap.get("totalDiscountEP");
            countMoney = countMap.get("countMoney");
            model.setFirstReferrerScale(0d);
            model.setSecondReferrerScale(0d);
            model.setThirdReferrerScale(0d);
            model.setBusinessSendEp(0d);
            model.setDoudou(0d);
            model.setRemark("有EP抵扣");
            model.setDiscountEP(totalDiscountEP);
        }

        //设置实付金额
        model.setConfirmScore(countMoney);
        countMoney = PoundageUtil.getPoundage(countMoney, 1d, 2);
        /*余额支付时先进行一些基本判断*/
        if (source == BankCardType.BALANCE.getCode().intValue()) {
            if (StringUtils.isBlank(user.getPayPwd())) {
                logger.error( "请先设置支付密码");
            }
            if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
                logger.error( "支付密码不正确");
            }
            if (vo.getScore() < 0) {
                logger.error( "请输入合法有效的付款金额");
            }
            if (user.getScore() == null || user.getScore().intValue() < vo.getScore().intValue()) {
                logger.error( "您的积分不足");
            }
        }

        //获取设置参数
        ParamUtil util = ParamUtil.getIstance();
        double tradeRate = PoundageUtil.divide(ToolUtil.parseDouble(util.get(Parameter.TRADERATE), 0d), 100, 4);
        //手续费 = 三级分销+ep赠送+平台抽佣
        double poundage = PoundageUtil.getPoundage(model.getScore() * (model.getBusinessSendEp() + model.getFirstReferrerScale() + model.getSecondReferrerScale() + model.getThirdReferrerScale()) / 100d, 1d);
        poundage = poundage + (model.getConfirmScore() - poundage) * tradeRate;
        poundage = PoundageUtil.getPoundage(poundage, 1d);
        model.setPoundage(poundage);
        if (source == BankCardType.ALIPAY_STORE_SCAN.getCode().intValue()) {
            //支付宝客户端商家固定二维码发起支付宝支付
            model.setSource(BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode());
            model.setRemark("支付宝客户端商家固定二维码发起支付宝支付");
            walletRechargeService.add(model);

            //支付宝网页支付
            String notifyUrl = "";
            SecondCallBack secondCallBack = secondCallBackService.getById(BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode() + "");
            if (secondCallBack == null || ToolUtil.isEmpty(secondCallBack.getReturnUrl())) {
                logger.error( "支付回调参数设置不合法");
            }
            if ("test".equals(environment)) {
                notifyUrl = secondCallBack.getTestReturnUrl();
            } else if ("online".equals(environment)) {
                notifyUrl = secondCallBack.getReturnUrl();
            }
            orderTypeService.add(model.getOrderNo(), BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode(), "支付宝支付宝客户端直接发起商家二维码扫码网页支付", tokens);
            String orderInfo = AliPayUtils.alipayPreOrderForWap(model.getOrderNo(), notifyUrl, model.getScore(), "斗拍商城支付");
            result.put("orderInfo", orderInfo);

            result.put("returnUrl", environment);
            result.put("orderNo", model.getOrderNo());
            result.put("userId", null);
            response.reset();
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(orderInfo);
            //直接将完整的表单html输出到页面
            writer.flush();
        }
    }


    /**
     * 微信扫码支付对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/query/wxscanorder", method = RequestMethod.POST)
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
        if (org.apache.commons.lang3.StringUtils.isEmpty(recharge.getStoreUserId())) {
            return new JsonResult("此订单不是扫码支付订单，接口调用错误");
        }
        if (recharge.getStatus().intValue() == RechargeType.PENDING.getCode().intValue()) {
            Boolean isSucess = WechatUtil.isAppPaySucess(orderNo);
            String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
            Boolean flag = RedisLock.redisLock(key, orderNo, 6);
            if (flag && isSucess) {
                walletRechargeService.scanCodeHandler(user, orderNo);
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
     * 支付宝扫码支付对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/query/alipayscanorder", method = RequestMethod.POST)
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
        if (org.apache.commons.lang3.StringUtils.isEmpty(recharge.getStoreUserId())) {
            return new JsonResult("此订单不是扫码支付订单，接口调用错误");
        }
        if (recharge.getStatus().intValue() == RechargeType.PENDING.getCode().intValue()) {
//            Boolean isSucess = WechatUtil.isAppScanAliPaySucess(orderNo);
            Boolean isSucess = AliPayUtils.isSZAliPaySucess(orderNo);
            String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
            Boolean flag = RedisLock.redisLock(key, orderNo, 6);
            if (flag && isSucess) {
                walletRechargeService.scanCodeHandler(user, orderNo);
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


    /**
     * 微信充值确认回调
     */
    @ResponseBody
    @RequestMapping(value = "/query/wxChargeCallback", method = RequestMethod.POST)
    public JsonResult wxChargeCallback(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
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
        if (!org.apache.commons.lang3.StringUtils.isEmpty(recharge.getStoreUserId())) {
            return new JsonResult("此订单不是充值订单，接口调用错误");
        }
        if (recharge.getStatus().intValue() == RechargeType.PENDING.getCode().intValue()) {
            Boolean isSucess = false;
            if (recharge.getScenes() != null && recharge.getScenes().intValue() == ScenesType.WEIXIN_STORE.getCode().intValue()) {
                isSucess = WechatUtil.isH5PaySucess(orderNo);
            } else {
                isSucess = WechatUtil.isAppPaySucess(orderNo);
            }
            String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
            Boolean flag = RedisLock.redisLock(key, orderNo, 6);
            if (flag && isSucess) {
                walletRechargeService.rechargeHandler(user, orderNo);
                return new JsonResult(ResultCode.SUCCESS.getCode(), "订单支付确认回调成功");
            }
        }
        if (recharge.getStatus().intValue() == RechargeType.SUCCESS.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "订单微信已经回调成功");
        }
        //成功回调后记录
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo) + " 微信充值确认回调回调失败", true);
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }


    /**
     * 支付宝充值确认回调
     */
    @ResponseBody
    @RequestMapping(value = "/query/alipayChargeCallback", method = RequestMethod.POST)
    public JsonResult alipayChargeCallback(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
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
        if (!org.apache.commons.lang3.StringUtils.isEmpty(recharge.getStoreUserId())) {
            return new JsonResult("此订单不是充值订单，接口调用错误");
        }
        if (recharge.getStatus().intValue() == RechargeType.PENDING.getCode().intValue()) {
//            Boolean isSucess = WechatUtil.isAppScanAliPaySucess(orderNo);
            Boolean isSucess = AliPayUtils.isSZAliPaySucess(orderNo);
            String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
            Boolean flag = RedisLock.redisLock(key, orderNo, 6);
            if (flag && isSucess) {
                walletRechargeService.rechargeHandler(user, orderNo);
                return new JsonResult(ResultCode.SUCCESS.getCode(), "订单支付回调成功");
            }
        }
        if (recharge.getStatus().intValue() == RechargeType.SUCCESS.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "订单支付宝已经回调成功");
        }
        //成功回调后记录
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo) + " 支付宝充值确认回调回调失败", true);
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }

    /**
     * 商家固定扫码微信发起支付回调
     */
    @ResponseBody
    @RequestMapping(value = "/storeWxScanCallback", method = RequestMethod.POST)
    public void wxScanCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resXml = HttpsUtil.getXmlFromRequest(request);
        String msg = "";
        String orderNo = "";
        //开始前记录回调
        payCallbackService.doCallBackAction(null, request.getRequestURI(), " 商家固定扫码微信发起支付回调原始参数 " + resXml, false);
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
            //微信验签成功回调
            payCallbackService.doCallBackAction(orderNo, request.getRequestURI(), resXml, false);
            if ("SUCCESS".equals((String) packageParams.get("result_code")) && userId != null) {
                recharge = walletRechargeService.getByOrderNo(orderNo, userId);
                double fee1 = PoundageUtil.getPoundage(fee.doubleValue(), 1d);
                double confirmScore = PoundageUtil.getPoundage(recharge.getConfirmScore() * 100, 1d);
                boolean b = fee == null || recharge == null;
                boolean b1 = true;
                if (!b) {
                    b1 = confirmScore != fee1 || recharge.getStatus() != RechargeType.PENDING.getCode() || !StringUtils.isEmpty(value) || payType != BankCardType.WECHATPAY_STORE_SCAN.getCode().intValue();
                }

                if (b && b1) {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    msg += "微信回调不符合业务规则 ";
                    msg += resXml;
                } else {
                    Strings.setEx(RedisKey.WEIXIN_CALLBACK.getKey() + orderNo, RedisKey.WEIXIN_CALLBACK.getSeconds(), orderNo);
                    User user = userService.getById(userId);
                    //处理商家固定扫码支付成功后的业务
                    walletRechargeService.storeScanCodeHandler(user, orderNo);
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
        payCallbackService.doCallBackAction(orderNo, request.getRequestURI(), JSON.toJSONString(recharge) + "  " + msg, true);
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
     * 微信客商家固定二维码扫码支付对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/query/storewxscanorder", method = RequestMethod.POST)
    public JsonResult queryStoreScanCodeWxOrder(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
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
        if (org.apache.commons.lang3.StringUtils.isEmpty(recharge.getStoreUserId())) {
            return new JsonResult("此订单不是扫码支付订单，接口调用错误");
        }
        if (recharge.getStatus().intValue() == RechargeType.PENDING.getCode().intValue()) {
            Boolean isSucess = WechatUtil.isAppPaySucess(orderNo);
            String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
            Boolean flag = RedisLock.redisLock(key, orderNo, 6);
            if (flag && isSucess) {
                walletRechargeService.storeScanCodeHandler(user, orderNo);
                return new JsonResult(ResultCode.SUCCESS.getCode(), "商家固定二维码扫码订单支付确认回调成功");
            }
        }
        if (recharge.getStatus().intValue() == RechargeType.TRANSFER_SUCCESS.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "商家固定二维码扫码订单支付微信已经回调成功");
        }
        logger.error("0000000000----> 微信扫码支付失败");
        //成功回调后记录
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo) + " 微信客商家固定二维码扫码支付对账查询回调失败", true);
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }


    /**
     * 支付宝商家固定二维码扫码支付对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/query/storealipayscanorder", method = RequestMethod.POST)
    public JsonResult queryStoreAlipayScanCodeWxOrder(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
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
        if (org.apache.commons.lang3.StringUtils.isEmpty(recharge.getStoreUserId())) {
            return new JsonResult("此订单不是扫码支付订单，接口调用错误");
        }
        if (recharge.getStatus().intValue() == RechargeType.PENDING.getCode().intValue()) {
//            Boolean isSucess = WechatUtil.isAppScanAliPaySucess(orderNo);
            Boolean isSucess = AliPayUtils.isSZAliPaySucess(orderNo);
            String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
            Boolean flag = RedisLock.redisLock(key, orderNo, 6);
            if (flag && isSucess) {
                walletRechargeService.storeScanCodeHandler(user, orderNo);
                return new JsonResult(ResultCode.SUCCESS.getCode(), "商家固定二维码扫码订单支付回调成功");
            }
        }
        if (recharge.getStatus().intValue() == RechargeType.TRANSFER_SUCCESS.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "商家固定二维码扫码订单支付宝已经回调成功");
        }
        logger.error("0000000000----> 支付失败");
        //成功回调后记录
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo) + " 支付宝商家固定二维码扫码支付对账查询回调失败", true);
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }


    /**
     * 支付宝商家固定二维码扫码支付对账查询(只针对支付宝客户端发起支付)
     */
    @ResponseBody
    @RequestMapping(value = "/query/storealipayscanorderbyaliapay", method = RequestMethod.POST)
    public JsonResult queryStoreAlipayScanCodeWxOrderByAlipay(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
        //开始前记录回调
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo), false);
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "orderNo不能为空");
        }
        String orderNo = vo.getOrderNo();
        WalletRecharge recharge = walletRechargeService.getUserOrderByOrderNo(orderNo);
        if (recharge == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "订单不存在");
        }
        if (recharge.getSource() != BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode().intValue()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "此订单不是支付宝直接发起支付的，暂不处理");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(recharge.getStoreUserId())) {
            return new JsonResult("此订单不是扫码支付订单，接口调用错误");
        }
        if (recharge.getStatus().intValue() == RechargeType.PENDING.getCode().intValue()) {
//            Boolean isSucess = WechatUtil.isAppScanAliPaySucess(orderNo);
            Boolean isSucess = AliPayUtils.isSZAliPaySucess(orderNo);
            String key = RedisKey.HANDLE_CALLBACK.getKey() + orderNo;
            Boolean flag = RedisLock.redisLock(key, orderNo, 6);
            if (flag && isSucess) {
                walletRechargeService.storeScanCodeHandler(null, orderNo);
                return new JsonResult(ResultCode.SUCCESS.getCode(), "商家固定二维码扫码订单支付回调成功");
            }
        }
        if (recharge.getStatus().intValue() == RechargeType.TRANSFER_SUCCESS.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "商家固定二维码扫码订单支付宝已经回调成功");
        }
        logger.error("0000000000----> 支付失败");
        //成功回调后记录
        payCallbackService.doCallBackAction(vo.getOrderNo(), request.getRequestURI(), JSON.toJSONString(vo) + " 支付宝商家固定二维码扫码支付对账查询(只针对支付宝客户端发起支付)回调失败", true);
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }


    @ResponseBody
    @RequestMapping(value = "/query/codetopaytab", method = RequestMethod.GET)
    public JsonResult codetopaytab(HttpServletRequest request, Page page, Integer status) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户错误");
        }
        //商家
        Store store = storeService.getById(user.getStoreId());
        if (null == store) {
            return new JsonResult(1, "未找到商铺");
        }
        List<Integer> sources = new ArrayList<Integer>();
        sources.add(BankCardType.STORE_SCAN_APP_ALIPAY.getCode());
        sources.add(BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode());
        sources.add(BankCardType.STORE_SCAN_APP_WEIXIN.getCode());
        sources.add(BankCardType.STORE_SCAN_PAGE_WEIXIN.getCode());
        sources.add(BankCardType.STORE_SCAN_CODE_BALANCE.getCode());
        WalletRecharge wr = new WalletRecharge();
        wr.setUserId(user.getId());
        wr.setStatus(status);
        PageResult<WalletRecharge> result = walletRechargeService.getByscore(wr, sources, page);
        PageResult<WalletRechargeVo> resultList = new PageResult<WalletRechargeVo>();
        resultList.setPageNo(result.getPageNo());
        resultList.setPageSize(result.getPageSize());
        resultList.setTotalSize(result.getTotalSize());
        resultList.setRows(getResult(result.getRows(), store));
        return new JsonResult(resultList);
    }

    @ResponseBody
    @RequestMapping(value = "/query/codetopaydetails", method = RequestMethod.GET)
    public JsonResult codetopaydetails(HttpServletRequest request, WxH5OrderVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户错误");
        }
        //商家
        Store store = storeService.getById(user.getStoreId());
        if (null == store) {
            return new JsonResult(1, "未找到商铺");
        }
        WalletRecharge wr = walletRechargeService.getUserOrderByOrderNo(vo.getOrderNo());
        if (null == wr) {
            return new JsonResult(1, "无此订单");
        }
        if (user.getId().equals(wr.getStoreUserId())) {
            List<WalletRecharge> walletRe = new ArrayList<WalletRecharge>();
            walletRe.add(wr);
            return new JsonResult(getResult(walletRe, store).get(0));
        } else {
            return new JsonResult(2, "非当前用户订单");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/query/statistics", method = RequestMethod.GET)
    public JsonResult statistics(HttpServletRequest request, Integer status, String nowDate) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户错误");
        }
        //商家
        Store store = storeService.getById(user.getStoreId());
        if (null == store) {
            return new JsonResult(1, "未找到商铺");
        }
        List<Integer> sources = new ArrayList<Integer>();
        sources.add(BankCardType.STORE_SCAN_APP_ALIPAY.getCode());
        sources.add(BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode());
        sources.add(BankCardType.STORE_SCAN_APP_WEIXIN.getCode());
        sources.add(BankCardType.STORE_SCAN_PAGE_WEIXIN.getCode());
        sources.add(BankCardType.STORE_SCAN_CODE_BALANCE.getCode());
        WalletRecharge wr = new WalletRecharge();
        wr.setUserId(user.getId());
        wr.setStatus(status);
        if (StringUtils.isEmpty(nowDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            nowDate = sdf.format(new Date());
        }
        Double result = 0d;
        result = walletRechargeService.statistics(wr, sources, nowDate);
        if (null == result) {
            result = 0d;
        }
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("countMoney", result);
        return new JsonResult(map);
    }

    public List<WalletRechargeVo> getResult(List<WalletRecharge> walletRe, Store store) {
        List<WalletRechargeVo> result = new ArrayList<WalletRechargeVo>();
        Map<String, String> source = new HashMap<String, String>();
        source.put(BankCardType.STORE_SCAN_APP_ALIPAY.getCode() + "", "支付宝扫码");
        source.put(BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode() + "", "支付宝扫码");
        source.put(BankCardType.STORE_SCAN_APP_WEIXIN.getCode() + "", "微信扫码");
        source.put(BankCardType.STORE_SCAN_PAGE_WEIXIN.getCode() + "", "微信扫码");
        source.put(BankCardType.STORE_SCAN_CODE_BALANCE.getCode() + "", "斗拍扫码");
        Map<String, String> status = new HashMap<String, String>();
        status.put(RechargeType.SUCCESS.getCode() + "", RechargeType.SUCCESS.getMsg());
        status.put(RechargeType.PENDING.getCode() + "", RechargeType.PENDING.getMsg());
        status.put(RechargeType.CHECKED.getCode() + "", RechargeType.CHECKED.getMsg());
        status.put(RechargeType.TRANSFER_SUCCESS.getCode() + "", RechargeType.TRANSFER_SUCCESS.getMsg());
        Map<String, String> storemap = new HashMap<String, String>();
        storemap.put("storeName", store.getStoreName());
        storemap.put("storeIcon", store.getIcon());
        for (WalletRecharge walletRecharge : walletRe) {
            WalletRechargeVo vo = new WalletRechargeVo();
            vo.setId(walletRecharge.getId());
            vo.setOrderNo(walletRecharge.getOrderNo());
            vo.setUserId(walletRecharge.getUserId());
            vo.setScore(walletRecharge.getScore());
            vo.setPayment(walletRecharge.getConfirmScore());
            vo.setConfirmScore(walletRecharge.getConfirmScore() - walletRecharge.getPoundage());
            vo.setSource(null == source.get(walletRecharge.getSource() + "") ? "其他支付" : source.get(walletRecharge.getSource() + ""));
            vo.setStatus(status.get(walletRecharge.getStatus() + ""));
            vo.setStoreUserId(walletRecharge.getStoreUserId());
            vo.setBusinessSendEp(walletRecharge.getBusinessSendEp());
            vo.setFirstReferrerScale(walletRecharge.getFirstReferrerScale());
            vo.setSecondReferrerScale(walletRecharge.getSecondReferrerScale());
            vo.setThirdReferrerScale(walletRecharge.getThirdReferrerScale());
            vo.setCreateTime(walletRecharge.getCreateTime());
            vo.setStoreInfo(storemap);
            vo.setDiscountEP(walletRecharge.getDiscountEP() * 100);
            result.add(vo);
        }
        return result;
    }


    /**
     * 查询是否到账:支付宝支付、微信支付
     *
     * @param request
     * @param orderNo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/isPaySuccess", method = RequestMethod.GET)
    public JsonResult callScan(HttpServletRequest request, String orderNo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(ResultCode.ERROR.getCode(), "查询用户信息错误");
        }
        JsonResult result = null;
        String uri = request.getRequestURI();
        if (org.apache.commons.lang3.StringUtils.isEmpty(orderNo)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "订单号不能为空");
        }
        WalletRecharge model = walletRechargeService.getUserOrderByOrderNo(orderNo);
        if (null == model) {
            return new JsonResult(ResultCode.ERROR.getCode(), "订单不存在");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(model.getStoreUserId())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "此订单不是扫码支付订单，接口调用错误");
        }

        if (model.getStatus() != RechargeType.PENDING.getCode()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "订单支付微信支付宝已经回调成功");
        }

        //判断是否是支付宝网页扫码支付
        if (model.getSource() != BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode()) {
            if (org.apache.commons.lang3.StringUtils.isEmpty(model.getUserId())) {
                return new JsonResult(ResultCode.ERROR.getCode(), "用户id不能为空");
            }
            user = userService.getById(model.getUserId());
            if (user == null) {
                return new JsonResult(ResultCode.ERROR.getCode(), "用户不存在");
            }
        }

        Boolean isSuccess = false;
        //判断微信扫码支付方式：微信app内扫码支付，微信商家固定二维码app内扫码支付，微信商家固定二维码微信客户端直接发起扫码支付,加入合伙人
        if (model.getSource() == BankCardType.SCAN_CODE_WEIXIN.getCode().intValue() || model.getSource() == BankCardType.STORE_SCAN_APP_WEIXIN.getCode().intValue() || model.getSource() == BankCardType.STORE_SCAN_PAGE_WEIXIN.getCode().intValue() || model.getSource() == BankCardType.JOIN_WEIXIN.getCode().intValue()) {
            isSuccess = WechatUtil.isAppPaySucess(orderNo);
            if (!isSuccess) {
                return new JsonResult(ResultCode.ERROR.getCode(), "微信扫码订单未支付");
            }
        }
        //判断支付宝是否是扫码支付，商家固定扫码app支付，商家固定扫码支付宝支付,加入合伙人
        if (model.getSource() == BankCardType.SCAN_CODE_ALIPAY.getCode().intValue() || model.getSource() == BankCardType.STORE_SCAN_APP_ALIPAY.getCode().intValue() || model.getSource() == BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode().intValue() || model.getSource() == BankCardType.JOIN_ALIPAY.getCode().intValue()) {
//            isSuccess = WechatUtil.isAppScanAliPaySucess(orderNo);
            isSuccess = AliPayUtils.isSZAliPaySucess(orderNo);
            if (!isSuccess) {
                return new JsonResult(ResultCode.ERROR.getCode(), "支付宝扫码订单未支付");
            }
        }
        return new JsonResult(ResultCode.SUCCESS.getCode(), "扫码订单支付成功");
    }


    /**
     * 积分兑换/提现
     */
    @ResponseBody
    @RequestMapping(value = "/v42/exchange", method = RequestMethod.POST)
    public JsonResult v42Exchange(HttpServletRequest request, @RequestBody WalletV42Vo vo) throws Exception {
        logger.error("000000000000000000 /v42/exchagne:" + JSON.toJSONString(vo));
        Token token = TokenUtil.getSessionUser(request);
        // 检查后台是否开启兑换开关
        ParamUtil util = ParamUtil.getIstance();
        if (ToolUtil.parseInt(util.get(Parameter.EXCHANGESWITCH), 0) != StatusType.TRUE.getCode()) {
            return new JsonResult(0, "兑换功能暂未开启");
        }


        if (vo.getSource() == null) {
            return new JsonResult(0, "提现请下载最新app");
        }
        if (vo.getSource() != 1 && vo.getSource() != 2 && vo.getSource() != 3) {
            return new JsonResult(0, "请选择合法提现方式");
        }

        double exchangeMin = ToolUtil.parseDouble(util.get(Parameter.EXCHANGEMIN), 100d);
        double exchangeMax = ToolUtil.parseDouble(util.get(Parameter.EXCHANGEMAX), 10000d);
        if (vo.getScore() == null || vo.getScore() < exchangeMin || vo.getScore() > exchangeMax) {
            return new JsonResult(1, "单笔兑换余额必须在[" + exchangeMin + "," + exchangeMax + "]之间");
        }
        if (vo.getSource() == 1 || vo.getSource() == 2) {
            //银行
            if (!ToolUtil.is100Mutiple(vo.getScore())) {
                return new JsonResult(1, "单笔兑换余额必须是100整数倍");
            }
            if (ToolUtil.isEmpty(vo.getBankId())) {
                return new JsonResult(2, "请选择银行卡类型");
            }
            if (ToolUtil.isEmpty(vo.getBankName())) {
                return new JsonResult(3, "请填写开户银行");
            }
            if (ToolUtil.isEmpty(vo.getBankId())) {
                return new JsonResult(4, "请选择银行卡类型");
            }
            if (ToolUtil.isEmpty(vo.getCardNo())) {
                return new JsonResult(5, "请填写银行卡号");
            }
            if (ToolUtil.isEmpty(vo.getUserName())) {
                return new JsonResult(6, "请填写真实姓名");
            }
            if (StringUtils.isBlank(vo.getPayPwd())) {
                return new JsonResult(8, "支付密码不能为空");
            }
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getPayPwd())) {
            return new JsonResult(3, "请先设置支付密码");
        }

        if (user.getScore() == null || user.getScore() < vo.getScore()) {
            return new JsonResult(5, "您的余额不足");
        }
        Integer times = ToolUtil.parseInt(util.get(Parameter.EXCHANGTIMES), 0);
        Integer hasExchange = walletExchangeService.countCurrentDay(user.getId());
        if (hasExchange != null && hasExchange >= times) {
            return new JsonResult(0, "每天最多提现" + times + "笔");
        }
        if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
            return new JsonResult(4, "支付密码不正确");
        }
        if (vo.getSource() == 1 || vo.getSource() == 2) {

            List<UserBankcard> bankList = userBankcardService.getList(user.getId());
            UserBankcard bankcard = new UserBankcard();
            if (CollectionUtils.isEmpty(bankList)) {
                bankcard.setRemark(user.getUserName());
                bankcard.setUserId(user.getId());
                bankcard.setBankId(vo.getBankId());
                bankcard.setBankName(vo.getBankName());
                bankcard.setCardNo(vo.getCardNo());
                bankcard.setType(0);
                userBankcardService.add(bankcard);
            } else {
                bankcard = new UserBankcard();
                bankcard.setId(bankList.get(0).getId());
                bankcard.setType(0);
                bankcard.setBankName(vo.getBankName());
                bankcard.setBankId(vo.getBankId());
                bankcard.setCardNo(vo.getCardNo());
                bankcard.setUserId(user.getId());
                bankcard.setRemark(user.getUserName());
                userBankcardService.update(bankcard.getId(), bankcard);
            }
            User updateUser = new User();
            updateUser.setUserName(vo.getUserName());
            userService.update(user.getId(), updateUser);
            walletExchangeService.exchangeHandler(user, vo.getScore(), bankcard);
        }
        if (vo.getSource() == 3) {
            if (ToolUtil.isEmpty(user.getAppOpenId())) {
                return new JsonResult(4, "未绑定微信号");
            }
            if (ToolUtil.isEmpty(user.getUserName()) && ToolUtil.isEmpty(vo.getUserName())) {
                return new JsonResult(4, "微信提现必须填写实名");
            }
            if (ToolUtil.isEmpty(user.getUserName()) && ToolUtil.isNotEmpty(vo.getUserName())){
                User updateUser = new User();
                updateUser.setUserName(vo.getUserName());
                userService.update(user.getId(), updateUser);
            }
//            Integer isCheck=com.yanbao.util.ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.ISWXCHECKUSERNAME),1);
            walletExchangeService.exchangeHandlerForWeiXin(user, vo.getScore());
        }
        // 操作成功返回用户当前积分
        Map<String, Object> result = new HashMap<String, Object>();
        user = userService.getById(token.getId());
        result.put("score", FormatUtils.formatDouble(user.getScore()));
        //邮箱推送
        emailService.sendEmail("斗拍推送", "系统有一条新的提现消息，请注意查看", util.get(Parameter.MAILPEOPLE));
        return new JsonResult(result);
    }


    /**
     * EP赠送/转账
     */
    @ResponseBody
    @RequestMapping(value = "/donateEP", method = RequestMethod.POST)
    public JsonResult donate1(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        // 检查后台是否开启赠送开关
        ParamUtil util = ParamUtil.getIstance();
        if (ToolUtil.parseInt(util.get(Parameter.DONATEEPSWICH), 0) != StatusType.TRUE.getCode()) {
            return new JsonResult(0, "赠送EP功能暂未开启");
        }
        Token token = TokenUtil.getSessionUser(request);
        if (StringUtils.isBlank(vo.getDonateTo()) || vo.getEp() == null) {
            return new JsonResult(1, "参数异常");
        }
        //判断赠送Ep是否是100的整数倍
        if (!ToolUtil.is100Mutiple(vo.getEp())) {
            return new JsonResult(9, "赠送EP必须是100的整数倍");
        }
        Double donateMax = ToolUtil.parseDouble(util.get(Parameter.DONATEEPMAX), 10000d);
        Double donateMin = ToolUtil.parseDouble(util.get(Parameter.DONATEEPMIN), 100d);
        if (vo.getEp() < donateMin || vo.getEp() > donateMax) {
            return new JsonResult(2, "赠送EP必须在[" + donateMin + "," + donateMax + "]之间");
        }
        if (StringUtils.isBlank(vo.getPayPwd())) {
            return new JsonResult(3, "支付密码不能为空");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(user.getPayPwd())) {
            return new JsonResult(4, "请先设置支付密码");
        }
        if (user.getExchangeEP() == null || user.getExchangeEP() < vo.getEp()) {
            return new JsonResult(6, "您的ep不足");
        }
        User condition = new User();
        if (vo.getDonateTo().length() < 11) { // UID
            condition.setUid(Integer.parseInt(vo.getDonateTo()));
        } else { // 手机号
            condition.setPhone(vo.getDonateTo());
        }
        User donateUser = userService.getByCondition(condition);
        if (null == donateUser) {
            return new JsonResult(7, "赠送用户不存在");
        }
        if (user.getId().equals(donateUser.getId())) {
            return new JsonResult(8, "不能赠送给自己");
        }
        //处理ep赠送业务
        walletDonateService.donateEpHandler(user, donateUser, vo.getEp());
        // 操作成功返回用户当前积分
        Map<String, Object> result = new HashMap<String, Object>();
        user = userService.getById(token.getId());
        result.put("ep", FormatUtils.formatDouble(user.getExchangeEP()));
        return new JsonResult(result);
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
