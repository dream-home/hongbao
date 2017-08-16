package com.yanbao.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.PayDistributionDao;
import com.yanbao.mapper.PayDistributionMapper;
import com.mall.model.PayDistribution;

/* 
 * 文件名：付款分销dao类  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年4月6日
 * 版本号：v1.0
*/
@Repository
public class PayDistributionDaoImpl implements PayDistributionDao {
	
	@Autowired
	private PayDistributionMapper payDistributionMapper;

	/**
	 * 根据用户id查询付款分销记录
	 */
	@Override
	public PayDistribution getByUserId(String userId, Integer type) throws Exception {
		
		return payDistributionMapper.getByUserId(userId,type);
	}

	/**
	 * 新增付款分销记录
	 */
	@Override
	public Integer addPayDistribution(PayDistribution model) throws Exception {
		
		return payDistributionMapper.addPayDistribution(model);
	}

	/**
	 * 修改付款分销记录
	 */
	@Override
	public Integer updatePayDistribution(PayDistribution model) throws Exception {
		
		return payDistributionMapper.updatePayDistribution(model);
	}
}

