package com.yanbao.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.HbSysKeyDao;
import com.yanbao.mapper.HbSysKeyMapper;
import com.mall.model.HbSysKey;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class HbSysKeyDaoImpl implements HbSysKeyDao {

	@Autowired
	private HbSysKeyMapper hbSysKeyMapper;

	@Override
	public void insert(HbSysKey hbSysKey) {
		hbSysKeyMapper.insert(hbSysKey);
	}
	@Override
	public void del(HbSysKey hbSysKey) {
		hbSysKeyMapper.delById(hbSysKey);
	};

}
