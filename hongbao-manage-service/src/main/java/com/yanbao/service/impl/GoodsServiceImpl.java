package com.yanbao.service.impl;

import com.mall.model.Goods;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.GoodsDao;
import com.yanbao.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by summer on 2016-12-08:15:25;
 */
@Service
public class GoodsServiceImpl extends CommonServiceImpl<Goods> implements GoodsService {

    @Autowired
    GoodsDao goodsDao;

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
	public int updateSort(String originalType, String newType,
			List<String> goodsids) {
		return goodsDao.updateSort(originalType, newType, goodsids);
	}
    
}
