package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.SysAdminMenuDao;
import com.mall.model.SysAdminMenu;
import com.yanbao.service.SysAdminMenuService;

@Service
public class SysAdminMenuServiceImpl extends CommonServiceImpl<SysAdminMenu> implements SysAdminMenuService {
	 	@Autowired
	    SysAdminMenuDao sysAdminMenuDao;

	    @Override
	    protected CommonDao<SysAdminMenu> getDao() {
	        return sysAdminMenuDao;
	    }

	    @Override
	    protected Class<SysAdminMenu> getModelClass() {
	        return SysAdminMenu.class;
	    }
	    
	    @Override
	   public  List<SysAdminMenu> readAllData(String id){
	    	
	    	
	    return sysAdminMenuDao.readAllData(id);	
	    }
	
	
	

}
