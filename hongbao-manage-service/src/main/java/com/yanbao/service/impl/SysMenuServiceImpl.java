package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.SysMenuDao;
import com.mall.model.SysMenu;
import com.yanbao.service.SysMenuService;

@Service
public class SysMenuServiceImpl extends CommonServiceImpl<SysMenu> implements SysMenuService {
	@Autowired
	SysMenuDao sysMenuDao;
	
    @Override
    protected CommonDao<SysMenu> getDao() {
        return sysMenuDao;
    }

    @Override
    protected Class<SysMenu> getModelClass() {
        return SysMenu.class;
    }

	
	
	
}
