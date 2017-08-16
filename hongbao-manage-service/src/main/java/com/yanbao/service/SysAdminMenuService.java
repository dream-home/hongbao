package com.yanbao.service;

import java.util.List;

import com.yanbao.core.service.CommonService;
import com.mall.model.SysAdminMenu;

public interface SysAdminMenuService extends CommonService<SysAdminMenu> {
	List<SysAdminMenu> readAllData(String id);

}
