package com.yanbao.dao;

import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.StoreCollect;

/**
 * 
 * @author zhuzh
 * @date 2017年1月11日
 */
public interface StoreCollectDao {

	Integer add(StoreCollect model) throws Exception;

	Integer update(String id, StoreCollect model) throws Exception;

	StoreCollect get(String userId, String storeId) throws Exception;

	Integer count(String userId) throws Exception;

	List<String> getList(String userId, Page page) throws Exception;
}
