package com.yanbao.service;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.User;
import com.mall.model.WalletDonate;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface WalletDonateService {

	WalletDonate getByOrderNo(String orderNo, String userId) throws Exception;

	PageResult<WalletDonate> getPage(String userId, Page page) throws Exception;

	Integer add(WalletDonate model) throws Exception;

	Boolean donateHandler(User user, User donateUser, Double score) throws Exception;

}
