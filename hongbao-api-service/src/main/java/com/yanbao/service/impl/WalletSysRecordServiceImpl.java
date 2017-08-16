package com.yanbao.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.constant.StatusType;
import com.yanbao.dao.WalletSysRecordDao;
import com.mall.model.WalletSysRecord;
import com.yanbao.service.WalletSysRecordService;
import com.yanbao.util.UUIDUtil;

/* 
 * 文件名：WalletSysRecordServiceImpl.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年3月6日
 * 版本号：v1.0
*/
@Service
public class WalletSysRecordServiceImpl implements WalletSysRecordService {
	
	@Autowired
	private WalletSysRecordDao walletSysRecordDao;

	@Override
	public Integer add(WalletSysRecord model) throws Exception {
		if(model == null){
			return null;
		}
		setDefaultValue(model);
		return walletSysRecordDao.add(model);
	}
	
	private void setDefaultValue(WalletSysRecord model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.TRUE.getCode());
		model.setCreateTime(new Date());
	}

}

