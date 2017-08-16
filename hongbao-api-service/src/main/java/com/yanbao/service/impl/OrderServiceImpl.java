package com.yanbao.service.impl;

import com.mall.model.*;
import com.yanbao.constant.*;
import com.yanbao.core.model.JpushExtraModel;
import com.yanbao.service.*;
import com.yanbao.util.JPushUtil;
import com.yanbao.util.OrderNoUtil;
import com.yanbao.util.PoundageUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.vo.CartVo;
import com.yanbao.vo.OrderPurchaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/3.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private GoodsWinDetailService goodsWinDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletRecordService walletRecordService;
    @Autowired
    private MessageService messageService;


    @Override
    @Transactional
    public GoodsWin addOrderByApp(User user, List<Goods> goodsList, OrderPurchaseVo orderPurchaseVo,Store store) throws Exception {

        //定义商品订单变量
        GoodsWin goodsWin = new GoodsWin();
        goodsWin.setOrderNo(OrderNoUtil.get());
        goodsWin.setUserId(user.getId());
        goodsWin.setScenes(orderPurchaseVo.getScenes());//场景
        goodsWin.setStatus(GoodsWinType.PENDING.getCode());//待处理(待付款)
        if (orderPurchaseVo.getPayType().intValue()==BankCardType.ALIPAY.getCode().intValue()){
            goodsWin.setPayWay(BankCardType.PURCHASE_ALIPAY.getCode().intValue());//支付宝直接购买支付
        }else if(orderPurchaseVo.getPayType().intValue()==BankCardType.WECHATPAY.getCode().intValue()){
            goodsWin.setPayWay(BankCardType.PURCHASE_WEIXIN.getCode().intValue());//微信直接购买支付
        }else if(orderPurchaseVo.getPayType().intValue()==BankCardType.BALANCE.getCode().intValue()){
            goodsWin.setPayWay(BankCardType.PURCHASE_BALANCE.getCode().intValue());//余额直接购买支付
        }
        // 收货信息
        goodsWin.setUserName(orderPurchaseVo.getUserName());
        goodsWin.setPhone(orderPurchaseVo.getPhone());
        goodsWin.setAddr(orderPurchaseVo.getAddr());

        //如果商品订单列表的个数为1，代表一件商品，不作循环
        if(goodsList.size() == 1){
            Goods goods = goodsService.getById(goodsList.get(0).getId());
            //直接购买(1个商品)
            goodsWin.setGoodsId(goodsList.get(0).getId());
            goodsWin.setGoodsName(goodsList.get(0).getName());
            goodsWin.setDetail(goodsList.get(0).getDetail());
            goodsWin.setIcon(goodsList.get(0).getIcon());
            goodsWin.setPrice(goodsList.get(0).getPrice());
            goodsWin.setStoreId(goodsList.get(0).getStoreId());
            goodsWin.setStoreName(goodsList.get(0).getStoreName());
            goodsWin.setIssueId(goodsList.get(0).getCurIssueId());
            goodsWin.setIssueNo(goodsList.get(0).getCurIssueNo());
            goodsWin.setOrderType(OrderType.PURCHASE.getCode());
            goodsWin.setRemark(OrderType.PURCHASE.getMsg());
            goodsWin.setNum(orderPurchaseVo.getCartList().get(0).getNum());
            goodsWin.setFirstReferrerScale(goods.getFirstReferrerScale());
            goodsWin.setSecondReferrerScale(goods.getSecondReferrerScale());
            goodsWin.setThirdReferrerScale(goods.getThirdReferrerScale());
            goodsWin.setBusinessSendEp(goods.getBusinessSendEp());
            //判断用户设置的折扣优惠是否等于0
            if(orderPurchaseVo.getCartList().get(0).getDiscountEP() == 0){
                goodsWin.setDiscountEP(0d);
                //只有ep折扣优惠为0时才会赠送斗斗
                goodsWin.setDoudou(PoundageUtil.getPoundage(goods.getBusinessSendEp() * orderPurchaseVo.getCartList().get(0).getNum(),1d));
            }else{
                //如果ep折扣优惠不等于0,则计算ep折扣券优惠
                Double totalDiscountEP = orderPurchaseVo.getCartList().get(0).getNum() * goodsWin.getPrice();
                totalDiscountEP = PoundageUtil.getPoundage(totalDiscountEP * goodsList.get(0).getDiscountEP() / 100,1d);
                //判断用户的ep是否足够扣除
                if(user.getExchangeEP() < totalDiscountEP){
                    //如果用户ep不足，则扣除用户现拥有的ep值
                    totalDiscountEP = user.getExchangeEP();
                }
                goodsWin.setDiscountEP(totalDiscountEP);
            }
        }else {
            //购物车购买(多个商品)
            Goods goods = null;
            GoodsWinDetail goodsWinDetail = null;
            //定义goodsWin的id
            String goodsWinId = UUIDUtil.getUUID();
            //定义一个变量保存总价格
            Double totalPrice = 0d;
            //定义一个变量保存数量
            Integer totalNum = 0;
            //定义一个变量保存ep折扣优惠
            Double totalDiscountEP = 0d;
            //定义一个变量保存斗斗
            Double doudou = 0d;

            for(CartVo cartVo:orderPurchaseVo.getCartList()){
                //根据商品id查询商品信息
                goods = goodsService.getById(cartVo.getGoodsId());
                goodsWinDetail = new GoodsWinDetail();
                goodsWinDetail.setNum(cartVo.getNum());
                goodsWinDetail.setGoodsId(goods.getId());
                goodsWinDetail.setGoodsName(goods.getName());
                goodsWinDetail.setGoodsWinId(goodsWinId);
                goodsWinDetail.setIcon(goods.getIcon());
                goodsWinDetail.setOrderNo(goodsWin.getOrderNo());
                goodsWinDetail.setPrice(goods.getPrice());
                goodsWinDetail.setStoreId(goods.getStoreId());
                goodsWinDetail.setStoreName(goods.getStoreName());
                goodsWinDetail.setStatus(0);
                goodsWinDetail.setUserId(user.getId());
                goodsWinDetail.setFirstReferrerScale(goods.getFirstReferrerScale());
                goodsWinDetail.setSecondReferrerScale(goods.getSecondReferrerScale());
                goodsWinDetail.setThirdReferrerScale(goods.getThirdReferrerScale());
                goodsWinDetail.setBusinessSendEp(goods.getBusinessSendEp());
                goodsWinDetail.setOriginalPrice(goods.getOriginalPrice());
                goodsWinDetail.setDiscountEP(goods.getDiscountEP());
                goodsWinDetailService.add(goodsWinDetail);



                //判断商品是否有进行了ep折扣优惠(错误判断，先留着，说不定以后要改)
                if(cartVo.getDiscountEP() > 0){
                    Double discountEP = cartVo.getNum() * goods.getPrice() * goods.getDiscountEP() / 100;
                    //计算ep折扣优惠
                    totalDiscountEP = PoundageUtil.getPoundage(totalDiscountEP + discountEP ,1d);
                }else {
                    //未使用ep折扣优惠时计算斗斗
                    doudou = doudou + goodsWinDetail.getBusinessSendEp() * goodsWinDetail.getNum();
                }


                //计算价格
                totalPrice = PoundageUtil.getPoundage(totalPrice + (goods.getPrice() * cartVo.getNum()),1d);

                //计算数量
                totalNum = totalNum + cartVo.getNum();

            }

            goodsWin.setId(goodsWinId);
            goodsWin.setGoodsId(null);
            goodsWin.setGoodsName(null);
            goodsWin.setDetail(null);
            goodsWin.setIcon(store.getIcon());
            goodsWin.setPrice(totalPrice);
            goodsWin.setStoreId(goods.getStoreId());
            goodsWin.setStoreName(goods.getStoreName());
            goodsWin.setIssueId(goods.getCurIssueId());
            goodsWin.setIssueNo(goods.getCurIssueNo());
            goodsWin.setOrderType(OrderType.CARTPAY.getCode());
            goodsWin.setRemark(OrderType.CARTPAY.getMsg());
            goodsWin.setNum(totalNum);
            //判断用户设置的折扣优惠是否等于商品的折扣优惠
            //判断用户的ep是否足够扣除
            if(user.getExchangeEP() < totalDiscountEP){
                //如果用户ep不足，则扣除用户现拥有的ep值
                totalDiscountEP = user.getExchangeEP();
            }
            goodsWin.setDiscountEP(totalDiscountEP);
            goodsWin.setDoudou(PoundageUtil.getPoundage(doudou,1d));
        }

        //新增订单
        goodsWinService.add(goodsWin);
        return goodsWin;
    }

    /**
     * 根据商品id和商品数量计算金额
     * @param cartList
     * @return
     */
    @Transactional
    public Map<String,Double> countMoney(List<CartVo> cartList,User user,String goodWinId) throws Exception{
        //定义一个map集合保存数据
        Map<String,Double> map = new HashMap<>();
        //定义变量保存商品信息
        Goods goods = null;
        //定义总价格
        Double originalPrice = 0d;
        //定义ep折扣
        Double totalDiscountEP = 0d;
        //定义原ep折扣
        Double originalDiscountEp =0d;
        //计算实付商品价格
        Double realPayPrice = 0d;
        //循环计算金额
        for(CartVo cartVo : cartList){
            //计算单件实付价格
            Double singlePayPrice = 0d;
            //根据商品id查询商品信息
            goods = goodsService.getById(cartVo.getGoodsId());
            singlePayPrice=PoundageUtil.getPoundage(goods.getPrice() * cartVo.getNum(),1d);
            //计算商品总价
            originalPrice = originalPrice + singlePayPrice;
            //计算商品ep折扣优惠价格
            if(cartVo.getDiscountEP() !=null && cartVo.getDiscountEP() > 0) {
                totalDiscountEP = totalDiscountEP + PoundageUtil.getPoundage(singlePayPrice * goods.getDiscountEP() / 100, 1d);
                originalDiscountEp = totalDiscountEP;
            }
        }

        //判断用户是否有足够的ep抵扣
        if(user.getExchangeEP() < totalDiscountEP) {
            //如果不够，则选取用户所有的ep
            totalDiscountEP = user.getExchangeEP();
        }
        //计算实付金额
        realPayPrice = originalPrice - totalDiscountEP;
        originalDiscountEp= PoundageUtil.getPoundage(originalDiscountEp,1d);
        originalPrice= PoundageUtil.getPoundage(originalPrice,1d);
        totalDiscountEP= PoundageUtil.getPoundage(totalDiscountEP,1d);
        realPayPrice= PoundageUtil.getPoundage(realPayPrice,1d);

        map.put("originalDiscountEp",originalDiscountEp);
        map.put("originalPrice",originalPrice);
        map.put("totalDiscountEp",totalDiscountEP);
        map.put("realPayPrice",realPayPrice);
        return map;
    }

    /**
     * 待付款订单计算金额
     * @return
     */
    @Transactional
    @Override
    public Map<String,Double> calcMoney(boolean isUseDiscount,User user,String goodWinId) throws Exception{
        //定义一个map集合保存数据
        Map<String,Double> map = new HashMap<>();
        //定义总价格
        Double originalPrice = 0d;
        //定义ep折扣
        Double totalDiscountEP = 0d;
        //定义原ep折扣
        Double originalDiscountEp =0d;
        //计算实付商品价格
        Double realPayPrice = 0d;
        List<GoodsWinDetail> list = goodsWinDetailService.getListByGoodsWinId(goodWinId);
        GoodsWin goodsWin = goodsWinService.getById(goodWinId);
        if(list == null || list.size() == 0){
            //计算商品总价
            originalPrice = PoundageUtil.getPoundage(goodsWin.getPrice()*goodsWin.getNum(),1d,2);
            //计算商品ep折扣优惠价格
            if (isUseDiscount && goodsWin.getDiscountEP() != null && goodsWin.getDiscountEP() > 0) {
                totalDiscountEP = PoundageUtil.getPoundage(goodsWin.getDiscountEP(),1d,2);
                originalDiscountEp = totalDiscountEP;
            }
        }else {
            //循环计算金额
            for (GoodsWinDetail detail : list) {
                //计算单件实付价格
                Double singlePayPrice = 0d;
                singlePayPrice = PoundageUtil.getPoundage(detail.getPrice() * detail.getNum(), 1d);
                //计算商品总价
                originalPrice = originalPrice + singlePayPrice;
                //计算商品ep折扣优惠价格
                if (isUseDiscount && detail.getDiscountEP() != null && detail.getDiscountEP() > 0) {
                    totalDiscountEP = totalDiscountEP + PoundageUtil.getPoundage(singlePayPrice * detail.getDiscountEP()*detail.getNum() / 100, 1d);
                    originalDiscountEp = totalDiscountEP;
                }
            }
        }

        //判断用户是否有足够的ep抵扣
        if(user.getExchangeEP() < totalDiscountEP) {
            //如果不够，则选取用户所有的ep
            totalDiscountEP = user.getExchangeEP();
        }
        //计算实付金额
        realPayPrice = originalPrice - totalDiscountEP;
        originalDiscountEp= PoundageUtil.getPoundage(originalDiscountEp,1d);
        originalPrice= PoundageUtil.getPoundage(originalPrice,1d);
        totalDiscountEP= PoundageUtil.getPoundage(totalDiscountEP,1d);
        realPayPrice= PoundageUtil.getPoundage(realPayPrice,1d);

        map.put("originalDiscountEp",originalDiscountEp);
        map.put("originalPrice",originalPrice);
        map.put("totalDiscountEp",totalDiscountEP);
        map.put("realPayPrice",realPayPrice);
        return map;
    }


    @Override
    @Transactional
    public void purchasingUpdate(List<CartVo> cartList, User user, GoodsWin goodsWin,Map<String,Double> map) throws Exception {
        //定义变量保存商品信息
        /*Goods goods = null;
        //循环计算金额
        for(CartVo cartVo : cartList){
            goods = goodsService.getById(cartVo.getGoodsId());
            //修改明细数据
            GoodsWinDetail detail = new GoodsWinDetail();
            detail.setGoodsWinId(goodsWin.getId());
            detail.setOrderNo(goodsWin.getOrderNo());
            detail.setPrice(goods.getPrice());
            detail.setFirstReferrerScale(goods.getFirstReferrerScale());
            detail.setSecondReferrerScale(goods.getSecondReferrerScale());
            detail.setThirdReferrerScale(goods.getThirdReferrerScale());
            detail.setBusinessSendEp(goods.getBusinessSendEp());
            detail.setDiscountEP(goods.getDiscountEP());
            detail.setOriginalPrice(goods.getOriginalPrice());
            detail.setUpdateTime(new Date());
            //根据商品id和订单主表的主键id更新订单明细表数据
            goodsWinDetailService.updateByGoodsId(goods.getId(),detail);
        }*/
        //修改订单
        GoodsWin updateGoodsWin = new GoodsWin();
        //判断是否是购物车订单
        if (cartList.size()>1){
            updateGoodsWin.setPrice(map.get("originalPrice"));
        }
        updateGoodsWin.setPayWay(goodsWin.getPayWay());
        updateGoodsWin.setDiscountEP(map.get("totalDiscountEp"));
        goodsWinService.update(goodsWin.getId(),updateGoodsWin);
    }


    /**
     * 直接购买订单回调业务
     * @param user
     * @param orderNo
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public Boolean purchaseHandlerByApp(User user, String orderNo) throws Exception {

        //获取商品订单
        GoodsWin goodsWin = goodsWinService.getByOrderNo(orderNo,user.getId());

        if(goodsWin == null){
            logger.error(String.format("Illegal goodsWin orderNo[%s]", orderNo));
            throw new IllegalArgumentException();
        }
        if(goodsWin.getStatus().intValue() != GoodsWinType.PENDING.getCode()){
            logger.error(goodsWin.getOrderNo()+"：订单状态不是待付款，不需要处理");
            throw new IllegalArgumentException();
        }

        //定义一个变量保存积分
        Double score =0d;
        //定义一个变量保存金额，比较折扣是否等于金额
        Double discountScore = 0d;
        //定义一个变量订单中商品数量之和，用于累计商家销售商品之和
        Integer sumSales = 0;

        if(goodsWin.getOrderType().intValue() == OrderType.PURCHASE.getCode().intValue()){
            //单件商品购买
            //计算积分
            score =  PoundageUtil.getPoundage(goodsWin.getPrice() * goodsWin.getNum() - goodsWin.getDiscountEP(),1d);
            discountScore = PoundageUtil.getPoundage(goodsWin.getPrice() * goodsWin.getNum(),1d);
            // 扣减商品库存
            goodsService.updateStock(goodsWin.getGoodsId(), -goodsWin.getNum());
            // 累计单件商品销售数量
            goodsService.updateSaleCount(goodsWin.getGoodsId(), goodsWin.getNum());
            // 累计商家商品销售数量
            sumSales = goodsWin.getNum();

        }else if(goodsWin.getOrderType().intValue() == OrderType.CARTPAY.getCode().intValue()){
            //多件商品购买
            //计算积分
            score =  PoundageUtil.getPoundage(goodsWin.getPrice() - goodsWin.getDiscountEP(),1d);
            discountScore = PoundageUtil.getPoundage(goodsWin.getPrice(),1d);
            //获取购物车商品列表
            List<GoodsWinDetail> goodsWinDetailList = goodsWinDetailService.getListByGoodsWinId(goodsWin.getId());
            //定义一个goods变量
            Goods goods = null;
            sumSales = 0;
            for(GoodsWinDetail goodsWinDetail : goodsWinDetailList){
                goodsService.updateStock(goodsWinDetail.getGoodsId(), -goodsWinDetail.getNum());
                // 累计单件商品销售数量
                goodsService.updateSaleCount(goodsWinDetail.getGoodsId(), goodsWinDetail.getNum());
                sumSales +=goodsWinDetail.getNum();
            }
        }

        //判断是否有ep折扣优惠
        if(goodsWin.getDiscountEP() != 0){
            //扣除用户ep
            userService.updateEp(user.getId(),-goodsWin.getDiscountEP());
        }

        //扣减用户积分
        if(goodsWin.getPayWay().intValue() == BankCardType.PURCHASE_BALANCE.getCode().intValue()){
            //扣除用户积分
            userService.updateScore(user.getId(), -score);
        }

        //判断用户是否全部用ep折扣来付款
        if(goodsWin.getDiscountEP() != discountScore){
            //增加用户积分流水
            addUserScoreRecord(goodsWin.getUserId(),goodsWin.getOrderNo(),-score,RecordType.PURCHASE.getCode(),RecordType.PURCHASE.getMsg());
            //新增用户消费消息
            String detail = "-" + score + "金额";
            addUserScoreAndEpMessage(goodsWin.getUserId(),goodsWin.getOrderNo(),RecordType.PURCHASE.getMsg(),MessageType.PURCHASE.getCode(),detail,"App版直接购买");
        }

        //增加商家卖商品消息
        Store store = storeService.getById(goodsWin.getStoreId());
        Message scoreMessage = new Message();
        scoreMessage.setUserId(store.getUserId());
        scoreMessage.setOrderNo(goodsWin.getOrderNo());
        scoreMessage.setTitle(MessageType.ORDER.getMsg());
        scoreMessage.setType(MessageType.ORDER.getCode());
        scoreMessage.setDetail("有人购买了你的商品,请及时发货!");
        scoreMessage.setRemark("App版直接购买");
        messageService.add(scoreMessage);

        //待付款订单改为待发货状态
        GoodsWin newOrder = new GoodsWin();
        newOrder.setId(goodsWin.getId());
        newOrder.setStatus(GoodsWinType.BUYED.getCode());
        goodsWinService.update(newOrder.getId(),newOrder);

        // 获取商家信息唯一的极光推送Id
        User storeUser = userService.getById(store.getUserId());
        //累计商家销售商品之和
        storeService.updateSumSaleCount(store.getId(),sumSales);
        // 普通用户购买成功后推送给商家，提醒发货
        boolean rs = JPushUtil.pushPayloadByid(storeUser.getRegistrationId(), "您有新的商品需要发货，请尽快处理",new JpushExtraModel(JpushExtraModel.NOTIFIYPE,JpushExtraModel.STORE_ORDER));
        logger.info("推送ID:" + storeUser.getRegistrationId() + ";用户ID:" + storeUser.getUid() + ";推送结果：" + rs);
        return true;
    }

    /**
     * 新增用户流水消息
     **/
    private void addUserScoreRecord(String id, String orderNo, double score, Integer recordType, String msg) throws Exception {
        WalletRecord record = new WalletRecord();
        record.setUserId(id);
        record.setOrderNo(orderNo);
        record.setScore(score);
        record.setRecordType(recordType);
        record.setRemark(msg);
        walletRecordService.add(record);
    }

    /**
     * 新增扫码ep消息、支付消息
     **/
    private void addUserScoreAndEpMessage(String id, String orderNo, String msg, Integer type, String detail, String remark) throws Exception {
        Message message = new Message();
        message.setUserId(id);
        message.setOrderNo(orderNo);
        message.setTitle(msg);
        message.setType(type);
        message.setDetail(detail);
        message.setRemark(remark);
        messageService.add(message);
    }


}
