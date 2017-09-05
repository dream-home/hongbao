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
import com.yanbao.util.h5.GenerateH5Order;
import com.yanbao.util.h5.IWxAction;
import com.yanbao.util.h5.WxH5CallBack;
import com.yanbao.util.wechatpay.GenerateOrder;
import com.yanbao.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import java.util.*;

import static com.yanbao.util.PoundageUtil.getPoundage;

/**
 * Created by Administrator on 2017/5/3.
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private UserService userService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private OrderService orderService;
    @Value("${purchase_weixin_notifyUrl}")
    private String purchase_weixin_notifyUrl;
    @Value("${purchase_wxH5_store_notifyUrl}")
    private String purchase_wxH5_store_notifyUrl;
    //    @Value("${purchase_alipay_notifyUrl}")
//    private String purchaseAlipayNotifyUrl;
    @Value("${environment}")
    private String environment;
    @Autowired
    private GoodsWinDetailService goodsWinDetailService;
    @Autowired
    private GoodsIssueService goodsIssueService;
    @Autowired
    private PayCallbackService payCallbackService;

    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private SecondCallBackService secondCallBackService;

    /**
     * 订单直接购买
     */
    @ResponseBody
    @RequestMapping(value = "/appPurchase", method = RequestMethod.POST)
    public JsonResult appPurchase(HttpServletRequest request, @RequestBody OrderPurchaseVo orderPurchaseVo) throws Exception {
        String  tokens = TokenUtil.getToken(request);
        Token token = TokenUtil.getSessionUser(request);
        if (orderPurchaseVo.getCartList() == null || orderPurchaseVo.getCartList().size() <= 0 || StringUtils.isBlank(orderPurchaseVo.getCartList().get(0).getGoodsId())) {
            return new JsonResult(1, "请选择商品");
        }
        //支付来源
        int source = orderPurchaseVo.getPayType().intValue();
        //支付来源是否合法有效
        boolean isVaildPay = source == BankCardType.ALIPAY.getCode().intValue() || source == BankCardType.WECHATPAY.getCode().intValue() || source == BankCardType.BALANCE.getCode().intValue();
        if (!isVaildPay) {
            return new JsonResult(1, "目前仅支持支付宝、微信支付、余额付款");
        }
        if (StringUtils.isBlank(orderPurchaseVo.getUserName()) || StringUtils.isBlank(orderPurchaseVo.getPhone()) || StringUtils.isBlank(orderPurchaseVo.getAddr())) {
            return new JsonResult(6, "请完善收货信息");
        }
        final User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }

        Goods goods = null;
        Store store = null;
        List<Goods> goodsList = new ArrayList<>();
        int length = orderPurchaseVo.getCartList().size();
        for (int i = 0; i < length; i++) {
            goods = goodsService.getById(orderPurchaseVo.getCartList().get(i).getGoodsId());
            if (goods == null || StatusType.FALSE.getCode().intValue() == goods.getStatus().intValue()) {
                return new JsonResult(2, goods.getName() + "已下架");
            }
            //购买数量不传默认为1
            if (orderPurchaseVo.getCartList().get(i).getNum() == null || orderPurchaseVo.getCartList().get(i).getNum() <= 0) {
                orderPurchaseVo.getCartList().get(i).setNum(1);
            }
            int num = orderPurchaseVo.getCartList().get(i).getNum();
            if (goods.getStock() < num) {
                return new JsonResult(3, goods.getName() + "库存不足");
            }
            /*获取商家信息*/
            if (i == 0) {
                store = storeService.getById(goods.getStoreId());
                if (null == store || StoreType.FINISH.getCode() != store.getStatus()) {
                    return new JsonResult(4, "商铺已关闭，禁止购买");
                }
            }
            //判断商品折扣优惠是否为空，如果为空，默认为零
            if (orderPurchaseVo.getCartList().get(i).getDiscountEP() == null) {
                //默认设置为零
                orderPurchaseVo.getCartList().get(i).setDiscountEP(0d);
            }
            goodsList.add(goods);
        }

        /*余额支付时先进行一些基本判断*/
        if (source == BankCardType.BALANCE.getCode().intValue()) {
            if (StringUtils.isBlank(user.getPayPwd())) {
                return new JsonResult(4, "请先设置支付密码");
            }
            if (!user.getPayPwd().equals(Md5Util.MD5Encode(orderPurchaseVo.getPayPwd(), user.getSalt()))) {
                return new JsonResult(3, "支付密码不正确");
            }
        }

        //生成待付款的订单
        GoodsWin goodsWin = orderService.addOrderByApp(user, goodsList, orderPurchaseVo, store);
        //计算订单价格
        Double totalPrice = 0d;
        if (goodsWin.getOrderType().intValue() == OrderType.PURCHASE.getCode().intValue()) {
            //ep折扣优惠前的计算金钱
//                m = goodsWin.getPrice() * goodsWin.getNum() * 100 + "";
            //ep折扣优惠后的计算金钱
            totalPrice = goodsWin.getPrice() * goodsWin.getNum() - goodsWin.getDiscountEP();
        } else {
//                m = goodsWin.getPrice() * 100 + "";
            //ep折扣优惠后的计算金钱
            totalPrice = goodsWin.getPrice() - goodsWin.getDiscountEP();
        }
        totalPrice = getPoundage(totalPrice, 1d, 2);
        //返回参数给前端
        Map<Object, Object> map = new HashedMap();
        if (source == BankCardType.WECHATPAY.getCode().intValue()) {
            String ip = request.getRemoteAddr();
            String attach = token.getId() + "@" + BankCardType.PURCHASE_WEIXIN.getCode();
            String m = "";
            m = totalPrice * 100 + "";
            String money = m.substring(0, m.indexOf("."));
            //场景：APP余额充值
            if (ToolUtil.isEmpty(orderPurchaseVo.getScenes()) || orderPurchaseVo.getScenes().intValue() != ScenesType.WEIXIN_STORE.getCode().intValue()) {
                //微信支付
                GenerateOrder generateOrder = new GenerateOrder();
                Map<String, String> wxMap = generateOrder.generate(money, ip, attach, goodsWin.getOrderNo(), purchase_weixin_notifyUrl);
                map.put("appid", wxMap.get("appid"));
                map.put("partnerid", wxMap.get("partnerid"));
                map.put("noncestr", wxMap.get("noncestr"));
                map.put("timestamp", wxMap.get("timestamp"));
                map.put("package", wxMap.get("package"));
                map.put("prepayid", wxMap.get("prepayid"));
                map.put("sign", wxMap.get("sign"));
                //场景：微店余额充值
            } else if (orderPurchaseVo.getScenes().intValue() == ScenesType.WEIXIN_STORE.getCode().intValue()) {
                GenerateH5Order order = new GenerateH5Order();
                Map<String, String> wxMap = order.generate(money, ip, attach, goodsWin.getOrderNo(), user.getOpenId(), purchase_wxH5_store_notifyUrl);
                map.put("appid", wxMap.get("appId"));
                map.put("noncestr", wxMap.get("nonceStr"));
                map.put("timestamp", wxMap.get("timeStamp"));
                map.put("package", wxMap.get("package"));
                map.put("prepayid", wxMap.get("prepayid"));
                map.put("sign", wxMap.get("paySign"));

            }
        } else if (source == BankCardType.ALIPAY.getCode().intValue()) {
            String notifyUrl="";
            SecondCallBack secondCallBack =  secondCallBackService.getById(BankCardType.PURCHASE_ALIPAY.getCode()+"");
            if (secondCallBack==null || ToolUtil.isEmpty(secondCallBack.getReturnUrl())){
                return new JsonResult(-1,"支付回调参数设置不合法");
            }
            if ("test".equals(environment)){
                notifyUrl=secondCallBack.getTestReturnUrl();
            }else if ("online".equals(environment)){
                notifyUrl=secondCallBack.getReturnUrl();
            }
            orderTypeService.add(goodsWin.getOrderNo(),BankCardType.PURCHASE_ALIPAY.getCode(),"支付宝订单购买原生支付",tokens);
            String orderInfo = AliPayUtils.alipayPreOrderForApp(goodsWin.getOrderNo(),notifyUrl,totalPrice,"斗拍商城支付");
            map.put("orderInfo", orderInfo);
            //支付宝支付
            map.put("orderNo", goodsWin.getOrderNo());
            map.put("payTime", goodsWin.getCreateTime());
            map.put("userId", goodsWin.getUserId());
            map.put("returnUrl", environment);
            map.put("orderTitle", "购买商品");
            map.put("tranType", 30);
        } else if (source == BankCardType.BALANCE.getCode().intValue()) {
            //余额支付
            Double score = 0d;
            if (goodsWin.getOrderType().intValue() == OrderType.PURCHASE.getCode().intValue()) {
                //计算金额
                score = goodsWin.getPrice() * goodsWin.getNum() - goodsWin.getDiscountEP();
            } else if (goodsWin.getOrderType().intValue() == OrderType.CARTPAY.getCode().intValue()) {
                score = goodsWin.getPrice() - goodsWin.getDiscountEP();
            }
            if (user.getScore() == null || user.getScore() < score) {
                return new JsonResult(5, "您的积分不足");
            }
            if (goodsWin.getDiscountEP() > 0 && (user.getExchangeEP() == null || user.getExchangeEP() < goodsWin.getDiscountEP())) {
                return new JsonResult(6, "您的ep不足");
            }
            //处理业务
            orderService.purchaseHandlerByApp(user, goodsWin.getOrderNo());
        }
        map.put("orderNo", goodsWin.getOrderNo());
        map.put("time", goodsWin.getCreateTime().getTime());
        return new JsonResult(map);
    }


    /**
     * 待付款订单直接购买
     */
    @ResponseBody
    @RequestMapping(value = "/purchasing", method = RequestMethod.POST)
    public JsonResult purchasing(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        String tokens = TokenUtil.getToken(request);
        Token token = TokenUtil.getSessionUser(request);
        //支付来源
        Integer source = vo.getSource();
        if (source == null) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "支付方式不支付");
        }
        //支付来源是否合法有效
        boolean isVaildPay = source == BankCardType.ALIPAY.getCode().intValue() || source == BankCardType.WECHATPAY.getCode().intValue() || source == BankCardType.BALANCE.getCode().intValue();
        if (!isVaildPay) {
            return new JsonResult(1, "目前仅支持支付宝、微信支付、余额付款");
        }

        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "orderNo不能为空");
        }
        String orderNo = vo.getOrderNo();
        GoodsWin goodsWin = goodsWinService.getByOrderNo(orderNo, user.getId());
        if (goodsWin == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "订单不存在");
        }
        Date createTime = goodsWin.getCreateTime();

        if (createTime != null) {
            Long createTimeStamp = createTime.getTime();
            if ((System.currentTimeMillis() - createTimeStamp) > 2 * 60 * 60 * 1000) {
                return new JsonResult(ResultCode.ERROR.getCode(), "订单已失效");
            }
        }
        if (goodsWin.getStatus().intValue() != GoodsWinType.PENDING.getCode()) {
            logger.error(goodsWin.getOrderNo() + "：订单状态不是待付款，不需要处理");
            return new JsonResult(ResultCode.SUCCESS.getCode(), "订单已付款");
        }
        Goods goods = null;
        List<CartVo> cartVoList = new ArrayList<>();
        CartVo cartVo = null;
        if (goodsWin.getOrderType().intValue() == OrderType.CARTPAY.getCode().intValue()) {
            List<GoodsWinDetail> list = goodsWinDetailService.getListByGoodsWinId(goodsWin.getId());
            for (GoodsWinDetail goodsWinDetail : list) {
                goods = goodsService.getById(goodsWinDetail.getGoodsId());
                if (goods == null || StatusType.FALSE.getCode() == goods.getStatus()) {
                    return new JsonResult(2, goods.getName() + "已下架,请重新下订单");
                }
                int num = goodsWinDetail.getNum();
                if (goods.getStock() < num) {
                    return new JsonResult(3, goods.getName() + "库存不足,请重新下订单");
                }
                //创建变量，重新计算金额
                cartVo = new CartVo();
                cartVo.setGoodsId(goods.getId());
                cartVo.setNum(num);
                cartVo.setDiscountEP(0d);
                if (vo.getDiscountEP() > 0) {
                    cartVo.setDiscountEP(1d);
                }
                cartVoList.add(cartVo);
            }
        } else {
            goods = goodsService.getById(goodsWin.getGoodsId());
            if (goods == null || StatusType.FALSE.getCode() == goods.getStatus()) {
                return new JsonResult(2, goods.getName() + "已下架,请重新下订单");
            }
            if (goods.getStock() < goodsWin.getNum()) {
                return new JsonResult(3, goods.getName() + "库存不足,请重新下订单");
            }
            //创建变量，重新计算金额
            cartVo = new CartVo();
            cartVo.setGoodsId(goods.getId());
            cartVo.setNum(goodsWin.getNum());
            cartVo.setDiscountEP(0d);
            if (vo.getDiscountEP() > 0) {
                cartVo.setDiscountEP(1d);
            }
            cartVoList.add(cartVo);
        }
         /*获取商家信息*/
        Store store = storeService.getById(goodsWin.getStoreId());
        if (null == store || StoreType.FINISH.getCode() != store.getStatus()) {
            return new JsonResult(4, "商铺已关闭，禁止购买");
        }

        //返回参数给前端
        Map<Object, Object> map = new HashedMap();
        GoodsWin model = new GoodsWin();
        model.setId(goodsWin.getId());
        model.setOrderNo(OrderNoUtil.get());
        model.setUpdateTime(new Date());
        model.setScenes(vo.getScenes());//场景
        if (source == BankCardType.ALIPAY.getCode().intValue()) {
            model.setPayWay(BankCardType.PURCHASE_ALIPAY.getCode().intValue());//支付宝直接购买支付
        } else if (source == BankCardType.WECHATPAY.getCode().intValue()) {
            model.setPayWay(BankCardType.PURCHASE_WEIXIN.getCode().intValue());//微信直接购买支付
        } else if (source == BankCardType.BALANCE.getCode().intValue()) {
            model.setPayWay(BankCardType.PURCHASE_BALANCE.getCode().intValue());//余额直接购买支付
        } else {

        }
        //直接购买的订单，要修正表头的三级分销 注释掉取用最新价格的分销数据
        /*if(goodsWin.getOrderType().intValue() == OrderType.PURCHASE.getCode().intValue()){
            goods = goodsService.getById(goodsWin.getGoodsId());
            model.setBusinessSendEp(goods.getBusinessSendEp());
            model.setFirstReferrerScale(goods.getFirstReferrerScale());
            model.setSecondReferrerScale(goods.getSecondReferrerScale());
            model.setThirdReferrerScale(goods.getThirdReferrerScale());
        }*/
        goodsWinService.update(model.getId(), model);
        goodsWinDetailService.updateOrderNoByWinId(model.getOrderNo(), goodsWin.getId());
        //获取待付订单数据后，重新计算该订单实付价格
        Double realPayPrice = 0d;
        goodsWin.setOrderNo(model.getOrderNo());
        goodsWin.setPayWay(model.getPayWay());
        //定义实付金额
        Map<String, Double> countMap = orderService.calcMoney(vo.getDiscountEP() > 0, user, goodsWin.getId());
        realPayPrice = countMap.get("realPayPrice");
        //处理订单主表和明细表数据
        orderService.purchasingUpdate(cartVoList, user, goodsWin, countMap);
        if (goodsWin.getPayWay().intValue() == BankCardType.PURCHASE_WEIXIN.getCode().intValue()) {
            //微信支付
            String ip = request.getRemoteAddr();
            String attach = token.getId() + "@" + BankCardType.PURCHASE_WEIXIN.getCode();
            realPayPrice = PoundageUtil.getPoundage(realPayPrice, 1d, 2);
            String m = "";
            m = realPayPrice * 100 + "";
            String money = m.substring(0, m.indexOf("."));
            GenerateOrder generateOrder = new GenerateOrder();
            if (ToolUtil.isEmpty(vo.getScenes()) || vo.getScenes().intValue() != ScenesType.WEIXIN_STORE.getCode().intValue()) {
                Map<String, String> wxMap = generateOrder.generate(money, ip, attach, goodsWin.getOrderNo(), purchase_weixin_notifyUrl);
                map.put("appid", wxMap.get("appid"));
                map.put("partnerid", wxMap.get("partnerid"));
                map.put("noncestr", wxMap.get("noncestr"));
                map.put("timestamp", wxMap.get("timestamp"));
                map.put("package", wxMap.get("package"));
                map.put("prepayid", wxMap.get("prepayid"));
                map.put("sign", wxMap.get("sign"));
            } else if (vo.getScenes().intValue() == ScenesType.WEIXIN_STORE.getCode().intValue()) {
                GenerateH5Order order = new GenerateH5Order();
                Map<String, String> wxMap = order.generate(money, ip, attach, goodsWin.getOrderNo(), user.getOpenId(), purchase_wxH5_store_notifyUrl);
                map.put("appid", wxMap.get("appId"));
                map.put("noncestr", wxMap.get("nonceStr"));
                map.put("timestamp", wxMap.get("timeStamp"));
                map.put("package", wxMap.get("package"));
                map.put("prepayid", wxMap.get("prepayid"));
                map.put("sign", wxMap.get("paySign"));
            }

        } else if (goodsWin.getPayWay().intValue() == BankCardType.PURCHASE_ALIPAY.getCode().intValue()) {


            //原生支付
            String notifyUrl = "";
            SecondCallBack secondCallBack = secondCallBackService.getById(BankCardType.PURCHASE_ALIPAY.getCode() + "");
            if (secondCallBack == null || ToolUtil.isEmpty(secondCallBack.getReturnUrl())) {
                return new JsonResult(-1, "支付回调参数设置不合法");
            }
            if ("test".equals(environment)) {
                notifyUrl = secondCallBack.getTestReturnUrl();
            } else if ("online".equals(environment)) {
                notifyUrl = secondCallBack.getReturnUrl();
            }
            orderTypeService.add(model.getOrderNo(), BankCardType.PURCHASE_ALIPAY.getCode(), "支付宝待付订单购买原生支付",tokens);
            String orderInfo = AliPayUtils.alipayPreOrderForApp(model.getOrderNo(), notifyUrl,realPayPrice, "斗拍商城支付");
            map.put("orderInfo", orderInfo);


            //支付宝支付
            map.put("orderNo", goodsWin.getOrderNo());
            map.put("payTime", goodsWin.getCreateTime());
            map.put("userId", goodsWin.getUserId());
            map.put("returnUrl", environment);
        } else if (goodsWin.getPayWay().intValue() == BankCardType.PURCHASE_BALANCE.getCode().intValue()) {
            if (StringUtils.isBlank(user.getPayPwd())) {
                return new JsonResult(4, "请先设置支付密码");
            }
            if (!user.getPayPwd().equals(Md5Util.MD5Encode(vo.getPayPwd(), user.getSalt()))) {
                return new JsonResult(3, "支付密码不正确");
            }
            //余额支付
            Double score = realPayPrice;
//            if (goodsWin.getOrderType().intValue() == OrderType.PURCHASE.getCode().intValue()) {
//                //计算金额
//                score = goodsWin.getPrice() * goodsWin.getNum() - goodsWin.getDiscountEP();
//            } else if(goodsWin.getOrderType().intValue() == OrderType.CARTPAY.getCode().intValue()){
//                score = goodsWin.getPrice() - goodsWin.getDiscountEP();
//            }
            if (user.getScore() == null || user.getScore() < score) {
                return new JsonResult(5, "您的积分不足");
            }
            //处理业务
            orderService.purchaseHandlerByApp(user, goodsWin.getOrderNo());
        }
        map.put("orderNo", goodsWin.getOrderNo());
        map.put("time", goodsWin.getCreateTime().getTime());
        return new JsonResult(map);
    }

    /***
     *获取待付订单数据后，重新计算该订单实付价格
     * @param user
     * @param goodsWin
     * @param flag
     * @param cartVoList
     * @return
     * @throws Exception
     */
    private Double calculateRealPrice(User user, GoodsWin goodsWin, Boolean flag, List<CartVo> cartVoList) throws Exception {
        //定义实付金额
        Double realPayPrice = 0d;

        //重新计算金额
        Map<String, Double> countMap = orderService.countMoney(cartVoList, user, goodsWin.getId());
        //定义总价格
        Double originalPrice = countMap.get("originalPrice");
        //定义ep折扣
        Double totalDiscountEP = countMap.get("totalDiscountEP");
        //计算实付商品价格
        realPayPrice = countMap.get("realPayPrice");
        realPayPrice = getPoundage(realPayPrice, 1d);
        //返回实付价格
        return realPayPrice;
    }


    /**
     * 支付宝直接购买回调
     */
    @ResponseBody
    @RequestMapping(value = "/alipayCallback", method = RequestMethod.POST)
    public JsonResult alipayCallback(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (vo.getSource() != BankCardType.ALIPAY.getCode() && vo.getSource() != BankCardType.PURCHASE_ALIPAY.getCode()) {
            return new JsonResult(1, "目前仅支持支付宝、微信支付付款");
        }
        String key = RedisKey.WEIXIN_CALLBACK.getKey() + vo.getOrderNo();
        String value = Strings.get(key);
        User user = userService.getById(token.getId());
        GoodsWin goodsWin = goodsWinService.getByOrderNo(vo.getOrderNo(), user.getId());
        if (goodsWin == null) {
            return new JsonResult(2, "订单不存在");
        }
        boolean flag1 = goodsWin == null || goodsWin.getStatus() != GoodsWinType.PENDING.getCode().intValue() || !StringUtils.isEmpty(value) || vo.getSource() != BankCardType.PURCHASE_ALIPAY.getCode().intValue();
        if (flag1) {
            return new JsonResult(3, "回调失败");
        } else {
            Boolean flag = RedisLock.redisLock(key, vo.getOrderNo(), 6);
            if (flag) {
                orderService.purchaseHandlerByApp(user, vo.getOrderNo());
            }
        }
        return new JsonResult();
    }

    /**
     * 微信直接购买支付回调
     */
    @ResponseBody
    @RequestMapping(value = "/wxCallback", method = RequestMethod.POST)
    public void wxCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resXml = HttpsUtil.getXmlFromRequest(request);
        logger.error("***********call***********");
        logger.error(resXml);
        logger.error("***********wxCallback***********");
        if (StringUtils.isEmpty(resXml)) {
            logger.info("微信返回XML数据为空");
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";
        } else {
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
        String signStr = generateOrder.createSignWithObjectKV(packageParams);
        String sign = (String) packageParams.get("sign");
        String attach = (String) packageParams.get("attach");
        if (StringUtils.isEmpty(sign) || !signStr.equals(sign)) {
            logger.info("支付失败,错误信息,验签失败");
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";
        } else {
            String userId = "";
            int payType = 0;
            if (!StringUtils.isEmpty(attach)) {
                if (attach.split("@").length > 1) {
                    payType = Integer.valueOf(attach.split("@")[1]);
                }
                userId = attach.split("@")[0];
            }
            String orderNo = (String) packageParams.get("out_trade_no");
            Double fee = Double.valueOf((String) packageParams.get("total_fee"));
            String key = RedisKey.WEIXIN_CALLBACK.getKey() + orderNo;
            String value = Strings.get(key);
            logger.error(JSON.toJSONString(packageParams));
            logger.error("****************************************");
            System.out.println("微信返回参数：" + JSON.toJSONString(packageParams));
            logger.error("****************************************");

            if ("SUCCESS".equals((String) packageParams.get("result_code")) && userId != null) {
                GoodsWin goodsWin = goodsWinService.getByOrderNo(orderNo, userId);
                if (fee == null || goodsWin == null || goodsWin.getStatus() != GoodsWinType.PENDING.getCode().intValue() || !StringUtils.isEmpty(value) || payType != BankCardType.PURCHASE_WEIXIN.getCode().intValue()) {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                } else {
                    User user = userService.getById(userId);
                    if (user != null) {
                        Boolean flag = RedisLock.redisLock(key, orderNo, 6);
                        if (flag) {
                            orderService.purchaseHandlerByApp(user, orderNo);
                            logger.error("支付成功");
                            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        }
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 微信直接购买支付回调-微店回调
     */
    @ResponseBody
    @RequestMapping(value = "/wxStoreH5Callback", method = RequestMethod.POST)
    public void wxStoreH5Callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WxH5CallBack.wxCallback(request, response, payCallbackService, new IWxAction() {
            @Override
            public String doAction(String orderNo, String userId, Double fee, int payType) throws Exception {
                GoodsWin goodsWin = goodsWinService.getByOrderNo(orderNo, userId);
                String key = RedisKey.WEIXIN_CALLBACK.getKey() + orderNo;
                String value = Strings.get(key);
                String resXml = "";
                if (fee == null || goodsWin == null || goodsWin.getStatus() != GoodsWinType.PENDING.getCode().intValue() || !StringUtils.isEmpty(value) || payType != BankCardType.PURCHASE_WEIXIN.getCode().intValue()) {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                } else {
                    User user = userService.getById(userId);
                    if (user != null) {
                        Boolean flag = RedisLock.redisLock(key, orderNo, 6);
                        if (flag) {
                            orderService.purchaseHandlerByApp(user, orderNo);
                            logger.error("支付成功");
                            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        }
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }
                logger.info(resXml);
                return JSON.toJSONString(goodsWin);
            }
        });
    }

    /**
     * 微信APP直接购买对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/wxapporder", method = RequestMethod.POST)
    public JsonResult wxapporder(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
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
            return new JsonResult(ResultCode.SUCCESS.getCode(), "微信直接购买订单已支付成功");
        }
        Boolean isSucess = false;
        if (goodsWin.getScenes().intValue() == ScenesType.WEIXIN_STORE.getCode().intValue()) {
            isSucess = WechatUtil.isH5PaySucess(vo.getOrderNo());
        } else {
            isSucess = WechatUtil.isAppPaySucess(vo.getOrderNo());
        }
        String key = RedisKey.HANDLE_CALLBACK.getKey() + vo.getOrderNo();
        Boolean flag = RedisLock.redisLock(key, vo.getOrderNo(), 6);
        if (flag && isSucess) {
            //处理业务
            orderService.purchaseHandlerByApp(user, vo.getOrderNo());
            return new JsonResult(ResultCode.SUCCESS.getCode(), "微信直接购买订单支付确认回调成功");
        }
        if (goodsWin.getStatus().intValue() == GoodsWinType.BUYED.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "微信直接购买订单微信已经回调成功");
        }
        logger.error("0000000000---> 微信直接购买支付失败");
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }

    /**
     * 支付宝APP直接购买对账查询
     */
    @ResponseBody
    @RequestMapping(value = "/alipayAppOrder", method = RequestMethod.POST)
    public JsonResult alipayAppOrder(HttpServletRequest request, @RequestBody WxH5OrderVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
        }
        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "orderNo不能为空");
        }
        String orderNo = vo.getOrderNo();
        GoodsWin goodsWin = goodsWinService.getByOrderNo(orderNo, user.getId());
        if (goodsWin == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "订单不存在");
        }
        if (goodsWin.getStatus().intValue() != GoodsWinType.PENDING.getCode()) {
            logger.error(goodsWin.getOrderNo() + "：订单状态不是待付款，不需要处理");
            return new JsonResult(ResultCode.SUCCESS.getCode(), "支付宝直接购买订单已支付成功");
        }

//        Boolean isSucess = WechatUtil.isAppScanAliPaySucess(vo.getOrderNo());
        Boolean isSucess = AliPayUtils.isSZAliPaySucess(orderNo);
        String key = RedisKey.HANDLE_CALLBACK.getKey() + vo.getOrderNo();
        Boolean flag = RedisLock.redisLock(key, vo.getOrderNo(), 6);

        if (flag && isSucess) {
            //处理业务
            orderService.purchaseHandlerByApp(user, vo.getOrderNo());
            return new JsonResult(ResultCode.SUCCESS.getCode(), "支付宝直接购买订单支付确认回调成功");
        }
        if (goodsWin.getStatus().intValue() == GoodsWinType.BUYED.getCode().intValue()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "支付宝直接购买订单微信已经回调成功");
        }

        logger.error("0000000000---> 支付宝直接购买支付失败");
        return new JsonResult(ResultCode.ERROR.getCode(), "支付失败");
    }


    /**
     * 订单数量
     */
    @ResponseBody
    @RequestMapping(value = "/orderNum", method = RequestMethod.GET)
    public JsonResult getorderNum(HttpServletRequest request, Integer status, String storeId) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        String userid = token.getId();
        if (!StringUtils.isEmpty(storeId)) {
            userid = null;
        }
        Integer count = goodsWinService.count(userid, status, null, storeId);
        Integer pending = goodsWinService.count(userid, 0, null, storeId);
        Integer payed = goodsWinService.count(userid, 2, null, storeId);
        Integer delivered = goodsWinService.count(userid, 3, null, storeId);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("orderNum", count);
        map.put("pending", pending);
        map.put("payed", payed);
        map.put("delivered", delivered);
        return new JsonResult(map);
    }


    /**
     * 我的发货订单列表
     */
    @ResponseBody
    @RequestMapping(value = "/orderlist", method = RequestMethod.GET)
    public JsonResult getOrderPage(HttpServletRequest request, Integer status, Page page, String storeId) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (status != null && GoodsWinType.PENDING.getCode() != status && GoodsWinType.BUYED.getCode() != status && GoodsWinType.DELIVERED.getCode() != status) {
            return new JsonResult(0, "请传入正常的状态");
        }
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        String userId = token.getId();
        if (!StringUtils.isEmpty(storeId)) {
            userId = null;
        }
        PageResult<GoodsWin> result = goodsWinService.getPage(userId, status, page, null, storeId);
        PageResult<GoodsWinVo> voPage = new PageResult<GoodsWinVo>();
        List<GoodsWinVo> voList = new ArrayList<>();
        for (GoodsWin goodsWin : result.getRows()) {
            GoodsWinVo vo = new GoodsWinVo();
            BeanUtils.copyProperties(vo, goodsWin);
            if (goodsWin.getOrderType().equals(OrderType.PURCHASE.getCode()) || goodsWin.getOrderType().equals(OrderType.CARTPAY.getCode())) {
                Goods goods = goodsService.getById(goodsWin.getGoodsId());
                if (goods != null && goods.getBusinessSendEp() != null) {
                    vo.setBusinessSendEp(goods.getBusinessSendEp());
                } else {
                    vo.setBusinessSendEp(0.0d);
                }
            } else {
                GoodsIssue goodsIssue = goodsIssueService.getById(goodsWin.getIssueId());
                if (goodsIssue != null && goodsIssue.getBusinessSendEp() != null) {
                    vo.setBusinessSendEp(goodsIssue.getBusinessSendEp());
                } else {
                    vo.setBusinessSendEp(0.0d);
                }
            }
            voList.add(vo);
        }
        voPage.setRows(voList);
        voPage.setPageNo(result.getPageNo());
        voPage.setPageSize(result.getPageSize());
        voPage.setTotalSize(result.getTotalSize());
        return new JsonResult(voPage);
    }


    /**
     * 商家发货订单详情
     */
    @ResponseBody
    @RequestMapping(value = "/orderdetail", method = RequestMethod.GET)
    public JsonResult getOrderInfo(HttpServletRequest request, String orderNo, String storeId) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isEmpty(storeId)) {
            storeId = null;
        }
        GoodsWin goodsWin = goodsWinService.getStoreOrderByOrderNo(orderNo, storeId);
        if (goodsWin == null) {
            logger.error("订单号不存在 " + orderNo);
            return new JsonResult(1, "订单号不存在");
        }
        GoodsWinVo goodsWinVo = new GoodsWinVo();
        BeanUtils.copyProperties(goodsWinVo, goodsWin);
       /*if (ToolUtil.isNotEmpty(goodsWin.getGoodsId())){
           Goods goods= goodsService.getById(goodsWin.getGoodsId());
           if (goods==null){
               return  new JsonResult(1,"商品已下架");
           }
           goodsWinVo.setPrice(goods.getPrice());
       }*/
        List<CartGoodsVo> cartGoodsVoList = goodsWinVo.getCartList();
        if (goodsWin.getOrderType().intValue() == OrderType.CARTPAY.getCode().intValue()) {
            List<GoodsWinDetail> goodsWinDetailList = goodsWinDetailService.getListByGoodsWinId(goodsWin.getId());
            for (GoodsWinDetail detail : goodsWinDetailList) {
                CartGoodsVo cartGoodsVo = new CartGoodsVo();
//                Goods detailGoods= goodsService.getById(detail.getGoodsId());
                //if (detailGoods!=null){
                cartGoodsVo.setGoodsId(detail.getGoodsId());
                cartGoodsVo.setGoodsName(detail.getGoodsName());
                cartGoodsVo.setPrice(detail.getPrice());
                cartGoodsVo.setIcon(detail.getIcon());
                cartGoodsVo.setNum(detail.getNum());
                cartGoodsVoList.add(cartGoodsVo);
                // }
            }
        }
        Store store = storeService.getById(goodsWin.getStoreId());
        if (store != null) {
            goodsWinVo.setStoreIcon(store.getIcon());
        }
        return new JsonResult(goodsWinVo);
    }

    /**
     * 订单删除
     */
    @ResponseBody
    @RequestMapping(value = "/ordercancle", method = RequestMethod.GET)
    public JsonResult orderCancle(HttpServletRequest request, String orderNo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (StringUtils.isEmpty(orderNo)) {
            return new JsonResult("订单号不能为空");
        }
        GoodsWin goodsWin = goodsWinService.getByOrderNo(orderNo, token.getId());
        if (goodsWin == null) {
            logger.error("订单号不存在 " + orderNo);
            return new JsonResult("订单号不存在");
        }
        //兼容老数据
        if (goodsWin.getDiscountEP() == null) {
            goodsWin.setDiscountEP(0d);
        }
        //订单取消，返回ep值
        //判断是否有ep折扣优惠
        if (goodsWin.getDiscountEP() != 0) {
            //返回用户ep
            userService.updateEp(user.getId(), goodsWin.getDiscountEP());
        }
        //修改状态
        GoodsWin model = new GoodsWin();
        model.setId(goodsWin.getId());
        model.setStatus(GoodsWinType.DEL.getCode().intValue());
        goodsWinService.update(model.getId(), model);
        return new JsonResult("取消成功");
    }

    /**
     * 重新计算金额
     *
     * @param request
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/purchasingep", method = RequestMethod.POST)
    public JsonResult purchasingep(HttpServletRequest request, @RequestBody WalletVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.getById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }

        if (StringUtils.isEmpty(vo.getOrderNo())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "orderNo不能为空");
        }

        String orderNo = vo.getOrderNo();
        GoodsWin goodsWin = goodsWinService.getByOrderNo(orderNo, user.getId());
        if (goodsWin == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "订单不存在");
        }
        if (goodsWin.getStatus().intValue() != GoodsWinType.PENDING.getCode()) {
            logger.error(goodsWin.getOrderNo() + "：订单状态不是待付款，不需要处理");
            return new JsonResult(ResultCode.SUCCESS.getCode(), "订单已付款");
        }
        Goods goods = null;
        List<CartVo> cartVoList = new ArrayList<>();
        CartVo cartVo = null;
        if (goodsWin.getOrderType().intValue() == OrderType.CARTPAY.getCode().intValue()) {
            List<GoodsWinDetail> list = goodsWinDetailService.getListByGoodsWinId(goodsWin.getId());
            for (GoodsWinDetail goodsWinDetail : list) {
                goods = goodsService.getById(goodsWinDetail.getGoodsId());
                if (goods == null || StatusType.FALSE.getCode() == goods.getStatus()) {
                    return new JsonResult(2, goods.getName() + "已下架,请重新下订单");
                }
                int num = goodsWinDetail.getNum();
                if (goods.getStock() < num) {
                    return new JsonResult(3, goods.getName() + "库存不足,请重新下订单");
                }
                //创建变量，重新计算金额
                cartVo = new CartVo();
                cartVo.setGoodsId(goods.getId());
                cartVo.setNum(num);
                cartVo.setDiscountEP(1d);
                cartVoList.add(cartVo);
            }
        } else {
            goods = goodsService.getById(goodsWin.getGoodsId());
            if (goods == null || StatusType.FALSE.getCode() == goods.getStatus()) {
                return new JsonResult(2, goods.getName() + "已下架,请重新下订单");
            }
            if (goods.getStock() < goodsWin.getNum()) {
                return new JsonResult(3, goods.getName() + "库存不足,请重新下订单");
            }
            //创建变量，重新计算金额
            cartVo = new CartVo();
            cartVo.setGoodsId(goods.getId());
            cartVo.setNum(goodsWin.getNum());
            cartVo.setDiscountEP(1d);
            cartVoList.add(cartVo);
        }

        //重新计算金额
        Map<String, Double> countMap = orderService.countMoney(cartVoList, user, goodsWin.getId());

        return new JsonResult(countMap);
    }


    @ResponseBody
    @RequestMapping(value = "/dealordericon", method = RequestMethod.GET)
    public JsonResult dealicon(HttpServletRequest request, Page page, String bucket) throws Exception {
        //bucket=doupai-test-goods
        PageResult<GoodsWin> goodsPage = goodsWinService.getPage(null, 1000, page, null, null);
        int i = 0;
        int j = 0;
        int m = 0;
        int n = 0;
        int h = 0;
        List<GoodsWin> winlist = goodsPage.getRows();
        if (ToolUtil.isNotEmpty(winlist)) {
            m = winlist.size();
            for (GoodsWin goodsWin : winlist) {
                if (ToolUtil.isNotEmpty(goodsWin.getIcon()) && goodsWin.getIcon().length() > 2000) {
                    String fileName = UUIDUtil.getUUID();
                    Map<String, String> map = QiNiuUtil.put64image(bucket, goodsWin.getIcon(), fileName, 2000);
                    if (map.size() > 0) {
                        i++;
                        String icon = map.get("keyWithPrefix");
                        if (ToolUtil.isNotEmpty(icon)) {
                            GoodsWin updateModel = new GoodsWin();
                            updateModel.setId(goodsWin.getId());
                            updateModel.setIcon(icon);
                            goodsWinService.update(goodsWin.getId(), updateModel);
                            j++;
                        }
                    }
                }
                if (goodsWin.getOrderType().intValue() == OrderType.CARTPAY.getCode().intValue()) {
                    h++;
                    List<GoodsWinDetail> list = goodsWinDetailService.getListByGoodsWinId(goodsWin.getId());
                    for (GoodsWinDetail goodsWinDetail : list) {
                        n++;
                        if (ToolUtil.isNotEmpty(goodsWinDetail.getIcon()) && goodsWinDetail.getIcon().length() > 2000) {
                            String fileName = UUIDUtil.getUUID();
                            Map<String, String> map = QiNiuUtil.put64image(bucket, goodsWinDetail.getIcon(), fileName, 2000);
                            if (map.size() > 0) {
                                i++;
                                String icon = map.get("keyWithPrefix");
                                if (ToolUtil.isNotEmpty(icon)) {
                                    GoodsWinDetail updateModel = new GoodsWinDetail();
                                    updateModel.setId(goodsWinDetail.getId());
                                    updateModel.setGoodsWinId(goodsWinDetail.getGoodsWinId());
                                    updateModel.setGoodsId(goodsWinDetail.getGoodsId());
                                    updateModel.setIcon(icon);
                                    goodsWinDetailService.updateByGoodsId(goodsWinDetail.getGoodsId(), updateModel);
                                    j++;
                                }
                            }
                        }
                    }
                }
            }
        }
        String msg = "共计" + m + "个base64订单,其中" + h + "个购物车订单，购物车明细数据共" + n + "张图片，累计上传了" + i + "张图片，累计更新了" + j + "张图片";
        return new JsonResult(msg);
    }


    @ResponseBody
    @RequestMapping(value = "/dealgoodsicon", method = RequestMethod.GET)
    public JsonResult dealGoodsIcon(HttpServletRequest request, Page page, String bucket) throws Exception {
        //bucket=doupai-test-goods
        page.setTimeSort(1000);
        page.setPriceSort(1000);
        PageResult<Goods> goodsPage = goodsService.getPage("", page);
        int i = 0;
        int j = 0;
        int m = 0;
        List<Goods> goodslist = goodsPage.getRows();
        if (ToolUtil.isNotEmpty(goodslist)) {
            m = goodslist.size();
            for (Goods goods : goodslist) {
                if (ToolUtil.isNotEmpty(goods.getIcon()) && goods.getIcon().length() > 2000) {
                    String fileName = UUIDUtil.getUUID();
                    Map<String, String> map = QiNiuUtil.put64image(bucket, goods.getIcon(), fileName, 2000);
                    if (map.size() > 0) {
                        i++;
                        String icon = map.get("keyWithPrefix");
                        if (ToolUtil.isNotEmpty(icon)) {
                            Goods updateModel = new Goods();
                            updateModel.setId(goods.getId());
                            updateModel.setIcon(icon);
                            goodsService.update(goods.getId(), updateModel);
                            j++;
                        }
                    }
                }
            }
        }
        String msg = "共计" + m + "个base64商品, ，累计上传了" + i + "张图片，累计更新了" + j + "张图片";
        return new JsonResult(msg);
    }

    @ResponseBody
    @RequestMapping(value = "/storeDealicon", method = RequestMethod.GET)
    public JsonResult storeDealicon(HttpServletRequest request, Page page, String bucket) throws Exception {
        //bucket=doupai-test-goods
        PageResult<Store> storePage = storeService.getPage(page, "");
        int i = 0;
        int j = 0;
        List<Store> winlist = storePage.getRows();
        if (ToolUtil.isNotEmpty(winlist)) {
            for (Store store : winlist) {
                if (ToolUtil.isNotEmpty(store.getIcon()) && store.getIcon().length() > 2000 && store.getIcon().indexOf("data:image/jpeg;base64,") >= 0) {
                    String fileName = UUIDUtil.getUUID();
                    Map<String, String> map = QiNiuUtil.put64image(bucket, store.getIcon(), fileName, 2000);
                    if (map.size() > 0) {
                        i++;
                        String icon = map.get("keyWithPrefix");
                        if (ToolUtil.isNotEmpty(icon)) {
                            Store model = new Store();
                            model.setId(store.getId());
                            model.setIcon(icon);
                            storeService.update(model.getId(), model);
                            j++;
                        }
                    }
                }
            }
        }
        String msg = "累计上传了" + i + "张图片，累计更新了" + j + "张图片";
        return new JsonResult(msg);
    }

    public static void main(String[] args) {
        String m = "";
        m = 2.01 * 100 + "";//201
        System.out.println(m);
        System.out.println(Double.valueOf(m));
        double d = PoundageUtil.getPoundage(Double.valueOf(m).doubleValue(), 1d);
        String money = m.substring(0, m.indexOf("."));
        System.out.println(d);
        System.out.println(money);
        Date date = DateUtils.addHours(new Date(), -2);
        Long createTimeStamp = date.getTime();
        System.out.println(System.currentTimeMillis() - createTimeStamp);
    }
}
