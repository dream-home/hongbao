package com.yanbao.mapper;

import java.util.List;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.SysAdminMenu;


public interface SysAdminMenuDao extends CommonDao<SysAdminMenu>{
	
	List<SysAdminMenu> readAllData(String id);

}
