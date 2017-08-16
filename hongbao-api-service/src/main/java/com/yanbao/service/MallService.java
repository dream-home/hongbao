package com.yanbao.service;

import com.mall.model.Goods;
import com.mall.model.GoodsIssue;
import com.mall.model.User;
import com.yanbao.vo.PurchaseVo;

/**
 * 
 * @author zhuzh
 * @date 2016年12月28日
 */
public interface MallService {

	void drawHandler(User user, Goods goods, GoodsIssue issue) throws Exception;

	void purchaseHandler(User user, Goods goods, PurchaseVo vo) throws Exception;
	
	void epExchangeHandler(User user, Goods goods, PurchaseVo vo) throws Exception;
	
	public void finishDrawHandlerTest(Goods goods, GoodsIssue issue) throws Exception;
}

