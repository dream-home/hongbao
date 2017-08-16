package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.WalletSysRecord;

/* 
 * 文件名：WalletSysRecordMapper.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：lxl 
 * 创建时间：2017年3月6日
 * 版本号：v1.0
*/
@Repository
public interface WalletSysRecordMapper {

	Integer add(@Param("model") WalletSysRecord model);
	
}

