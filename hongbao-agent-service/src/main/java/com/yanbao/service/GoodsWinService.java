package com.yanbao.service;

import java.util.List;

import com.mall.model.GoodsWin;
import com.mall.model.WalletExchange;
import com.yanbao.core.service.CommonService;

/**
 * Created by summer on 2016-12-14:10:05;
 */
public interface GoodsWinService extends CommonService<GoodsWin> {
	/**
	 * 根据商家ID查询订单记录
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	List<GoodsWin> getByStoreIds(List<String> storeIds);
    
}
