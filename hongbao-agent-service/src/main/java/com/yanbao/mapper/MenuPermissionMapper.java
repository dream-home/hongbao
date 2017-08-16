package com.yanbao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mall.model.MenuPermission;
import com.yanbao.core.dao.CommonDao;

@Repository
public interface MenuPermissionMapper extends CommonDao<MenuPermission>{
	
	List<MenuPermission> getPoList(@Param("model") MenuPermission model);
	
	Integer addPo(@Param("model") MenuPermission model);
}
