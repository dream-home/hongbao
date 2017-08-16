package com.yanbao.dao.impl;

import com.yanbao.dao.FeedBackDao;
import com.yanbao.mapper.FeedBackMapper;
import com.mall.model.FeedBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author zzwei
 * @date 2017年06月27日
 */
@Repository
public class FeedBackDaoImpl implements FeedBackDao {

	@Autowired
	private FeedBackMapper feedBackMapper;

	@Override
	public FeedBack getById(String id) throws Exception {
		return feedBackMapper.getById(id);
	}

	@Override
	public Integer add(FeedBack model) throws Exception {
		return feedBackMapper.add(model);
	}


}
