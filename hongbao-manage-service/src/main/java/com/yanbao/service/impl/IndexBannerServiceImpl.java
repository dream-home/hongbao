package com.yanbao.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.IndexBannerDao;
import com.mall.model.IndexBanner;
import com.yanbao.service.IndexBannerService;

@Service
public class IndexBannerServiceImpl extends CommonServiceImpl<IndexBanner> implements IndexBannerService{

	@Autowired
    IndexBannerDao  indexBannerDao;

    @Override
    protected CommonDao<IndexBanner> getDao() {
        return indexBannerDao;
    }

    @Override
    protected Class<IndexBanner> getModelClass() {
        return IndexBanner.class;
    }

}
