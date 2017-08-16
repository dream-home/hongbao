package com.yanbao.service;

import com.mall.model.HbSysKey;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface HbSysKeyService {
	void insert(HbSysKey hbSysKey) throws Exception;
	
	void delById(HbSysKey hbSysKey) throws Exception;
}
