package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.GoodsIssueDao;
import com.yanbao.mapper.GoodsIssueMapper;
import com.mall.model.GoodsIssue;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class GoodsIssueDaoImpl implements GoodsIssueDao {

	@Autowired
	private GoodsIssueMapper goodsIssueMapper;

	@Override
	public GoodsIssue getById(String id) throws Exception {
		return goodsIssueMapper.getById(id);
	}

	@Override
	public Integer add(GoodsIssue model) throws Exception {
		return goodsIssueMapper.add(model);
	}

	@Override
	public Integer update(String id, GoodsIssue model) throws Exception {
		return goodsIssueMapper.update(id, model);
	}

	@Override
	public Integer updateCurNum(String id) throws Exception {
		return goodsIssueMapper.updateCurNum(id);
	}
	
	@Override
	public List<GoodsIssue> getTimeoutList(String time) throws Exception {
		return goodsIssueMapper.getTimeoutList(time);
	}

}
