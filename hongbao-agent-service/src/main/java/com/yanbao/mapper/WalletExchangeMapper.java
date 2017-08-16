package com.yanbao.mapper;

import com.mall.model.WalletExchange;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.vo.StoreExchangeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WalletExchangeMapper extends CommonDao<WalletExchange>{

	/**
	 * 根据userId分组查询商家提现记录
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	List<StoreExchangeRecord> getStoreList(@Param("starttime") Date starttime, @Param("endtime") Date endtime);

	/**
	 * 根据商家ID查询提现记录
	 * @param storeIds
	 * @return
	 */
	List<WalletExchange> getByStoreIds(@Param("storeIds")List<String> storeIds);
}
