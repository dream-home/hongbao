package com.yanbao.service;

import java.util.List;

import com.yanbao.core.page.JsonResult;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.mall.model.GoodsWin;
import com.mall.model.GoodsWinDetail;
import com.yanbao.vo.GoodsWinVo;
import com.yanbao.vo.OrderVo;

/**
 * Created by summer on 2016-12-14:10:05;
 */
public interface GoodsWinService extends CommonService<GoodsWin> {
    //用户购买商品的收入
    double getIncome();

    List<GoodsWinVo> readListWithTime(GoodsWinVo goodsWinVo, Page page);

    
    List<GoodsWinVo> comSaleList(GoodsWinVo goodsWinVo, Page page);
    int readSaleCount(GoodsWinVo goodsWinVo, Page page);
    int readCountWithTime(GoodsWinVo goodsWinVo, Page page);
    
    List<GoodsWinVo> readListWithTimeByStore(GoodsWinVo goodsWinVo, Page page);
    
    int readCountWithTimeByStore(GoodsWinVo goodsWinVo, Page page);
    
    /**
     * 发货订单业务
     * @param vo
     * @param goodWin
     * @return
     */
    public JsonResult sendGoodService(OrderVo vo,GoodsWin goodWin) throws Exception;
    
    
    List<GoodsWinDetail> shoppGoodsList(String orderNo);
    
    Integer count(Integer orderType);
}
