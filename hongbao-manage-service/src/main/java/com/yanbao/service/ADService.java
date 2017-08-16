package com.yanbao.service;

import com.yanbao.core.page.Page;
import com.yanbao.core.service.CommonService;
import com.mall.model.Ad;

import java.util.List;

/**
 * Created by summer on 2016-12-08:13:55;
 */
public interface ADService extends CommonService<Ad> {
	
	int readStartCount(Ad ad);
	List<Ad> readStartList(Ad ad,Page page);
	
}
