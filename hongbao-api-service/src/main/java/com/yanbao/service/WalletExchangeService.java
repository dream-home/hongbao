package com.yanbao.service;

import com.mall.model.User;
import com.mall.model.UserBankcard;
import com.mall.model.WalletExchange;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface WalletExchangeService {

	WalletExchange getByOrderNo(String orderNo, String userId) throws Exception;

	PageResult<WalletExchange> getPage(String userId, Page page) throws Exception;

	Integer add(WalletExchange model) throws Exception;

	Integer update(String id, WalletExchange model) throws Exception;

	Boolean exchangeHandler(User user, Double score, UserBankcard bankcard) throws Exception;

	Boolean epToDouDouExchangeHandler(User user, Double ep) throws Exception;

	public Integer countCurrentDay(String userId) throws Exception;

	Boolean exchangeHandlerForWeiXin(User user, Double score) throws Exception;

}
