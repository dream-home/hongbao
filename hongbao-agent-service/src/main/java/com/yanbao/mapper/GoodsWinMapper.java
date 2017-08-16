package com.yanbao.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mall.model.GoodsWin;
import com.mall.model.WalletExchange;
import com.yanbao.core.dao.CommonDao;

/**
 * Created by summer on 2016-12-14:10:05;
 */
public interface GoodsWinMapper extends CommonDao<GoodsWin>{
	/**
	 * 根据商家ID查询订单记录
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	List<GoodsWin> getByStoreIds(@Param("storeIds")List<String> storeIds);
}
