package com.yanbao.service;

import com.mall.model.WalletExchange;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.core.service.CommonService;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.vo.StoreExchangeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 根据userId分钟查询商家提现记录
 * @author lxl
 * @date 2016年11月27日
 */
public interface WalletExchangeService extends CommonService<WalletExchange> {

	/**
	 * 根据userId分组查询商家提现记录
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	List<StoreExchangeRecord> getStoreList(Date starttime, Date endtime);
	
	/**
	 * 根据商家ID查询提现记录
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	List<WalletExchange> getByStoreIds(List<String> storeIds);
}
