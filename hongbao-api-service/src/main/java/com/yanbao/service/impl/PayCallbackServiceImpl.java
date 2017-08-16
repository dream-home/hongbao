package com.yanbao.service.impl;

import com.mall.model.GoodsWin;
import com.mall.model.PayCallback;
import com.mall.model.WalletRecharge;
import com.yanbao.constant.BankCardType;
import com.yanbao.constant.StatusType;
import com.yanbao.dao.PayCallbackDao;
import com.yanbao.service.GoodsWinService;
import com.yanbao.service.PayCallbackService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletRechargeService;
import com.yanbao.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Title:PayCallbackServiceImpl
 * Description:支付回调记录服务实现类
 * Copyright: Copyright (c) 2017
 * Company: 炎宝科技
 *
 * @author Pay - 1091945691@qq.com
 * @version v1.0 2017-05-26
 */
@Service
public class PayCallbackServiceImpl implements PayCallbackService {
    @Autowired
    private PayCallbackDao dao;
    @Autowired
    private WalletRechargeService walletRechargeService;
    @Autowired
    private GoodsWinService goodsWinService;
    @Autowired
    private UserService userService;

    @Override
    public Integer add(PayCallback model) throws Exception {
        return dao.add(model);
    }

    @Override
    public Integer del(PayCallback model) throws Exception {
        return dao.del(model);
    }

    @Override
    public Integer delByPk(String id) throws Exception {
        return dao.delByPk(id);
    }

    @Override
    public Integer delList(List<String> ids) throws Exception {
        return dao.delList(ids);
    }

    @Override
    public Integer modUpdate(PayCallback model) throws Exception {
        return dao.modUpdate(model);
    }

    @Override
    public Integer modUpdateNotNull(PayCallback model) throws Exception {
        return dao.modUpdateNotNull(model);
    }

    @Override
    public PayCallback getByPk(String id) throws Exception {
        return dao.getByPk(id);
    }

    @Override
    public Long getCount(PayCallback model) throws Exception {
        return dao.getCount(model);
    }

    @Override
    public List<PayCallback> getList(PayCallback model) throws Exception {
        return dao.getList(model);
    }

    /**
     * 记录所有支付回调的操作
     *
     * @param orderNo 订单号 唯一
     * @param uri     请求回调地址
     * @param params  请求回调的参数
     * @param flag    false 启动回调，true 成功回调
     */
    @Override
    public void doCallBackAction(String orderNo, String uri, String params, boolean flag) {
        //就算错了也不影响回调方法
        try {
            if (StringUtils.isEmpty(orderNo)){
                buildWrongPayCallBack(null, uri, params, flag, "", "");
                return;
            }
            WalletRecharge recharge = walletRechargeService.getUserOrderByOrderNo(orderNo);//来源二维码支付
            GoodsWin win = null;
            String userId = "";
            Integer payWay = null;
            String orderMsg="";
            if (recharge != null) {
                userId = recharge.getUserId();
                payWay = recharge.getSource();
            } else {
                orderMsg="未查询到充值订单";
                win = goodsWinService.getUserOrderByOrderNo(orderNo);//来源购买
                if (win != null) {
                    userId = win.getUserId();
                    payWay = win.getPayWay();
                } else {
                    orderMsg="未查询到充值订单并且未查询到购买订单和扫码支付订单";
                    buildWrongPayCallBack(null, uri, params, flag, userId, orderMsg);
                    return;
                }
            }
            buildPayCallBack(orderNo, uri, params, flag, userId, payWay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildPayCallBack(String orderNo, String uri, String params, boolean flag, String userId, Integer payWay) throws Exception {
        PayCallback callback = new PayCallback();
        callback.setId(UUIDUtil.getUUID());
        callback.setCreateTime(new Date());
        callback.setOrderNo(orderNo);
        callback.setStatus(StatusType.TRUE.getCode());
        callback.setPayWay(payWay);
        callback.setUserId(userId);
        callback.setUri(uri);
        callback.setParam(params);
        String remark = !flag ? "启动回调：" : "成功回调：" ;
        remark=remark.concat(payWay != null ? BankCardType.fromCode(payWay).getMsg() : null);
        callback.setRemark(remark);
        this.add(callback);
    }

    private void buildWrongPayCallBack(String orderNo, String uri, String params, boolean flag, String userId, String msg) throws Exception {
        PayCallback callback = new PayCallback();
        callback.setId(UUIDUtil.getUUID());
        callback.setCreateTime(new Date());
        callback.setOrderNo(orderNo);
        callback.setStatus(StatusType.TRUE.getCode());
        callback.setPayWay(null);
        callback.setUserId(userId);
        callback.setUri(uri);
        callback.setParam(params);
        String remark = !flag ? "启动回调：" : "成功回调：" ;
        remark=remark.concat(msg);
        callback.setRemark(remark);
        this.add(callback);
    }
}