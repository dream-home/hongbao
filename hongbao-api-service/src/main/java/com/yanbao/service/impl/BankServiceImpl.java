package com.yanbao.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.dao.BankDao;
import com.mall.model.Bank;
import com.yanbao.service.BankService;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class BankServiceImpl implements BankService {

	private static final Logger logger = LoggerFactory.getLogger(BankServiceImpl.class);

	@Autowired
	private BankDao bankDao;

	@Override
	public List<Bank> getList() throws Exception {
		return bankDao.getList();
	}

}
