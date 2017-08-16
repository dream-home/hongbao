package com.yanbao.service;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.User;
import com.mall.model.WalletRecharge;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface WalletRechargeService {

	WalletRecharge getByOrderNo(String orderNo, String userId) throws Exception;

	WalletRecharge getUserOrderByOrderNo(String orderNo) throws Exception;

	PageResult<WalletRecharge> getPage(String userId, Page page) throws Exception;

	Integer add(WalletRecharge model) throws Exception;

	Integer update(String id, WalletRecharge model) throws Exception;

	Boolean rechargeHandler(User user, String orderNo) throws Exception;

	Boolean scanCodeHandler(User user, String orderNo) throws Exception;

	Boolean storeScanCodeHandler(User user, String orderNo) throws Exception;
	
	PageResult<WalletRecharge> getByscore(WalletRecharge wr, List<Integer> sources,Page page) throws Exception;
	
	Double statistics(WalletRecharge wr, List<Integer> sources,String nowDate) throws Exception;

	Boolean joinPartnerHandler(User user, String orderNo) throws Exception;

	Map<String,Double> countWalletMoney(User user,String storeUserId,Integer payType,Double Money) throws Exception;
}
