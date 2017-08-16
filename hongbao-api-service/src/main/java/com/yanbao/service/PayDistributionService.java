package com.yanbao.service;

import com.mall.model.PayDistribution;

/* 
 * 文件名：付款分销业务接口类  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年4月6日
 * 版本号：v1.0
*/
public interface PayDistributionService {
	
	/**
	 * 根据会员id查询付款分销记录
	 * @param userId
	 * @param type
     * @return
	 */
	public PayDistribution getByUserId(String userId, Integer type) throws Exception;
	
	/**
	 * 新增付款分销
	 * @param model
	 * @return
	 */
	public Integer addPayDistribution(PayDistribution model) throws Exception;
	
	/**
	 * 保存付款分销
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Integer updatePayDistribution(PayDistribution model) throws Exception;

}

