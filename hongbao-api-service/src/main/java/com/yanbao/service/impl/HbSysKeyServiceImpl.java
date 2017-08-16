package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yanbao.dao.HbSysKeyDao;
import com.mall.model.HbSysKey;
import com.yanbao.service.HbSysKeyService;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class HbSysKeyServiceImpl implements HbSysKeyService {

	@Autowired
	private HbSysKeyDao hbSysKeyDao;

 
	@Override
	@Transactional
	public void insert(HbSysKey hbSysKey)throws Exception {
		hbSysKeyDao.insert(hbSysKey);
	}

	@Override
	public void delById(HbSysKey hbSysKey) throws Exception {
		hbSysKeyDao.del(hbSysKey);
		
	}

}
