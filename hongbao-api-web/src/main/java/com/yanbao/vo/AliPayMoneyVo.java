/* 
 * 文件名：AliPayMoneyVo.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：李小龙 
 * 创建时间：2017年2月17日
 * 版本号：v1.0
*/
package com.yanbao.vo;

import java.io.Serializable;

/**
 * 支付宝充值对象
 */
public class AliPayMoneyVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户订单号
     */
    private String tradeNo;

    /**
     * 订单金额
     */
    private Double payAmount;

    /**
     * 订单标题
     */
    private String orderTitle;

    /**
     * 通知返回页面
     */
    private String returnPage;

    /**
     * 通知返回服务器
     */
    private String notifyServe;

    /**
     * 用户访问ip地址
     */
    private String ip;

    /**
     * 回调用接口
     */
    private String returnUrl;

    /**
     * 支付宝发起url
     */
    private String payUrl;

    /**
     * 会员id
     */
    private String userId;
    /**
     * token
     */
    private String token;
    /**
     * 交易类型(10:面对面支付;20:支付宝充值)
     */
    private String tranType;

    /**
     * @return the tranType
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * @param tranType the tranType to set
     */
    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    /**
     * @return the tradeNo
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * @param tradeNo the tradeNo to set
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * @return the payAmount
     */
    public Double getPayAmount() {
        return payAmount;
    }

    /**
     * @param payAmount the payAmount to set
     */
    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * @return the orderTitle
     */
    public String getOrderTitle() {
        return orderTitle;
    }

    /**
     * @param orderTitle the orderTitle to set
     */
    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    /**
     * @return the returnPage
     */
    public String getReturnPage() {
        return returnPage;
    }

    /**
     * @param returnPage the returnPage to set
     */
    public void setReturnPage(String returnPage) {
        this.returnPage = returnPage;
    }

    /**
     * @return the notifyServe
     */
    public String getNotifyServe() {
        return notifyServe;
    }

    /**
     * @param notifyServe the notifyServe to set
     */
    public void setNotifyServe(String notifyServe) {
        this.notifyServe = notifyServe;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the returnUrl
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * @param returnUrl the returnUrl to set
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }
}
