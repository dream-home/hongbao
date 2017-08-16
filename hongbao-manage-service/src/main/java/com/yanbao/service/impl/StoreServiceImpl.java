package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.StoreDao;
import com.mall.model.Store;
import com.yanbao.service.StoreService;
import com.yanbao.vo.StoreVo;

/**
 * Created by summer on 2017-01-11:15:07;
 */
@Service
public class StoreServiceImpl extends CommonServiceImpl<Store> implements StoreService {

    @Autowired
    StoreDao storeDao;

    @Override
    protected CommonDao<Store> getDao() {
        return storeDao;
    }

    @Override
    protected Class<Store> getModelClass() {
        return Store.class;
    }

    @Override
    public List<StoreVo> readVoList(StoreVo storeVo, Page page) {
        return storeDao.readVoList(storeVo,page);
    }

    @Override
    public Integer readVoCount(StoreVo storeVo) {
        Integer count=storeDao.readVoCount(storeVo);
        return count==null?0:count;
    }
}
