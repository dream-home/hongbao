package com.yanbao.service.impl;

import com.mall.model.SysSetting;
import com.yanbao.mapper.SysSettingMapper;
import com.yanbao.service.SysSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class SysSettingServiceImpl implements SysSettingService {

	@Autowired
	private SysSettingMapper sysSettingMapper;

	@Override
	public SysSetting get() throws Exception {
		return sysSettingMapper.get();
	}

}
