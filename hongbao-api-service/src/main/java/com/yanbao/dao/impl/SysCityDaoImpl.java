package com.yanbao.dao.impl;

import com.mall.model.City;
import com.yanbao.dao.SysCityDao;
import com.yanbao.mapper.SysCityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author jay.zheng
 * @date 2017年7月14日
 */
@Repository
public class SysCityDaoImpl implements SysCityDao {

	@Autowired
	private SysCityMapper sysCityMapper;

	@Override
	public City readById(String id) throws Exception {
		return sysCityMapper.readById(id);
	}

	@Override
	public City readOne(City model) throws Exception {
		return sysCityMapper.readOne(model);
	}

	@Override
	public List<City> readList(City model, int startRow, int pageSize) throws Exception {
		return sysCityMapper.readList(model,startRow,pageSize);
	}

	@Override
	public Integer readCount(City model) throws Exception {
		return sysCityMapper.readCount(model);
	}

	@Override
	public List<City> getAllCity(String field) throws Exception {
		return sysCityMapper.getAllCity(field);
	}
}
