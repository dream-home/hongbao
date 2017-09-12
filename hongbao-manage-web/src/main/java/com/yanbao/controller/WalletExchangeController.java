package com.yanbao.controller;

import com.mall.model.*;
import com.yanbao.constant.BankCardType;
import com.yanbao.constant.MessageType;
import com.yanbao.constant.RecordType;
import com.yanbao.core.page.JsonResult;
import com.yanbao.service.*;
import com.yanbao.util.ParamUtil;
import com.yanbao.util.UUIDUtil;
import com.yanbao.util.refunds.RefundsUtils;
import com.yanbao.vo.Cash;
import com.yanbao.vo.CashMoneyVo;
import com.yanbao.vo.HttpUtils;
import com.yanbao.vo.ToolUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by summer on 2016-12-09:15:58;
 * <p>
 * 财务管理模块
 */

@RequestMapping("/exchange")
@Controller
public class WalletExchangeController extends BaseController {

    @Autowired
    WalletExchangeService walletExchangeService;
    @Autowired
    WalletRecordService walletRecordService;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    private AgentInfoService agentInfoService;
    @Autowired
    private AgentBillService agentBillService;
    @Autowired
    private AgentBillDetailService agentBillDetailService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private PartnerBillDetailService partnerBillDetailService;
    @Value("${certificatPath}")
    String certificatPath;
    @Value("${wechartAppId}")
    String wechartAppId;
    @Value("${wechartMuchId}")
    String wechartMuchId;

    /**
     * 提现审核 需要参照 最小提现金额 最大提现金额 审核不通过 需要退款
     *
     * @param userId
     * @param id
     * @param status
     * @return
     */
    @RequestMapping("/check")
    @ResponseBody
    public JsonResult check(HttpServletRequest request, String userId, String id, int status) {
        WalletExchange walletExchange = walletExchangeService.readById(id);
        if (walletExchange == null) {
            return fail("记录不存在");
        }
        if (walletExchange.getStatus() != 0) {
            return fail("已经审核,不可更改");
        }
        if (status == 1) {//通过
            walletExchange.setStatus(1);
            walletExchangeService.updateById(id, walletExchange);
        }
        if (status == 2) {
            try {
                walletExchange.setStatus(2);
                walletExchangeService.reFundScore(walletExchange);
                // 插入流水表
                WalletRecord walletRecord = new WalletRecord();
                walletRecord.setUserId(walletExchange.getUserId());
                walletRecord.setOrderNo(walletExchange.getOrderNo());
                walletRecord.setScore(-walletExchange.getScore());
                walletRecord.setRemark("兑换失败，余额退回");
                walletRecord.setRecordType(RecordType.ROLLBACK.getCode());
                walletRecordService.createWithUUID(walletRecord);
                // 发送兑换失败消息
                Message message = new Message();
                message.setId(UUIDUtil.getUUID());
                message.setUserId(walletExchange.getUserId());
                message.setOrderNo(walletExchange.getOrderNo());
                message.setTitle("兑换失败");
                message.setType(MessageType.EXCHANGE.getCode());
                message.setDetail("兑换失败，余额已退回您的钱包");
                message.setRemark("审核不通过");
                message.setStatus(0);
                messageService.create(message);
            } catch (Exception e) {
                return fail(1500, "审核退款出现异常");
            }
        }
        return success();
    }

    /**
     * 手工提现 : 提现表 流水表
     *
     * @return
     */
    /**
     * @param exchangeId
     * @param request
     * @return
     */
    @RequestMapping("/extMoney")
    @ResponseBody
    public JsonResult extMoney(String exchangeId, HttpServletRequest request) throws Exception {
        WalletExchange walletExchange = new WalletExchange();
        walletExchange.setId(exchangeId);
        walletExchange = walletExchangeService.readById(exchangeId);
        if (walletExchange == null || walletExchange.getStatus() != 1) {
            return fail("没有发现提现记录或提现未通过审核!");
        }

        User user = userService.readById(walletExchange.getUserId());

        if (ToolUtil.isEmpty(user)) {
            return fail("提现用户不能为空");
        }
        //certificatPath="E:\\weChart\\apiclient_cert.p12";
        if (null != walletExchange.getCardType() && walletExchange.getCardType() == BankCardType.DONATE_WITH_WEIXIN.getCode()) {
        	Map<String,String> resultMap=new HashMap<String, String>();
        	resultMap.put("NOAUTH", "没有权限");
        	resultMap.put("AMOUNT_LIMIT", "付款金额不能小于最低限额，每次付款金额必须大于1元");
        	resultMap.put("PARAM_ERROR", "参数错误");
        	resultMap.put("OPENID_ERROR", "Openid错误");
        	resultMap.put("NOTENOUGH", "余额不足");
        	resultMap.put("SYSTEMERROR", "系统繁忙，请稍后再试。");
        	resultMap.put("NAME_MISMATCH", "姓名校验出错");
        	resultMap.put("SIGN_ERROR", "签名错误");
        	resultMap.put("XML_ERROR", "Post内容出错");
        	resultMap.put("FATAL_ERROR", "两次请求参数不一致");
        	resultMap.put("CA_ERROR", "证书出错");
        	resultMap.put("V2_ACCOUNT_SIMPLE_BAN", "无法给非实名用户付款");
            if (null != certificatPath && !"".equals(certificatPath) && new File(certificatPath).exists()) {
                //自动微信转账
                Map map=null;
                try {
                    if (StringUtils.isEmpty(walletExchange.getCardNo())) {
                        return fail("无法完成转账！无微信openID");
                    }
                    if (StringUtils.isEmpty(walletExchange.getOrderNo())) {
                        return fail("无法完成转账！无订单号");
                    }
                    if (null == walletExchange.getConfirmScore() || walletExchange.getConfirmScore() <= 0) {
                        return fail("无法完成转账！金额错误");
                    }
                    Integer isCheck=com.yanbao.util.ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.ISWXCHECKUSERNAME),1);
                    Boolean f=false;
                    if (isCheck==1){
                        f=true;
                    }
                    map= RefundsUtils.weixinCompanyPay(certificatPath, wechartAppId, wechartMuchId, walletExchange.getCardNo(), walletExchange.getOrderNo(), (int) (walletExchange.getConfirmScore() * 100), f, walletExchange.getRemark(),com.yanbao.util.ToolUtil.getRemoteAddr(request),user.getUserName());
                    if (map == null) {
                        return fail("微信企业付款未知异常");
                    }
                    if (map.containsKey("result_code") && "FAIL".equals(map.get("result_code"))) {
                    	//真实姓名不正确或无法给非实名用户付款
                    	if(map.containsKey("err_code") && ("NAME_MISMATCH".equals(map.get("err_code"))||"V2_ACCOUNT_SIMPLE_BAN".equals(map.get("err_code")))){
                    		 // 设置兑现失败
                            walletExchange.setStatus(4);
                            walletExchangeService.updateById(exchangeId, walletExchange);
                            // 插入流水表
                            WalletRecord walletRecord = new WalletRecord();
                            walletRecord.setUserId(walletExchange.getUserId());
                            walletRecord.setOrderNo(walletExchange.getOrderNo());
                            walletRecord.setScore(-walletExchange.getScore());

                            walletRecord.setRemark("兑换失败，余额退回");
                            walletRecord.setRecordType(RecordType.ROLLBACK.getCode());
                            walletRecordService.createWithUUID(walletRecord);
                            // 退回用户积分
                            userService.addScoreByUserId(walletExchange.getUserId(), -walletExchange.getScore());
                            // 发送兑换失败消息
                            Message message = new Message();
                            message.setId(UUIDUtil.getUUID());
                            message.setUserId(walletExchange.getUserId());
                            message.setOrderNo(walletExchange.getOrderNo());
                            message.setTitle("兑换失败");
                            message.setType(MessageType.EXCHANGE.getCode());
                            message.setDetail("兑换失败,微信提示:"+resultMap.get(map.get("err_code").toString())+",余额已退回用户钱包");
                            message.setRemark("兑换失败,微信提示:"+resultMap.get(map.get("err_code").toString())+",余额已退回用户钱包");
                            message.setStatus(0);
                            messageService.create(message);
                            return fail("兑换失败:"+resultMap.get(map.get("err_code").toString())+",余额已退回用户钱包");
                    	}else{
                    		return fail(resultMap.get(map.get("err_code").toString()));
                    	}
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    if (map == null) {
                        return fail("微信企业付款未知异常");
                    }
                    if (map.containsKey("result_code") && "FAIL".equals(map.get("result_code"))) {
                        return fail(resultMap.get(map.get("err_code").toString()));
                    }
                }
            } else {
                return fail("无法完成转账！微信证书不存在!");
            }
            // 设置兑现成功
            walletExchange.setStatus(3);
            walletExchangeService.updateById(exchangeId, walletExchange);
            // 发送兑换成功消息
            Message message = new Message();
            message.setId(UUIDUtil.getUUID());
            message.setUserId(walletExchange.getUserId());
            message.setOrderNo(walletExchange.getOrderNo());
            message.setTitle("兑换成功");
            message.setType(MessageType.EXCHANGE.getCode());
            message.setDetail("本次兑换余额:" + -walletExchange.getScore() + "，实际到账：" + walletExchange.getConfirmScore());
            message.setRemark(MessageType.EXCHANGE.getMsg());
            message.setStatus(0);
            messageService.create(message);
            // 提现手续费加到系统用户
            userService.addScoreByUserId("system", walletExchange.getPoundage());
            //提现成功，新增合伙人推荐商家业绩明细记录
            if (!com.yanbao.util.ToolUtil.isEmpty(user.getStoreId())) {
                //判断是否是商家提现，如果是，就进入合伙人推荐商家业绩结算
                partnerBillDetailService.createPartnerBillDetail(user.getStoreId(), walletExchange.getOrderNo(), walletExchange.getScore());
            }
            return success();
        }
        CashMoneyVo cashMoneyVo = new CashMoneyVo();
        cashMoneyVo.setCashId(walletExchange.getId());
        cashMoneyVo.setCashMoney(String.valueOf(walletExchange.getConfirmScore()));
        cashMoneyVo.setName(user.getUserName());
        cashMoneyVo.setTranId(walletExchange.getOrderNo());
        cashMoneyVo.setUserId(walletExchange.getUserId());
        cashMoneyVo.setBankCardNo(walletExchange.getCardNo());

        if (!StringUtils.isEmpty(walletExchange.getBankId())) {
            cashMoneyVo.setUnionPayNumber(walletExchange.getBankId());
        } else {
            return fail("提现失败，提现联行号不能为空!");
        }

        JSONObject json = JSONObject.fromObject(cashMoneyVo);
        String param = json.toString();

        // 定义对象
        Cash cashtemp = null;

        // 调用提现接口
        String url = "http://120.24.234.115:8090/pay/payCash/simplePay";

        try {
            cashtemp = HttpUtils.post(url, param);
            // 判断调用银行接口返回结果
            if (cashtemp == null) {
                return fail("提现失败，请检查参数");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("提示respMsg:" + cashtemp.getRespMsg());
        System.out.println("提示OrigRespCode:" + cashtemp.getOrigRespCode());

        if (cashtemp.getRespCode().equals("0000") || cashtemp.getRespCode().substring(0, 1).equals("1")) {

            // 设置兑现成功
            walletExchange.setStatus(3);
            walletExchangeService.updateById(exchangeId, walletExchange);
            // 发送兑换成功消息
            Message message = new Message();
            message.setId(UUIDUtil.getUUID());
            message.setUserId(walletExchange.getUserId());
            message.setOrderNo(walletExchange.getOrderNo());
            message.setTitle("兑换成功");
            message.setType(MessageType.EXCHANGE.getCode());
            message.setDetail("本次兑换余额:" + -walletExchange.getScore() + "，实际到账：" + walletExchange.getConfirmScore());
            message.setRemark(MessageType.EXCHANGE.getMsg());
            message.setStatus(0);
            messageService.create(message);
            // 提现手续费加到系统用户
            userService.addScoreByUserId("system", walletExchange.getPoundage());

            //提现成功，新增合伙人推荐商家业绩明细记录
            if (!com.yanbao.util.ToolUtil.isEmpty(user.getStoreId())) {
                //判断是否是商家提现，如果是，就进入合伙人推荐商家业绩结算
                partnerBillDetailService.createPartnerBillDetail(user.getStoreId(), walletExchange.getOrderNo(), walletExchange.getScore());
            }

            return success(cashtemp.getRespMsg());

        } else if (cashtemp.getRespCode().equals("2317") || cashtemp.getRespCode().equals("2332")) {
            //银行卡余额不足，返回信息，提示管理员充值余额，提现记录状态不变
            return fail(1466, "电子银行余额不足，请联系管理员");
        } else {
            // 设置兑现失败
            walletExchange.setStatus(4);
            walletExchangeService.updateById(exchangeId, walletExchange);
            // 插入流水表
            WalletRecord walletRecord = new WalletRecord();
            walletRecord.setUserId(walletExchange.getUserId());
            walletRecord.setOrderNo(walletExchange.getOrderNo());
            walletRecord.setScore(-walletExchange.getScore());

            walletRecord.setRemark("兑换失败，余额退回");
            walletRecord.setRecordType(RecordType.ROLLBACK.getCode());
            walletRecordService.createWithUUID(walletRecord);
            // 退回用户积分
            userService.addScoreByUserId(walletExchange.getUserId(), -walletExchange.getScore());
            // 发送兑换失败消息
            Message message = new Message();
            message.setId(UUIDUtil.getUUID());
            message.setUserId(walletExchange.getUserId());
            message.setOrderNo(walletExchange.getOrderNo());
            message.setTitle("兑换失败");
            message.setType(MessageType.EXCHANGE.getCode());
            message.setDetail("兑换失败，余额已退回您的钱包");
            message.setRemark("兑换失败");
            message.setStatus(0);
            messageService.create(message);
            return fail(cashtemp.getRespMsg());
        }
    }
}
