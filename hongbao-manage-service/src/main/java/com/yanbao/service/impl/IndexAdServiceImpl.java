package com.yanbao.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.IndexAdDao;
import com.mall.model.IndexAd;
import com.yanbao.service.IndexAdService;

@Service
public class IndexAdServiceImpl extends CommonServiceImpl<IndexAd> implements IndexAdService {
	@Autowired
    IndexAdDao indexAdDao;

    @Override
    protected CommonDao<IndexAd> getDao() {
        return indexAdDao;
    }

    @Override
    protected Class<IndexAd> getModelClass() {
        return IndexAd.class;
    }
    
    
    
	

}
