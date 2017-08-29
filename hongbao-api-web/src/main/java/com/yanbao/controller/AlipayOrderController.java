package com.yanbao.controller;

import com.mall.model.OrderTypeModel;
import com.mall.model.SecondCallBack;
import com.mall.model.User;
import com.yanbao.constant.BankCardType;
import com.yanbao.core.model.Token;
import com.yanbao.service.ComOderService;
import com.yanbao.service.OrderTypeService;
import com.yanbao.service.SecondCallBackService;
import com.yanbao.service.UserService;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.TokenUtil;
import com.yanbao.util.ToolUtil;
import com.yanbao.util.alipay.AliPayUtils;
import com.yanbao.vo.AliPayMoneyVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/alipay")
public class AlipayOrderController {

    private static final Logger logger = LoggerFactory.getLogger(AlipayOrderController.class);


    @Autowired
    private SecondCallBackService secondCallBackService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private ComOderService comOderService;
    @Autowired
    private UserService userService;
    @Value("${environment}")
    private String environment;

    /**
     * 跳转支付宝
     */
    @RequestMapping("/paypage")
    @ResponseBody
    public void payPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AliPayMoneyVo aliPayMoneyVo) {
        try {
            if (ToolUtil.isEmpty(aliPayMoneyVo.getTradeNo()) || "undefined".equals(aliPayMoneyVo.getTradeNo())) {
                logger.error("充值订单号不能为空!");
                return;
            }
            if (ToolUtil.isEmpty(aliPayMoneyVo.getPayAmount())) {
                logger.error("充值金额不能为空!");
                return;
            }
            if (ToolUtil.isEmpty(aliPayMoneyVo.getOrderTitle())) {
                logger.error("标题不能为空!");
                return;
            }
            if (ToolUtil.isEmpty(aliPayMoneyVo.getReturnUrl()) || "undefined".equals(aliPayMoneyVo.getReturnUrl())) {
                logger.error("回调参数类型错误");
                return;
            }
            if (!("test".equals(aliPayMoneyVo.getReturnUrl().trim()) || "online".equals(aliPayMoneyVo.getReturnUrl().trim()))) {
                logger.error("returnUrl回调参数类型错误");
                return;
            }

            if (ToolUtil.isEmpty(aliPayMoneyVo.getTranType()) || "undefined".equals(aliPayMoneyVo.getTranType())) {
                logger.error("交易类型不能为空!");
                return;
            }
            SecondCallBack callBack = secondCallBackService.getById(aliPayMoneyVo.getTranType());
            if (ToolUtil.isEmpty(callBack)) {
                logger.error("调起支付,支付类型不存在，请检查支付类型: 99999 " + aliPayMoneyVo.getTranType() + " 时间：  " + DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG));
                return;
            }
            if (!"50".equals(aliPayMoneyVo.getTranType()) || !"500".equals(aliPayMoneyVo.getTranType())) {
                if (ToolUtil.isEmpty(aliPayMoneyVo.getUserId()) || "undefined".equals(aliPayMoneyVo.getUserId())) {
                    logger.error("用户Id不能为空!");
                    return;
                }
                if (ToolUtil.isEmpty(aliPayMoneyVo.getToken()) || "undefined".equals(aliPayMoneyVo.getToken())) {
                    logger.error("token不能为空!");
                    return;
                }
            }
            if ("test".equals(aliPayMoneyVo.getReturnUrl())) {
                if (ToolUtil.isEmpty(callBack.getTestReturnUrl())) {
                    logger.error("数据库类型错误，请检查支付类型: 99999 " + callBack.getTestReturnUrl() + " 时间：  " + DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG));
                    return;
                }
                aliPayMoneyVo.setReturnUrl(callBack.getTestReturnUrl());
            } else if ("online".equals(aliPayMoneyVo.getReturnUrl())) {
                if (ToolUtil.isEmpty(callBack.getReturnUrl())) {
                    logger.error("数据库类型错误，请检查支付类型: 99999 " + callBack.getReturnUrl() + " 时间：  " + DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG));
                    return;
                }
                aliPayMoneyVo.setReturnUrl(callBack.getReturnUrl());
            }
            //获取用户真实ip地址
            String ip = ToolUtil.getRemoteAddr(httpRequest);
            //将ip地址封装进RechargeMoneyVo类中
            aliPayMoneyVo.setIp(ip);
            //获取支付url
            aliPayMoneyVo.setPayUrl(httpRequest.getRequestURL().toString() + "?" + httpRequest.getQueryString());
            //获取表单对象
            String form = AliPayUtils.alipayPreOrderForWap(aliPayMoneyVo.getTradeNo(), aliPayMoneyVo.getReturnUrl(), aliPayMoneyVo.getPayAmount(), aliPayMoneyVo.getOrderTitle());
            httpResponse.setContentType("text/html;charset=utf-8");
            httpResponse.getWriter().write(form);
            //直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
        } catch (Exception e) {
            logger.error("支付宝充值报错" + e);
            e.printStackTrace();
        }
    }


    /**
     * 支付宝支付通知接口
     *
     * @param request
     * @param response
     */
    @RequestMapping("/notify")
    @ResponseBody
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        String responseCode = "";
        String orderNo = "";
        try {
            Map<String, Object> map = AliPayUtils.nofifyMap(request);
            orderNo = (String) map.get("orderNo");
            responseCode = (String) map.get("responseCode");
            if (map != null && map.containsKey("sign") &&  (Boolean) map.get("sign")) {
                OrderTypeModel orderType = orderTypeService.getById(orderNo);
                if (orderType == null) {
                    logger.error("回调配置表找不到订单，订单号为:" + orderNo);
                }
                if (ToolUtil.isEmpty(orderType.getType())) {
                    logger.error("回调配置表业务类型错误：订单号为:" + orderNo + "  类型为：" + orderType.getType(), orderType.getRemark());
                }
                Token token= null;
                User user= null;
                Boolean isAlipayPage =BankCardType.STORE_SCAN_PAGE_ALIPAY.getCode()==orderType.getType();
                if (!isAlipayPage &&  ToolUtil.isEmpty(orderType.getToken())) {
                    logger.error("回调配置表token为空：订单号为:" + orderNo + "  类型为：" + orderType.getType(), orderType.getRemark());
                }
                token =  (Token) TokenUtil.getTokenObject(orderType.getToken());
                if (!isAlipayPage &&  token==null){
                    logger.error("回调配置表token失效：订单号为:" + orderNo + "  类型为：" + orderType.getType(), orderType.getRemark() + "  token:" + orderType.getToken());
                }
                user = userService.getById(token.getId());
                if (!isAlipayPage && user == null) {
                    logger.error("回调配置表token失效：订单号为:" + orderNo + "  类型为：" + orderType.getType(), orderType.getRemark() + "  token:" + orderType.getToken());
                }
                Boolean flag = comOderService.handleOrder(user,orderNo);
                if (flag) {
                    response.getOutputStream().write("success".getBytes());
                    response.flushBuffer();
                } else {
                    response.getOutputStream().write("failure".getBytes());
                    response.flushBuffer();
                }
            } else {
                logger.error("验签失败，订单号为:" + orderNo);
                response.getOutputStream().write("failure".getBytes());
                response.flushBuffer();
            }
        } catch (Exception e) {
            logger.error("支付宝通知异常" + e);
            e.printStackTrace();
        }
    }
}
