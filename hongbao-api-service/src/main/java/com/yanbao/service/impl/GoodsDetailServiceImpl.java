package com.yanbao.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.dao.GoodsDetailDao;
import com.mall.model.GoodsDetail;
import com.yanbao.service.GoodsDetailService;

/**
 * 
 * @author ZHUZIHUI 
 * @date 2016年11月27日
 */
@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {

	private static final Logger logger = LoggerFactory.getLogger(GoodsDetailServiceImpl.class);

	@Autowired
	private GoodsDetailDao goodsDetailDao;

	@Override
	public List<GoodsDetail> getList() throws Exception {
		return goodsDetailDao.getList();
	}

	@Override
	public GoodsDetail getById(String id) throws Exception {
		return goodsDetailDao.getById(id);
	}

	@Override
	public List<GoodsDetail> getByGoodsId(String id) throws Exception {
		return goodsDetailDao.getByGoodsId(id);
	}

	@Override
	public List<GoodsDetail> getInLikeId(List<String> ids) throws Exception {
		return goodsDetailDao.getInLikeId(ids);
	}

	@Override
	public Integer delId(String id) throws Exception {
		return goodsDetailDao.delId(id);
	}

	@Override
	public Integer delInId(List<String> ids) throws Exception {
		return goodsDetailDao.delInId(ids);
	}

	@Override
	public Integer add(GoodsDetail model) throws Exception {
		return goodsDetailDao.add(model);
	}

	@Override
	public List<GoodsDetail> getPoList(GoodsDetail model) throws Exception {
		return goodsDetailDao.getPoList(model);
	}

	@Override
	public List<GoodsDetail> getByGoodsId(String id, Integer status)
			throws Exception {
		return goodsDetailDao.getByGoodsId(id, status);
	}


}
