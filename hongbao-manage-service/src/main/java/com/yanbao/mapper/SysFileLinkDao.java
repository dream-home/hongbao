package com.yanbao.mapper;

import java.util.List;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.SysFileLink;

/**
 * Created by summer on 2017-01-12:15:44;
 */
public interface SysFileLinkDao extends CommonDao<SysFileLink> {
	
	List<SysFileLink> readAllData(String id);
	
}
