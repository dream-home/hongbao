package com.yanbao.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.dao.PayDistributionDao;
import com.mall.model.PayDistribution;
import com.yanbao.service.PayDistributionService;
import com.yanbao.util.UUIDUtil;

/* 
 * 文件名：付款分销业务类  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年4月6日
 * 版本号：v1.0
*/
@Service
public class PayDistributionServiceImpl implements PayDistributionService {
	
	@Autowired
	private PayDistributionDao payDistributionDao;
	
	/**
	 * 根据用户id查询付款分销记录
	 */
	@Override
	public PayDistribution getByUserId(String userId, Integer type) throws Exception {
		if(StringUtils.isEmpty(userId)){
			return null;
		}
		
		return payDistributionDao.getByUserId(userId,type);
	}

	/**
	 * 新增付款分销记录
	 */
	@Override
	public Integer addPayDistribution(PayDistribution model) throws Exception {
		if(model == null){
			return null;
		}
		
		setDefaultValue(model);
		return payDistributionDao.addPayDistribution(model);
	}
	
	private void setDefaultValue(PayDistribution model) {
		model.setId(UUIDUtil.getUUID());
		model.setCreateTime(new Date());
		model.setStatus(1);
		model.setRemark("付款分销");
	}

	/**
	 * 修改付款分销记录
	 */
	@Override
	public Integer updatePayDistribution(PayDistribution model) throws Exception {
		if (model == null) {
			return 0;
		}
		
		model.setUpdateTime(new Date());
		return payDistributionDao.updatePayDistribution(model);
	}
}

