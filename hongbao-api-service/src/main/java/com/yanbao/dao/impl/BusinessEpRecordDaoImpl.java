package com.yanbao.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.BusinessEpRecordDao;
import com.yanbao.mapper.BusinessEpRecordMapper;
import com.mall.model.BusinessEpRecord;

/* 
 * 文件名：BusinessEpRecordDaoImpl.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年3月3日
 * 版本号：v1.0
*/
@Repository
public class BusinessEpRecordDaoImpl implements BusinessEpRecordDao {
	
	@Autowired
	private BusinessEpRecordMapper businessEpRecordMapper;

	@Override
	public Integer add(BusinessEpRecord model) throws Exception {
		return businessEpRecordMapper.add(model);
	}

}

