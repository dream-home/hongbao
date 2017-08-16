package com.yanbao.dao;

import java.util.Date;
import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.WalletRecharge;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface WalletRechargeDao {

	WalletRecharge getByOrderNo(String orderNo, String userId) throws Exception;

	WalletRecharge getUserOrderByOrderNo(String orderNo) throws Exception;

	Integer count(String userId) throws Exception;

	List<WalletRecharge> getList(String userId, Page page) throws Exception;

	Integer add(WalletRecharge model) throws Exception;

	Integer update(String id, WalletRecharge model) throws Exception;
	
	List<WalletRecharge> getByscore(WalletRecharge wr, List<Integer> sources,Page page) throws Exception;
	
	Integer countByscore(WalletRecharge wr, List<Integer> sources) throws Exception;
	
	Double statistics(WalletRecharge wr, List<Integer> sources,String nowDate) throws Exception;
	
	List<WalletRecharge> getListByTime(Date starttime,Date endtime,Integer status,Integer source);
}
