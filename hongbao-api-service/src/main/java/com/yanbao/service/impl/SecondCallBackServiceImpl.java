package com.yanbao.service.impl;

import com.mall.model.SecondCallBack;
import com.yanbao.dao.SecondCallBackDao;
import com.yanbao.service.SecondCallBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2018年08月29日
 */
@Service
public class SecondCallBackServiceImpl implements SecondCallBackService {
    @Autowired
    private SecondCallBackDao secondCallBackDao;

    @Override
    public SecondCallBack getById(String id) throws Exception {
        return secondCallBackDao.getById(id);
    }
}
