package com.yanbao.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.SysSettingDao;
import com.yanbao.mapper.SysSettingMapper;
import com.mall.model.SysSetting;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class SysSettingDaoImpl implements SysSettingDao {

	@Autowired
	private SysSettingMapper sysSettingMapper;

	@Override
	public SysSetting get() throws Exception {
		return sysSettingMapper.get();
	}


}
