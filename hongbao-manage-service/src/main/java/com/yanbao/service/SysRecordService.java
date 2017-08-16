package com.yanbao.service;

import com.yanbao.core.service.CommonService;
import com.mall.model.SysRecord;

/**
 * 
 * @author zyc 207-03-06 17:22
 * 
 *
 */
public interface SysRecordService extends CommonService<SysRecord> {
	double sysRecordSUM();
	double sysRecordRegister();
}
