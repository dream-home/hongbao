package com.yanbao.service.impl;

import com.yanbao.dao.IndexBannersDao;
import com.mall.model.IndexBanner;
import com.yanbao.service.IndexBannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2017年05月18日
 */
@Service
public class IndexBannerServiceImpl implements IndexBannerService {

    private static final Logger logger = LoggerFactory.getLogger(IndexBannerServiceImpl.class);

    @Autowired
    private IndexBannersDao indexBannersDao;

    @Override
    public List<IndexBanner> getIndexBannerList() throws Exception {
        return indexBannersDao.getList();
    }

}
