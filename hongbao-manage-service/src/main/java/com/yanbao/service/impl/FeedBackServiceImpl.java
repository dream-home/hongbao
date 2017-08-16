package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.FeedBackDao;
import com.mall.model.FeedBack;
import com.yanbao.service.FeedBackService;

/**
 * 
 * @author zyc 2017-06-29 11:42
 *
 */


@Service
public class FeedBackServiceImpl extends CommonServiceImpl<FeedBack> implements  FeedBackService{
	@Autowired
    FeedBackDao feedBackDao;

    @Override
    protected CommonDao<FeedBack> getDao() {
        return feedBackDao;
    }

    @Override
    protected Class<FeedBack> getModelClass() {
        return FeedBack.class;
    }

}
