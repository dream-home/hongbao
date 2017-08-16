package com.yanbao.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.City;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.SysCityMapper;
import com.yanbao.service.SysCityService;


@Service
public class SysCityServiceImpl extends CommonServiceImpl<City> implements SysCityService {
	
	@Autowired
	SysCityMapper sysCityDao;
	
	@Override
    protected CommonDao<City> getDao() {
        return sysCityDao;
    }

    @Override
    protected Class<City> getModelClass() {
        return City.class;
    }

	

}
