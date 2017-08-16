package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.dao.AgreementDao;
import com.mall.model.Agreement;
import com.yanbao.service.AgreementService;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class AgreementServiceImpl implements AgreementService {

	@Autowired
	private AgreementDao agreementDao;

	@Override
	public Agreement get() throws Exception {
		return agreementDao.get();
	}

}
