package com.yanbao.dao;

import java.util.Date;
import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.GoodsWin;
import com.yanbao.vo.GoodsSalesVo;
import com.yanbao.vo.TopVo;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface GoodsWinDao {

	List<TopVo> getTopList(Integer num) throws Exception;

	GoodsWin getByOrderNo(String orderNo, String userId) throws Exception;

	GoodsWin getUserOrderByOrderNo(String orderNo) throws Exception;

	GoodsWin getStoreOrderByOrderNo(String orderNo, String storeId) throws Exception;

	Integer count(String userId, Integer status,Integer orderType,String storeId) throws Exception;

	List<GoodsWin> getList(String userId, Integer status, Page page,Integer orderType,String storeId) throws Exception;

	Integer add(GoodsWin model) throws Exception;

	Integer update(String id, GoodsWin model) throws Exception;

	Integer countStoreOrder(String storeId, Integer status) throws Exception;

	List<GoodsWin> getStoreOrderList(String storeId, Integer status, Page page) throws Exception;

	Integer countStoreSales(String storeId) throws Exception;

	List<GoodsSalesVo> countStoreGoodsSales(String storeId) throws Exception;

    GoodsWin getById(String goodsWinId) throws Exception;
    
    List<GoodsWin> getListByTime(Date starttime,Date endtime,Integer status,Integer payWay,String isShare);

	Double sumUserBuyAmt(String userId) throws Exception;
}
