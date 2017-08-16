package com.yanbao.service;

import com.mall.model.IndexBanner;

import java.util.List;

/**
 * @date 2017年05月18日
 */
public interface IndexBannerService {

    /**
     * 首页banners
     */
    List<IndexBanner> getIndexBannerList() throws Exception;

}
