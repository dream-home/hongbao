package com.yanbao.service.impl;

import com.mall.model.GoodsWin;
import com.mall.model.OrderTypeModel;
import com.mall.model.User;
import com.mall.model.WalletRecharge;
import com.yanbao.constant.BankCardType;
import com.yanbao.constant.GoodsWinType;
import com.yanbao.service.*;
import com.yanbao.util.RedisLock;
import com.yanbao.util.ToolUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2018年08月29日
 */
@Service
public class ComOrderServiceImpl implements ComOderService {
    private static Log log = LogFactory.getLog(ComOrderServiceImpl.class);
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private WalletRechargeService walletRechargeService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private OrderService orderService;

    @Override
    public Boolean handleOrder(User user, String orderNo) throws Exception {
        OrderTypeModel orderType = orderTypeService.getById(orderNo);
        if (orderType == null) {
            log.error("回调配置表找不到订单，订单号为:" + orderNo);
        }
        if (ToolUtil.isEmpty(orderType.getType())) {
            log.error("回调配置表业务类型错误：订单号为:" + orderNo + "  类型为：" + orderType.getType() + "  " + orderType.getRemark());
        }
        switch (orderType.getType()) {
            case 1:
                rechargeHandle(user, orderNo, orderType);
                log.error("支付宝充值回调成功，订单号："+orderNo);
                return true;
            case 11:
                faceScanHandle(user, orderNo, orderType);
                log.error("000000000000000000000000000000000  支付宝面对面扫码回调成功，订单号："+orderNo);
                return true;
            case 13:
                purchaseHandle(user, orderNo, orderType);
                log.error("000000000000000000000000000000000  支付宝直接购买回调成功，订单号："+orderNo);
                return true;
            case 14:
                storeAppScanHandle(user, orderNo, orderType);
                log.error("000000000000000000000000000000000  支付宝斗拍APP内商家扫码回调成功，订单号："+orderNo);
                return true;
            case 15:
                storePageWithZFBScanHandle(orderNo, orderType);
                log.error("000000000000000000000000000000000  支付宝客户端发起网页商家扫码回调成功，订单号："+orderNo);
                return true;
            case 16:
                joinParternHandle(user, orderNo, orderType);
                log.error("000000000000000000000000000000000  支付宝加入合伙人回调成功，订单号："+orderNo);
                return true;
        }
        return false;
    }

    private void joinParternHandle(User user, String orderNo, OrderTypeModel orderType) throws Exception {
        WalletRecharge model = walletRechargeService.getByOrderNo(orderNo, user.getId());
        if (model == null) {
            log.error("加入合伙人回调配置表业务类型错误：订单号为:" + orderNo + "  类型为：" + orderType.getType() + "  " + orderType.getRemark());
        }
        if (model.getSource().intValue() == BankCardType.JOIN_ALIPAY.getCode().intValue()) {
            //支付宝面对面扫码支付
            Boolean flag = RedisLock.redisLock(orderNo, "", 6);
            if (flag) {
                walletRechargeService.joinPartnerHandler(user, orderNo);
            }
        }
    }

    private void purchaseHandle(User user, String orderNo, OrderTypeModel orderType) throws Exception {
        GoodsWin goodsWin = goodsWinService.getByOrderNo(orderNo, user.getId());
        if (goodsWin == null) {
            log.error("直接购买回调配置表业务类型错误：订单号为:" + orderNo + "  类型为：" + orderType.getType() + "  " + orderType.getRemark());
        }
        boolean flag1 = goodsWin == null || goodsWin.getStatus() != GoodsWinType.PENDING.getCode().intValue();
        if (flag1) {
            log.error("直接购买回调配置表业务类型错误：订单号为:" + orderNo + "  类型为：" + orderType.getType() + "  " + orderType.getRemark() + " 购买订单类型为：scenes:" + goodsWin.getScenes() + "    orderType :" + goodsWin.getOrderType());
        } else {
            Boolean flag = RedisLock.redisLock(orderNo, "", 6);
            if (flag) {
                orderService.purchaseHandlerByApp(user, orderNo);
            }
        }
    }

    private void faceScanHandle(User user, String orderNo, OrderTypeModel orderType) throws Exception {
        WalletRecharge model = walletRechargeService.getByOrderNo(orderNo, user.getId());
        if (model == null) {
            log.error("面对面扫码回调配置表业务类型错误：订单号为:" + orderNo + "  类型为：" + orderType.getType() + "  " + orderType.getRemark());
        }
        if (model.getSource().intValue() == BankCardType.SCAN_CODE_ALIPAY.getCode().intValue()) {
            //支付宝面对面扫码支付

            Boolean flag = RedisLock.redisLock(orderNo, "", 6);
            if (flag) {
                walletRechargeService.scanCodeHandler(user, orderNo);
            }
        }
    }

    private void rechargeHandle(User user, String orderNo, OrderTypeModel orderType) throws Exception {
        WalletRecharge model = walletRechargeService.getByOrderNo(orderNo, user.getId());
        if (model == null) {
            log.error("充值支付宝原生支付回调订单不存在：订单号为:" + orderNo + "  类型为：" + orderType.getType() + "  " + orderType.getRemark());
            return;
        }
        //支付宝充值

        Boolean flag = RedisLock.redisLock(orderNo, "", 6);
        if (flag) {
            walletRechargeService.rechargeHandler(user, orderNo);
        }
        return;
    }

    private void storeAppScanHandle(User user, String orderNo, OrderTypeModel orderType) throws Exception {
        WalletRecharge model = walletRechargeService.getByOrderNo(orderNo, user.getId());
        if (model == null) {
            log.error("商家扫码斗拍APP内回调配置表业务类型错误：订单号为:" + orderNo + "  类型为：" + orderType.getType() + "  " + orderType.getRemark());
        }
        if (model.getSource().intValue() == BankCardType.SCAN_CODE_ALIPAY.getCode().intValue()) {
            //支付宝面对面扫码支付

            Boolean flag = RedisLock.redisLock(orderNo, "", 6);
            if (flag) {
                walletRechargeService.storeScanCodeHandler(user, orderNo);
            }
        }
    }

    private void storePageWithZFBScanHandle(String orderNo, OrderTypeModel orderType) throws Exception {
        WalletRecharge model = walletRechargeService.getUserOrderByOrderNo(orderNo);
        if (model == null) {
            log.error("商家扫码网页支付宝客户端直接发起的支付，回调配置表业务类型错误：订单号为:" + orderNo + "  类型为：" + orderType.getType() + "  " + orderType.getRemark());
        }
        if (model.getSource().intValue() == BankCardType.SCAN_CODE_ALIPAY.getCode().intValue()) {
            //支付宝面对面扫码支付

            Boolean flag = RedisLock.redisLock(orderNo, "", 6);
            if (flag) {
                walletRechargeService.storeScanCodeHandler(null, orderNo);
            }
        }
    }
}
