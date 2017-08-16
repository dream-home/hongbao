package com.yanbao.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbao.constant.StatusType;
import com.yanbao.dao.BusinessEpRecordDao;
import com.mall.model.BusinessEpRecord;
import com.yanbao.service.BusinessEpRecordService;
import com.yanbao.util.UUIDUtil;

/* 
 * 文件名：BusinessEpRecordServiceImpl.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年3月3日
 * 版本号：v1.0
*/
@Service
public class BusinessEpRecordServiceImpl implements BusinessEpRecordService {
	
	@Autowired
	private BusinessEpRecordDao businessEpRecordDao;

	@Override
	@Transactional
	public Integer add(BusinessEpRecord model) throws Exception {
		if (model == null) {
			return null;
		}
		setDefaultValue(model);
		return businessEpRecordDao.add(model);
	}
	
	private void setDefaultValue(BusinessEpRecord model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.TRUE.getCode());
		model.setCreateTime(new Date());
	}

}

