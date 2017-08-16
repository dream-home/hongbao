package com.yanbao.dao.impl;

import com.yanbao.core.page.Page;
import com.yanbao.dao.StoreDao;
import com.yanbao.mapper.StoreMapper;
import com.mall.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class StoreDaoImpl implements StoreDao {

	@Autowired
	private StoreMapper storeMapper;

	@Override
	public Store getById(String id) throws Exception {
		return storeMapper.getById(id);
	}

	@Override
	public Integer add(Store model) throws Exception {
		return storeMapper.add(model);
	}

	@Override
	public Integer update(String id, Store model) throws Exception {
		return storeMapper.update(id, model);
	}

	@Override
	public Integer updateCollectNum(String id, Integer num) throws Exception {
		return storeMapper.updateCollectNum(id, num);
	}

	@Override
	public List<Store> getCollectList(List<String> storeIds) throws Exception {
		return storeMapper.getCollectList(storeIds);
	}

	@Override
	public Integer count(String location) throws Exception {
		Integer count = null;
		count = storeMapper.count(location);
		/*String temp = Strings.get(RedisKey.STORE_COUNT.getKey());
		if (null != temp) {
			count = Integer.parseInt(temp);
		} else {
			count = storeMapper.count(location);
			Strings.setEx(RedisKey.STORE_COUNT.getKey(), RedisKey.STORE_COUNT.getSeconds(), count.toString());
		}*/
		return count;
	}

	@Override
	public List<Store> getList(Page page, String location) throws Exception {
		return storeMapper.getList(page,location);
	}

	@Override
	public List<Store> getListByName(String searchWord, String location, Boolean isHasCoordinate) throws Exception {
		return storeMapper.getListByName(searchWord,location,isHasCoordinate);
	}

	@Override
	public List<Store> getNewStore(Integer maxRow) throws Exception {
		return storeMapper.getNewStore(maxRow);
	}

	@Override
	public List<Store> getCollectListByUserId(List<String> storeids)
			throws Exception {
		return storeMapper.getCollectListByUserId(storeids);
	}

	@Override
	public Integer updateSumSaleCount(String id, Integer num) throws Exception {
		return storeMapper.updateSumSaleCount(id,num);
	}

	@Override
	public List<Store> getSimpleList(Page page, String location) throws Exception {
		return storeMapper.getSimpleList(page,location);
	}

	@Override
	public List<Store> getListByIds(List<String> list) throws Exception {
		return storeMapper.getListByIds(list);
	}
}
