package com.yanbao.mapper;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.SysRecord;

public interface SysRecordDao extends CommonDao<SysRecord> {
	
	double sysRecordSUM();
	double sysRecordRegister();

}
