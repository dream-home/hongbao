package com.yanbao.dao.impl;

import com.mall.model.Ad;
import com.mall.model.SecondCallBack;
import com.yanbao.dao.AdDao;
import com.yanbao.dao.SecondCallBackDao;
import com.yanbao.mapper.AdMapper;
import com.yanbao.mapper.SecondCallBackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class SecondCallBackImpl implements SecondCallBackDao {

	@Autowired
	private SecondCallBackMapper secondCallBackMapper;

	@Override
	public SecondCallBack getById(String id) {
		return secondCallBackMapper.getById(id);
	}
}
