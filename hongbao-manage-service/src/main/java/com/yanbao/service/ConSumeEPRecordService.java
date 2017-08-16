package com.yanbao.service;

import com.yanbao.core.service.CommonService;
import com.mall.model.ConsumeEPRecord;

public interface ConSumeEPRecordService  extends CommonService<ConsumeEPRecord> {
	double ConSumeEPSUM();
	int count();

}
