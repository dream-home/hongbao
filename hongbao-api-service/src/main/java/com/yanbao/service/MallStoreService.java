package com.yanbao.service;

import com.mall.model.Goods;
import com.mall.model.GoodsIssue;
import com.mall.model.GoodsWin;
import com.mall.model.User;
import com.yanbao.vo.PurchaseVo;

/**
 * 
 * @author zhuzh
 * @date 2016年12月28日
 */
public interface MallStoreService {

	void drawHandler(User user, Goods goods, GoodsIssue issue) throws Exception;

	void purchaseHandler(User user, Goods goods, PurchaseVo vo) throws Exception;

	GoodsWin addOrderByH5(User user, Goods goods, PurchaseVo vo) throws Exception;

	void purchaseGoodsWinbyH5(String orderNo,String userid) throws Exception;
}
