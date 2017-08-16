package com.yanbao.dao;

import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.WalletExchange;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface WalletExchangeDao {

	WalletExchange getByOrderNo(String orderNo, String userId) throws Exception;

	Integer count(String userId) throws Exception;

	List<WalletExchange> getList(String userId, Page page) throws Exception;

	Integer add(WalletExchange model) throws Exception;

	Integer update(String id, WalletExchange model) throws Exception;

}
