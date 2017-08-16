package com.yanbao.mapper;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.ConsumeEPRecord;


/**
 * 
 * 
 * @author zyc 2017-03-06 15:23
 *
 */

public interface ConSumeEPRecordDao extends CommonDao<ConsumeEPRecord> {
	
	Double ConsumeEPSUM();
	
	int count();
	

}
