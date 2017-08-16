package com.yanbao.service;

import java.util.List;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.GoodsWin;
import com.mall.model.User;
import com.yanbao.vo.GoodsSalesVo;
import com.yanbao.vo.TopVo;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface GoodsWinService {

	List<TopVo> getTopList(Integer num) throws Exception;

	GoodsWin getByOrderNo(String orderNo, String userId) throws Exception;

	GoodsWin getUserOrderByOrderNo(String orderNo) throws Exception;

	GoodsWin getStoreOrderByOrderNo(String orderNo, String storeId) throws Exception;

	PageResult<GoodsWin> getPage(String userId, Integer status, Page page,Integer orderType,String storeId) throws Exception;

	Integer add(GoodsWin model) throws Exception;

	Integer update(String id, GoodsWin model) throws Exception;

	PageResult<GoodsWin> getStoreOrderPage(String storeId, Integer status, Page page) throws Exception;

	Boolean saleHandler(User user, GoodsWin goodsWin) throws Exception;

	Boolean buyHandler(User user, GoodsWin goodsWin) throws Exception;

	Integer countStoreSales(String storeId) throws Exception;

	List<GoodsSalesVo> countStoreGoodsSales(String storeId) throws Exception;

	Integer count(String userId, Integer status) throws Exception;

	Integer count(String userId, Integer status,Integer orderType,String storeId) throws Exception;

	GoodsWin getById(String goodsWinId) throws Exception;

	Double sumUserBuyAmt(String userId) throws Exception;
}
