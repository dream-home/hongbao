package com.yanbao.controller;

import com.yanbao.constant.BankCardType;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.service.*;
import com.yanbao.util.DateTimeUtil;
import com.yanbao.util.RandomUtil;
import com.yanbao.vo.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by summer on 2017-01-15:13:28;
 * 记录查询  积分交易记录 积分交易管理
 */
@Controller
@RequestMapping("/scoreManage")
public class ScoreTrandingRecordContrller extends BaseController {

    @Autowired
    WalletRechargeService walletRechargeService;
   
    
    @Autowired
    UserService userService;
    @Autowired
    WalletRecordService walletRecordService;
    @Autowired
    AdminService adminService;
    @Value("${systemSalt}")
    String salt;
    @Autowired
    WalletExchangeService walletExchangeService;
    @Autowired
    WalletDonateService walletDonateService;
    @Autowired
    GoodsWinService goodsWinService;
    @Autowired
    GoodsIssueDetailService goodsIssueDetailService;

    public static Map<String,String> sourceMap=new HashedMap();
    static {
        sourceMap.put("0","网银");
        sourceMap.put("1","支付宝");
        sourceMap.put("2","微信");
        sourceMap.put("3","苹果支付");
        sourceMap.put("4","手工充值");
    }


    /**
     * 会员充值购买积分的记录  会员充值购买积分的记录  积分充值记录  todo 订单下载 cvs
     *
     * @param rechargeVo
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/addScoreRecords")
    public JsonResult addScoreRecords(RechargeVo rechargeVo, Page page) {
        try {
            int count = walletRechargeService.getRechargeVoCount(rechargeVo);
            if (count == 0) {
                return fail("没有记录");
            }
            List<RechargeVo> list = walletRechargeService.getRechargeVoList(rechargeVo, page.getStartRow(), page.getPageSize());
            

            for(int i = 0; i<list.size();i++){
            	
            	list.get(i).setSourceName(BankCardType.fromCode(list.get(i).getSource()).getMsg());            

            }
            
            
            PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
            return success(pageResult);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return fail("数据库错误");
    }

    @RequestMapping("/extScoreRecords")
    public void extScoreRecords(RechargeVo rechargeVo, Page page, HttpServletResponse response) {
        String fileName = "积分充值统计" + RandomUtil.randomString(5) + ".csv";
        response.setContentType("application/CSV");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Pragma", "must-revalidate");
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write("记录编号,交易订单,购买会员,积分数量,支付方式,支付时间\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int count = walletRechargeService.getRechargeVoCount(rechargeVo);
            if (count == 0) {
                return;
            }
            List<RechargeVo> list = walletRechargeService.getRechargeVoList(rechargeVo, page.getStartRow(), page.getPageSize());
            String type = "";
            for (RechargeVo vo : list) {
                outputStream.write((vo.getId() + ",\t" + vo.getOrderNo().toString() +"," + vo.getUserName() + "," +vo.getScore()+","+sourceMap.get(vo.getSource()+"")
                        +"," +DateTimeUtil.formatDate(vo.getCreateTime(),DateTimeUtil.PATTERN_LONG)+"\r\n").getBytes());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * 记录查询  积分兑换记录 
     * zhengyc 2017-1-22 19:54
     * @param exchangevo
     * @param page
     * @param response
     */
    
    @RequestMapping("/extexchangeRecords")
    public void extexchangeRecords(ExchangeVo exchangevo, Page page, HttpServletResponse response) {
    	String fileName = "积分兑换统计" + RandomUtil.randomString(5) + ".csv";
        response.setContentType("application/CSV");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Pragma", "must-revalidate");
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write("记录编号,交易订单,兑换会员,积分数量,手续费,实际数量,银行卡账号,兑换时间\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int count = walletExchangeService.getExchangeVoCount(exchangevo);
            if (count == 0) {
                return;
            }
            List<ExchangeVo> list = walletExchangeService.getExchangeVoList(exchangevo, page.getStartRow(), page.getPageSize());
            String type = "";
           
            
            for (ExchangeVo vo : list) {
                outputStream.write((vo.getId() + ",\t" +  vo.getOrderNo()+"," + vo.getUserName() + "," +vo.getScore()+",\t" +vo.getPoundage()+",\t" +vo.getConfirmScore()+",\t"+vo.getCardNo()
                        +"," +DateTimeUtil.formatDate(vo.getCreateTime(),DateTimeUtil.PATTERN_LONG)+"\r\n").getBytes());
                //System.out.println("test"+new BigDecimal(vo.getOrderNo()).toString());
                outputStream.flush();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    

    /**
     * 积分兑换记录 提现记录
     *
     * @param exchangeVo
     * @param page
     * @return
     */
    @RequestMapping("/exchangeRecords")
    @ResponseBody
    public JsonResult exchangeRecords(ExchangeVo exchangeVo, Page page) {
        int count = walletExchangeService.getExchangeVoCount(exchangeVo);
        if (count == 0) {
            return fail("没有记录");
        }
        List list = walletExchangeService.getExchangeVoList(exchangeVo, page.getStartRow(), page.getPageSize());
        PageResult result = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return success(result);
    }
    
    
    
    
    
    
    
    
    
    

    /**
     * 积分互赠记录
     */
    @RequestMapping("/donateRecords")
    @ResponseBody
    public JsonResult donateRecords(WalletDonateVo donate, Page page) {
        int count = walletDonateService.readVoCount(donate);
        if (count == 0) {
            return fail("么有资源");
        }
        List<WalletDonateVo> walletDonates = walletDonateService.readVoList(donate, page);
        return success(getPageResult(page, count, walletDonates));

    }

    /**
     * 商品中奖购买记录
     *
     * @param goodsWin
     * @param page
     * @return
     */
    @RequestMapping("/winProducts")
    @ResponseBody
    public JsonResult winProducts(GoodsWinVo goodsWin, Page page) {
        //int count = goodsWinService.readCountWithTime(goodsWin, page);
    	int count = goodsWinService.readSaleCount(goodsWin, page);
    	
        if (count == 0) {
            return fail("没有资源");
        }
        List<GoodsWinVo> goodsWins = goodsWinService.comSaleList(goodsWin, page);
        //List<GoodsWinVo> goodsWins = goodsWinService.readListWithTime(goodsWin, page);
        return success(new PageResult(page.getPageNo(), page.getPageSize(), count, goodsWins));
    }

    /**
     * 委托出售记录
     *
     * @param goodsWin
     * @param page
     * @return
     */
    @RequestMapping("/productEntrustSell")
    @ResponseBody
    public JsonResult productEntrustSell(GoodsWinVo goodsWin, Page page) {
        int count = goodsWinService.readCountWithTime(goodsWin, page);
        if (count == 0) {
            return fail("没有资源");
        }
        List<GoodsWinVo> goodsWins = goodsWinService.readListWithTime(goodsWin, page);
        return success(new PageResult(page.getPageNo(), page.getPageSize(), count, goodsWins));
    }

    /**
     * 竞拍记录
     *
     * @param goodsIssueDetailVo
     * @param page
     * @return
     */
    @RequestMapping("/goodsIssueDetail")
    @ResponseBody
    public JsonResult goodsIssueDetail(GoodsIssueDetailVo goodsIssueDetailVo, Page page) {
        int count = goodsIssueDetailService.getGoodsIssueDetailVoCount(goodsIssueDetailVo);
        if (count == 0) {
            return fail("没有资源");
        }
        List<GoodsIssueDetailVo> goodsIssueDetailVos = goodsIssueDetailService.getGoodsIssueDetailVoList(goodsIssueDetailVo, page.getStartRow(), page.getPageSize());
        return success(new PageResult(page.getPageNo(), page.getPageSize(), count, goodsIssueDetailVos));
    }

    /**
     * 竞拍开奖记录
     *
     * @param goodsIssueDetailVo
     * @param page
     * @return
     */
    @RequestMapping("/winRecord")
    @ResponseBody
    public JsonResult winRecord(GoodsIssueDetailVo goodsIssueDetailVo, Page page) {
        int count = goodsIssueDetailService.getGoodsIssueDetailVoCount(goodsIssueDetailVo);
        if (count == 0) {
            return fail("没有资源");
        }
        List<GoodsIssueDetailVo> goodsIssueDetailVos = goodsIssueDetailService.getGoodsIssueDetailVoList(goodsIssueDetailVo, page.getStartRow(), page.getPageSize());
        return success(new PageResult(page.getPageNo(), page.getPageSize(), count, goodsIssueDetailVos));
    }


    /**
     * zyc 2017-07-04 扫码支付订单
     * @param rechargeVo
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/addScoreRecords2")
    public JsonResult addScoreRecords2(RechargeVo rechargeVo, Page page) {
        try {
            int count = walletRechargeService.getRechargeVoCount2(rechargeVo);
            if (count == 0) {
                return fail("没有记录");
            }
            List list = walletRechargeService.getRechargeVoList2(rechargeVo, page.getStartRow(), page.getPageSize());
            PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
            return success(pageResult);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return fail("数据库错误");
    }


}
