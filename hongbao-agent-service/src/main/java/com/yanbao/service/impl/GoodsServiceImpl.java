package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.Goods;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.GoodsMapper;
import com.yanbao.service.GoodsService;

/**
 * Created by summer on 2016-12-08:15:25;
 */
@Service
public class GoodsServiceImpl extends CommonServiceImpl<Goods> implements GoodsService {

    @Autowired
    GoodsMapper goodsDao;

    @Override
    protected CommonDao<Goods> getDao() {
        return goodsDao;
    }

    @Override
    protected Class<Goods> getModelClass() {
        return Goods.class;
    }
    
    @Override
	public List<Goods> readListWithTime(Goods goods, Page page) {
		return goodsDao.readListWithTime(goods, page);
	}
    
    
    @Override
	public void proDown(Goods goods) {
		 goodsDao.proDown(goods);
	}

	@Override
	public List<Goods> readByStoreId(List<String> storeIds, Integer startRow,
			Integer pageSize) {
		return goodsDao.readByStoreId(storeIds, startRow, pageSize);
	}
    
}
