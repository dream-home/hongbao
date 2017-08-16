package com.yanbao.dao.impl;

import com.mall.model.StoreCollect;
import com.yanbao.core.page.Page;
import com.yanbao.dao.StoreCollectDao;
import com.yanbao.mapper.StoreCollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class StoreCollectDaoImpl implements StoreCollectDao {

	@Autowired
	private StoreCollectMapper storeCollectMapper;

	@Override
	public Integer add(StoreCollect model) throws Exception {
		return storeCollectMapper.add(model);
	}

	@Override
	public Integer update(String id, StoreCollect model) throws Exception {
		return storeCollectMapper.update(id, model);
	}

	@Override
	public StoreCollect get(String userId, String storeId) throws Exception {
		return storeCollectMapper.get(userId, storeId);
	}

	@Override
	public Integer count(String userId) throws Exception {
		Integer count = null;
		count = storeCollectMapper.count(userId);
		/*String temp = Strings.get(RedisKey.USER_STORE_COLLECT.getKey() + userId);
		if (null != temp) {
			count = Integer.parseInt(temp);
		} else {
			count = storeCollectMapper.count(userId);
			Strings.setEx(RedisKey.USER_STORE_COLLECT.getKey() + userId, RedisKey.USER_STORE_COLLECT.getSeconds(), count.toString());
		}*/
		return count;
	}

	@Override
	public List<String> getList(String userId, Page page) throws Exception {
		return storeCollectMapper.getList(userId, page);
	}

}
