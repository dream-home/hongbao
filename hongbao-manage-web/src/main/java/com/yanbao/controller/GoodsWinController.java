package com.yanbao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.GoodsWin;
import com.mall.model.Message;
import com.mall.model.Store;
import com.yanbao.service.GoodsWinService;
import com.yanbao.service.MessageService;
import com.yanbao.service.StoreService;
import com.yanbao.vo.GoodsWinVo;

/**
 * Created by summer on 2016-12-23:17:12;
 *   获奖商品订单 发货订单
 */
@Controller
@RequestMapping("/win")
public class GoodsWinController extends BaseController {

    @Autowired
    GoodsWinService goodsWinService;
    @Autowired
    StoreService storeService;
    @Autowired
    MessageService messageService;


    @RequestMapping("/list")
    @ResponseBody
    public JsonResult WinList(GoodsWinVo goodsWin, Page page){
        int count=goodsWinService.readCountWithTime(goodsWin,page);
        if (count==0){
            return fail("没有资源");
        }
        List<GoodsWinVo> goodsWins=goodsWinService.readListWithTime(goodsWin,page);
        return success(new PageResult(page.getPageNo(),page.getPageSize(),count,goodsWins));
    }
    
    @RequestMapping("/list/store")
    @ResponseBody
    public JsonResult WinListByStore(GoodsWinVo goodsWin, Page page){
    	int count=goodsWinService.readCountWithTimeByStore(goodsWin,page);
    	if (count==0){
    		return fail("没有资源");
    	}
    	List<GoodsWinVo> goodsWins=goodsWinService.readListWithTimeByStore(goodsWin,page);
    	return success(new PageResult(page.getPageNo(),page.getPageSize(),count,goodsWins));
    }

    @ResponseBody
    @RequestMapping("update")
    public JsonResult updateGoodsWin(GoodsWin goodsWin){
        goodsWinService.updateById(goodsWin.getId(),goodsWin);
        return success();
    }

    /**
     * 提醒发货
     * @return
     */
    @ResponseBody
    @RequestMapping("/warnDeliver")
    public JsonResult warnDeliver(GoodsWin goodsWin){
        Store store=storeService.readById(goodsWin.getStoreId());
        if (store==null){
            return fail("商铺为空");
        }
        Message message=new Message();
        message.setTitle("发货提醒");
        message.setDetail("您有一个订单已付款,需要您及时发货!");
        message.setOrderNo(goodsWin.getOrderNo());
        message.setUserId(store.getUserId());
        message.setType(7);
        message.setStatus(0);
        message.setRemark(goodsWin.getRemark());
        messageService.createWithUUID(message);
        return success();
    }

}
