package com.yanbao.dao;

import com.mall.model.IndexBanner;

import java.util.List;

/**
 * @date 2017年05月18日
 */
public interface IndexBannersDao {

    /**
     * 首页banners
     */
    List<IndexBanner> getList() throws Exception;
}
