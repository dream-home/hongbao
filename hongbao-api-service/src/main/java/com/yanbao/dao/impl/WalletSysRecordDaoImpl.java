package com.yanbao.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.mapper.WalletSysRecordMapper;
import com.mall.model.WalletSysRecord;

/* 
 * 文件名：WalletSysRecordDao.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年3月6日
 * 版本号：v1.0
*/
@Repository
public class WalletSysRecordDaoImpl implements com.yanbao.dao.WalletSysRecordDao {
	
	@Autowired
	private WalletSysRecordMapper walletSysRecordMapper;

	@Override
	public Integer add(WalletSysRecord model) throws Exception {
		
		return walletSysRecordMapper.add(model);
	}

}

