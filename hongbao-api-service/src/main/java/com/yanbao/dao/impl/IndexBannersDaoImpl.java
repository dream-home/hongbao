package com.yanbao.dao.impl;

import com.yanbao.dao.IndexBannersDao;
import com.yanbao.mapper.IndexBannerMapper;
import com.mall.model.IndexBanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2017年05月18日
 */
@Repository
public class IndexBannersDaoImpl implements IndexBannersDao {

    @Autowired
    private IndexBannerMapper indexBannerMapper;

    @Override
    public List<IndexBanner> getList() throws Exception {
        return indexBannerMapper.getList();
    }
}
