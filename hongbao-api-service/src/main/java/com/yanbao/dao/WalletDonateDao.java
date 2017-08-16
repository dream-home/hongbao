package com.yanbao.dao;

import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.WalletDonate;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface WalletDonateDao {

	WalletDonate getByOrderNo(String orderNo, String userId) throws Exception;

	Integer count(String userId) throws Exception;

	List<WalletDonate> getList(String userId, Page page) throws Exception;

	Integer add(WalletDonate model) throws Exception;

}
