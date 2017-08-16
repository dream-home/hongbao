package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.BankDao;
import com.mall.model.Bank;
import com.yanbao.service.BankService;

/**
 * Created by summer on 2016-12-14:17:55;
 */
@Service
public class BankServiceImpl extends CommonServiceImpl<Bank> implements BankService {

    @Autowired
    BankDao bankDao;

    @Override
    protected CommonDao<Bank> getDao() {
        return bankDao;
    }

    @Override
    protected Class<Bank> getModelClass() {
        return Bank.class;
    }
}
