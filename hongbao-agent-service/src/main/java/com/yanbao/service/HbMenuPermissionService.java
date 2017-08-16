package com.yanbao.service;

import java.util.List;
import java.util.Map;

import com.mall.model.MenuPermission;
import com.yanbao.core.service.CommonService;

/**
 * @author Pay - 1091945691@qq.com
 */
public interface HbMenuPermissionService extends CommonService<MenuPermission>{
	
	List<MenuPermission> getPoList(MenuPermission model);
	
	List<MenuPermission> getAll();
	
	Map<String,MenuPermission> getAllByMap();
	
	Integer addPo(MenuPermission model);
}