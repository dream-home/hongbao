package com.yanbao.service;

import java.util.List;

import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.mall.model.Store;
import com.yanbao.vo.DistanceVo;

/**
 * 
 * @author zhuzh
 * @date 2017年1月11日
 */
public interface StoreService {

	Store getById(String id) throws Exception;

	Integer add(Store model) throws Exception;

	Integer update(String id, Store model) throws Exception;

	Integer updateCollectNum(String id, Integer num) throws Exception;
	
	List<Store> getCollectList(List<String> storeIds) throws Exception;
	
	PageResult<Store> getPage(Page page, String location) throws Exception;

	PageResult<Store> getNewPage(Page page, DistanceVo distanceVo) throws Exception;

	public List<DistanceVo> getSimpleList(Page page, DistanceVo distanceVo) throws Exception;

	List<Store> getListByIds(List<String> list) throws Exception;
	
	List<Store> getListByName(String searchWord, String location,Boolean isHasCoordinate) throws Exception;

	List<Store> getNewStore(Integer maxRow) throws Exception;

	Integer updateSumSaleCount(String id, Integer num) throws Exception;


}
