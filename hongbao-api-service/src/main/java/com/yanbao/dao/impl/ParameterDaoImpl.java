package com.yanbao.dao.impl;

import com.mall.model.Parameter;
import com.yanbao.dao.ParameterDao;
import com.yanbao.mapper.ParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class ParameterDaoImpl implements ParameterDao {

	@Autowired
	private ParameterMapper parameterMapper;

	@Override
	public List<Parameter> getList() throws Exception {
		return parameterMapper.getList();
	}
}
