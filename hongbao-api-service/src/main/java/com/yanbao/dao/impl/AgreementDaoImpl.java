package com.yanbao.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.AgreementDao;
import com.yanbao.mapper.AgreementMapper;
import com.mall.model.Agreement;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class AgreementDaoImpl implements AgreementDao {

	@Autowired
	private AgreementMapper agreementMapper;

	@Override
	public Agreement get() throws Exception {
		return agreementMapper.get();
	}

}
