package com.yanbao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.constant.OrderType;
import com.yanbao.core.page.JsonResult;
import com.yanbao.mapper.SysRecordDao;
import com.yanbao.mapper.WalletSignDao;
import com.mall.model.Goods;
import com.mall.model.GoodsIssue;
import com.mall.model.GoodsIssueDetail;
import com.mall.model.SysSetting;
import com.mall.model.User;
import com.mall.model.WalletExchange;
import com.mall.model.WalletRecharge;
import com.mall.model.WalletRecord;
import com.yanbao.service.ConSumeEPRecordService;
import com.yanbao.service.GoodsIssueDetailService;
import com.yanbao.service.GoodsIssueService;
import com.yanbao.service.GoodsService;
import com.yanbao.service.GoodsWinService;
import com.yanbao.service.SysSettingService;
import com.yanbao.service.UserService;
import com.yanbao.service.WalletDonateService;
import com.yanbao.service.WalletExchangeService;
import com.yanbao.service.WalletRechargeService;
import com.yanbao.service.WalletRecordService;
import com.yanbao.vo.FinancialStatistics;
import com.yanbao.vo.SysStatistics;



/**
 * Created by summer on 2016-12-08:16:31;
 *
 *   数据库统计
 *   系统收入 充值 提现 赠送 积分购买  系统维护费
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    WalletRecordService walletRecordService;
    @Autowired
    GoodsIssueService goodsIssueService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    WalletExchangeService walletExchangeService;
    @Autowired
    WalletRechargeService walletRechargeService;
    @Autowired
    WalletDonateService walletDonateService;
    @Autowired
    SysSettingService sysSettingService;
    @Autowired
    GoodsWinService goodsWinService;
    @Autowired
    GoodsIssueDetailService goodsIssueDetailService;
    @Autowired
    ConSumeEPRecordService conSumeEPRecordService;
    @Autowired
    WalletSignDao walletSignDao;
    @Autowired
    SysRecordDao sysRecordDao;


    /**
     *  全站信息统计 其中积分收入只包括平台交易税率收入 未包括商品本身的利润
     *  统计包括: 会员总数  积分收入总数 竞拍开奖总期数 参与竞拍人次 商品发布数量
     * @return
     */
    @RequestMapping("/sysDetail")
    @ResponseBody
    public  JsonResult detail(){
        //会员数量
        User user=new User();
        user.setStatus(1);
        int userCount=userService.readCount(user);
        //竞拍开奖总期数
        GoodsIssue goodsIssue=new GoodsIssue();
        int issueCount=goodsIssueService.readCount(goodsIssue);
        //参与竞拍人次
        GoodsIssueDetail goodsIssueDetail=new GoodsIssueDetail();
        int personTime=goodsIssueDetailService.readCount(goodsIssueDetail);
        //商品发布数量
        Goods goods=new Goods();
        goods.setStatus(1);
        int goodsCount=goodsService.readCount(goods);
        //系统积分总收入 转赠 充值 提现 积分购买 系统抽取商家费用
        SysSetting sysSetting=new SysSetting();
        sysSetting.setStatus(1);
        sysSetting=sysSettingService.readOne(sysSetting);
        //转赠手续费
        double donateIncome=walletDonateService.getIncome();
        //充值手续费
        double rechargeIncome= walletRechargeService.getIncome();
        //提现手续费
        double exchangeIncome=walletExchangeService.getIncome();
        //积分购买
        double sellIncome=goodsWinService.getIncome();
        
        
        
        
        
        //系统维护费
        WalletRecord walletRecord=new WalletRecord();
        walletRecord.setStatus(1);
        walletRecord.setRecordType(9);
        double protectionFee=walletRecordService.getSomeFee(walletRecord);
        //系统获取的分销费用
        walletRecord.setRecordType(8);
        walletRecord.setUserId("system");
        double shareFee=walletRecordService.getSomeFee(walletRecord);
        //系统收益
        double total=donateIncome+rechargeIncome+exchangeIncome+sellIncome+protectionFee+shareFee;
        //手工充值数额
        WalletRecharge walletRecharge=new WalletRecharge();
        walletRecharge.setSource(4);
        
      
        double handworkScore=walletRechargeService.getSomeSum(walletRecharge);
        
        

        SysStatistics statistics=new SysStatistics();
        statistics.setGoodsCount(goodsCount);
        statistics.setIssueCount(issueCount);
        statistics.setPointCount(total);
        statistics.setUserCount(userCount);
        statistics.setUserIssueCount(personTime);
        statistics.setHandworkScore(handworkScore);
        return success(statistics);
    }

    @ResponseBody
    @RequestMapping("/financialDetail")
    public JsonResult getDetail(){
        //转赠手续费
        double donateIncome=walletDonateService.getIncome();
        //充值手续费
        double rechargeIncome= walletRechargeService.getIncome();
        //提现手续费
        double exchangeIncome=walletExchangeService.getIncome();
        //积分购买
        double sellIncome=goodsWinService.getIncome();
        //系统维护费
        WalletRecord walletRecord=new WalletRecord();
        walletRecord.setStatus(1);
        walletRecord.setRecordType(9);
        walletRecord.setUserId("system");
        double protectionFee=walletRecordService.getSomeFee(walletRecord);
        //系统获取的分销费用
        walletRecord.setRecordType(8);
        walletRecord.setUserId("system");
        double shareFee=walletRecordService.getSomeFee(walletRecord);
        
      //系统抽取商家费用
        WalletRecord walletRecord1 = new WalletRecord();
        walletRecord1.setRecordType(22);
        walletRecord1.setStatus(1);
        double systemExpenses = -walletRecordService.getSomeFee(walletRecord1); 
        
        
        
        
        //系统收益
        double total=donateIncome+rechargeIncome+exchangeIncome+sellIncome+protectionFee+shareFee+systemExpenses;
        
        //充值订单数量 金额
        WalletRecharge walletRecharge=new WalletRecharge();
        walletRecharge.setStatus(2);
        int rechargeCount=walletRechargeService.readCount(walletRecharge);
        double rechargeMoneyCount=walletRechargeService.getSomeSum(walletRecharge);
        
        
        
        
        
        
        
        
        //面对面支付充值数量金额
        WalletRecharge walletRecharge1=new WalletRecharge();
        walletRecharge1.setStatus(3);
        int rechargeCount1=walletRechargeService.readCount(walletRecharge1);
        double rechargeMoneyCount1=walletRechargeService.getSomeSum(walletRecharge1);
        
        
        
        
        
        //提现订单数量 金额
        WalletExchange walletExchange=new WalletExchange();
        walletExchange.setStatus(3);
        int exchangeCount=walletExchangeService.readCount(walletExchange);
        double exchangeMoneyCount=walletExchangeService.getSomeSum(walletExchange);

        //未审核提现订单
        walletExchange.setStatus(0);
        int unCheckExchagne=walletExchangeService.readCount(walletExchange);
        
       //会员ep总数 
        double getexchangeEP = userService.getExchangeEP();
        
        //系统ep收入
        double conSumeEPSUM = conSumeEPRecordService.ConSumeEPSUM();
      
        //兑换商品数量
        //int conSumeCount = conSumeEPRecordService.count();
        Integer conSumeCount = goodsWinService.count(OrderType.EXCHANGEEP.getCode());
        if(null==conSumeCount){
        	conSumeCount=0;
        }
        
        
        //系统ep支出  用户签到+绑定手机号
        
        double signSUM = walletSignDao.signSUM()+sysRecordDao.sysRecordRegister();
   
        
        
        //积分转换ep
        double sysRecordSUM = sysRecordDao.sysRecordSUM();
        
        //用户总余额
        
        double scoreSUM = userService.getScoreSUM();
        
        
        //系统账户积分余额
        double sysScoreBalance = userService.sysScoreBalance();
        
        
        //系统账户ep余额
        double sysEPBalance = userService.sysEPBalance();
        
        
        
        
        //System.out.println(getexchangeEP);
        
        FinancialStatistics statistics=new FinancialStatistics();
        statistics.setExchangeCount(exchangeCount);
        statistics.setExchangeScoreCount(exchangeMoneyCount);
        statistics.setRechargeCount(rechargeCount+rechargeCount1);
        statistics.setRechaScoreCount(rechargeMoneyCount+rechargeMoneyCount1);
        statistics.setTotalProfit(total);
        statistics.setUnCheckCount(unCheckExchagne);
        statistics.setExchangeEP(getexchangeEP);
        statistics.setConSumeCount(conSumeCount);
        statistics.setConSumeEPSUM(conSumeEPSUM);
        statistics.setSign(signSUM);
        statistics.setSysRecord(sysRecordSUM);   
        statistics.setScoreSUM(scoreSUM);
        statistics.setSysScoreBalance(sysScoreBalance);
        statistics.setSysEPBalance(sysEPBalance);
        	
        return  success(statistics);

    }



}
