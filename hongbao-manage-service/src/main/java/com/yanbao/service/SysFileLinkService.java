package com.yanbao.service;

import java.util.List;

import com.yanbao.core.service.CommonService;
import com.mall.model.SysFileLink;

/**
 * Created by summer on 2017-01-12:15:45;
 */
public interface SysFileLinkService extends CommonService<SysFileLink> {
	
	List<SysFileLink> readAllData(String id);
	
}
