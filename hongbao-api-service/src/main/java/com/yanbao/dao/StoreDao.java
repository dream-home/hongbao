package com.yanbao.dao;

import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.Store;

/**
 * 
 * @author zhuzh
 * @date 2017年1月11日
 */
public interface StoreDao {

	Store getById(String id) throws Exception;

	Integer add(Store model) throws Exception;

	Integer update(String id, Store model) throws Exception;

	Integer updateCollectNum(String id, Integer num) throws Exception;

	List<Store> getCollectList(List<String> storeIds) throws Exception;

	Integer count(String location) throws Exception;

	List<Store> getList(Page page, String location) throws Exception;

	List<Store> getSimpleList(Page page, String location) throws Exception;

	List<Store> getListByIds(List<String> list) throws Exception;

	List<Store> getListByName(String searchWord, String location, Boolean isHasCoordinate) throws Exception;

	List<Store> getNewStore(Integer maxRow) throws Exception;
	
	List<Store> getCollectListByUserId(List<String> storeids) throws Exception;

	Integer updateSumSaleCount(String id, Integer num) throws Exception;
}
