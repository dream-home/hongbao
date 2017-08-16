package com.yanbao.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.dao.GoodsSortDao;
import com.mall.model.GoodsSort;
import com.yanbao.service.GoodsSortService;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class GoodsSortServiceImpl implements GoodsSortService {

	private static final Logger logger = LoggerFactory.getLogger(GoodsSortServiceImpl.class);

	@Autowired
	private GoodsSortDao goodsSortDao;

	@Override
	public List<GoodsSort> getList() throws Exception {
		return goodsSortDao.getList();
	}

	@Override
	public List<GoodsSort> getEpList() throws Exception {
		return goodsSortDao.getEpList();
	}

}
