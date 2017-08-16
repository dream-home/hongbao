package com.yanbao.service;

import com.mall.model.IndexAd;

import java.util.List;

/**
 * @date 2017年05月18日
 */
public interface IndexAdService {

    /**
     * 获取首页广告列表
     */
    List<IndexAd> getIndexAdList() throws Exception;

}
