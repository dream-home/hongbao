package com.yanbao.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.constant.BankCardType;
import com.yanbao.constant.GoodsWinType;
import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.GoodsWin;
import com.yanbao.redis.Strings;
import com.yanbao.service.GoodsIssueDetailService;
import com.yanbao.service.GoodsWinService;
import com.yanbao.service.StoreService;
import com.yanbao.service.WalletDonateService;
import com.yanbao.vo.GoodsIssueDetailVo;
import com.yanbao.vo.GoodsWinVo;
import com.yanbao.vo.OrderVo;
import com.yanbao.vo.WalletDonateVo;

/**
 * Created by summer on 2017-01-13:16:14;
 *
 * 订单管理
 */
@Controller
@RequestMapping("/OrderManage")
public class OrderManageController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderManageController.class);

    @Autowired
    GoodsIssueDetailService goodsIssueDetailService;
    @Autowired
    GoodsWinService goodsWinService;
    @Autowired
    StoreService storeService;
    @Autowired
    WalletDonateService walletDonateService;

    
    
    /**
     * 竞拍订单管理
     *
     * @param goodsIssueDetailVo
     * @param page
     * @return
     */
    @RequestMapping("/goodsIssueDetailList")
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
     * 积分赠送订单
     *
     * @return
     */
    @RequestMapping("/donateRecords")
    @ResponseBody
    public JsonResult donateRecords(WalletDonateVo walletDonateVo, Page page){
        int count=walletDonateService.readVoCount(walletDonateVo);
        if (count==0){
            return  fail("没有资源");
        }
        List<WalletDonateVo> walletDonates=walletDonateService.readVoList(walletDonateVo,page);
        return success(getPageResult(page,count,walletDonates));

    }

    /**
     * 获奖商品订单
     *
     * @param goodsWinVo
     * @param page
     * @return
     */
    @RequestMapping("/winProducts")
    @ResponseBody
    public JsonResult winProducts(GoodsWinVo goodsWinVo, Page page) {
        int count = goodsWinService.readCountWithTime(goodsWinVo, page);
        if (count == 0) {
            return fail("没有资源");
        }
        List<GoodsWinVo> goodsWins = goodsWinService.readListWithTime(goodsWinVo, page);
        return success(new PageResult(page.getPageNo(), page.getPageSize(), count, goodsWins));
    }

    /**
     * 发货订单
     *
     * @param goodsWinVo
     * @param page
     * @return
     */
    @RequestMapping("/deleverProducts")
    @ResponseBody
    public JsonResult deleverProducts(GoodsWinVo goodsWinVo, Page page) {
        int count = goodsWinService.readCountWithTimeByStore(goodsWinVo, page);
        if (count == 0) {
            return fail("没有资源");
        }
        List<GoodsWinVo> goodsWins = goodsWinService.readListWithTimeByStore(goodsWinVo, page);
        for(GoodsWinVo goodsWin:goodsWins){
        	//System.out.println(goodsWin.getPayWay());
        	goodsWin.setBankTypeName(goodsWin.getPayWay() == null?null:BankCardType.fromCode(goodsWin.getPayWay()).getMsg());
        	
        }
        
        
        return success(new PageResult(page.getPageNo(), page.getPageSize(), count, goodsWins));

    }
    
    
    /**
     * 获取购物车的商品详情
     */
    
    @RequestMapping("/shoppGoodsList")
    @ResponseBody
    public JsonResult shoppGoodsList(String orderNo){

    	return success(goodsWinService.shoppGoodsList(orderNo));
    }
    
    
    
    
    
    
	/**
	 * 系统商家发货
	 */
	@ResponseBody
	@RequestMapping("/order/edit")
	public JsonResult OrderEdit(OrderVo vo) throws Exception {
		
		if (org.springframework.util.StringUtils.isEmpty(vo.getOrderNo())) {
			return new JsonResult(1, "订单号不存在，请检查参数");
		}
		
		//根据订单号查询发货订单
		GoodsWin goodWin = new GoodsWin();
		goodWin.setOrderNo(vo.getOrderNo());
		goodWin = goodsWinService.readOne(goodWin);
		if (goodWin == null) {
			return new JsonResult(2, "订单不存在");
		}
		
		if (goodWin.getStatus() != GoodsWinType.BUYED.getCode()) { 
			return new JsonResult(3, "订单已发货");
		} 
		if (StringUtils.isBlank(vo.getExpressNo()) || StringUtils.isBlank(vo.getExpressName())) {
			return new JsonResult(4, "请完善物流信息");
		}
		//防止重复提交
		String key=RedisKey.SYSTEM_EXPRESS_GOODS.getKey()+vo.getOrderNo();
		if (!org.springframework.util.StringUtils.isEmpty(Strings.get(key))) {
			return new JsonResult(5,"商品已发货，请不要重复提交");
		}
		Strings.setEx(RedisKey.SYSTEM_EXPRESS_GOODS.getKey()+vo.getOrderNo(),20,vo.getOrderNo());
		
		return goodsWinService.sendGoodService(vo,goodWin);
	}
	
    /**
     * 发货记录 下一版
     */
    // TODO: 2017/1/15 0015 summer 

}
