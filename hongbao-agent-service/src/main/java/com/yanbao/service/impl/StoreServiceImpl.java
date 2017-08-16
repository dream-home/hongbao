package com.yanbao.service.impl;

import com.mall.model.Store;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.StoreMapper;
import com.yanbao.service.StoreService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */
@Service
public class StoreServiceImpl extends CommonServiceImpl<Store> implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    @Override
    protected CommonDao<Store> getDao() {
        return storeMapper;
    }
    @Override
    protected Class<Store> getModelClass() {
        return Store.class;
    }

    /**
     * 代理区域商家列表
     * @param ids
     * @return
     * @throws Exception
     */
    public List<Store> getStoreList(List<String> ids,List<Integer> statusList,Page page) throws Exception{

        return storeMapper.getStoreList(ids,statusList,page);
    }

    @Override
    public Integer countStoreList(List<String> ids,List<Integer> statusList) throws Exception {
        return storeMapper.countStoreList(ids,statusList);
    }
	@Override
	public List<Store> getListByAreaId(List<String> areaIds, List<Integer> statusList,
			Integer startRow, Integer pageSize) throws Exception {
		return storeMapper.getListByAreaId(areaIds, statusList, startRow, pageSize,null);
	}
	@Override
	public List<Store> getListByAreaId(List<String> areaIds,
			List<Integer> statusList, Integer startRow, Integer pageSize,
			String searh) throws Exception {
		return storeMapper.getListByAreaId(areaIds, statusList, startRow, pageSize,searh);
	}


}
