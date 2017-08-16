package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.AdminDao;
import com.mall.model.Admin;
import com.yanbao.service.AdminService;

/**
 * Created by summer on 2016-12-05:16:40;
 */
@Service
public class AdminServiceImpl extends CommonServiceImpl<Admin> implements AdminService {

    @Autowired
    AdminDao adminDao;

    @Override
    protected CommonDao<Admin> getDao() {
        return adminDao;
    }

    @Override
    protected Class<Admin> getModelClass() {
        return Admin.class;
    }

    @Override
    public Admin getByNickName(String nickeName) {
        return adminDao.getByNickName(nickeName);
    }
}
