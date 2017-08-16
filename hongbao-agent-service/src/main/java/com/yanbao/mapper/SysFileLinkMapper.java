package com.yanbao.mapper;

import java.util.List;

import com.mall.model.SysFileLink;
import com.yanbao.core.dao.CommonDao;

/**
 * Created by summer on 2017-01-12:15:44;
 */
public interface SysFileLinkMapper extends CommonDao<SysFileLink> {
	
	List<SysFileLink> readAllData(String id);
	
}
