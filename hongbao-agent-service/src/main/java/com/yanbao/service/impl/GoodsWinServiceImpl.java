package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.GoodsWin;
import com.mall.model.WalletExchange;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.GoodsWinMapper;
import com.yanbao.service.GoodsWinService;

/**
 * Created by summer on 2016-12-14:10:06;
 */
@Service
public class GoodsWinServiceImpl extends CommonServiceImpl<GoodsWin> implements GoodsWinService {

	@Autowired
	GoodsWinMapper goodsWinDao;

	@Override
	protected CommonDao<GoodsWin> getDao() {
		return goodsWinDao;
	}

	@Override
	protected Class<GoodsWin> getModelClass() {
		return GoodsWin.class;
	}

	@Override
	public List<GoodsWin> getByStoreIds(List<String> storeIds) {
		return goodsWinDao.getByStoreIds(storeIds);
	}
	
}
