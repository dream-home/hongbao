package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.GoodsDetailDao;
import com.yanbao.mapper.GoodsDetailMapper; 
import com.mall.model.GoodsDetail;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class GoodsDetailDaoImpl implements GoodsDetailDao {

	@Autowired
	private GoodsDetailMapper goodsDetailMapper;

	@Override
	public List<GoodsDetail> getList() throws Exception {
		return goodsDetailMapper.getList();
	}

	@Override
	public GoodsDetail getById(String id) throws Exception {
		return goodsDetailMapper.getById(id);
	}

	@Override
	public List<GoodsDetail> getByGoodsId(String id) throws Exception {
		return goodsDetailMapper.getByGoodsId(id,null);
	}

	@Override
	public List<GoodsDetail> getInLikeId(List<String> ids) throws Exception {
		return goodsDetailMapper.getInLikeId(ids);
	}

	@Override
	public Integer delId(String id) throws Exception {
		return goodsDetailMapper.delId(id);
	}

	@Override
	public Integer delInId(List<String> ids) throws Exception {
		return goodsDetailMapper.delInId(ids);
	}

	@Override
	public Integer add(GoodsDetail model) throws Exception {
		return goodsDetailMapper.add(model);
	}

	@Override
	public List<GoodsDetail> getPoList(GoodsDetail model) throws Exception {
		return goodsDetailMapper.getPoList(model);
	}

	@Override
	public List<GoodsDetail> getByGoodsId(String id, Integer status)
			throws Exception {
		return goodsDetailMapper.getByGoodsId(id,status);
	}


}
