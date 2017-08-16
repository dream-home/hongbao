package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.dao.SysSettingDao;
import com.mall.model.SysSetting;
import com.yanbao.service.SysSettingService;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class SysSettingServiceImpl implements SysSettingService {

	@Autowired
	private SysSettingDao sysSettingDao;

	@Override
	public SysSetting get() throws Exception {
		return sysSettingDao.get();
	}

}
