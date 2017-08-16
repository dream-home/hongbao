package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.UserBankcardDao;
import com.mall.model.UserBankcard;
import com.yanbao.service.UserBankcardService;

/**
 * Created by summer on 2016-12-12:16:49;
 */
@Service
public class UserBankcardServiceImpl extends CommonServiceImpl<UserBankcard> implements UserBankcardService {

    @Autowired
    UserBankcardDao userBankcardDao;

    @Override
    protected CommonDao<UserBankcard> getDao() {
        return userBankcardDao;
    }

    @Override
    protected Class<UserBankcard> getModelClass() {
        return UserBankcard.class;
    }

    @Override
    public void updateByUserId(UserBankcard userBankcard) {
        userBankcardDao.updateByUserId(userBankcard);
    }
}
