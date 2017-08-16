package com.yanbao.service;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.StoreCollect;

/**
 * 
 * @author zhuzh
 * @date 2017年1月11日
 */
public interface StoreCollectService {

	Boolean collect(String userId, String storeId) throws Exception;
	
	Boolean cancelCollect(String userId, String storeId) throws Exception;

	StoreCollect get(String userId, String storeId) throws Exception;

	Boolean isCollected(String userId, String storeId) throws Exception;

	PageResult<String> getPage(String userId, Page page) throws Exception;

}
