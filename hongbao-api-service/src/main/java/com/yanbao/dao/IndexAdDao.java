package com.yanbao.dao;

import com.mall.model.IndexAd;

import java.util.List;

/**
 * @date 2017年05月18日
 */
public interface IndexAdDao {

    /**
     * 获取首页6张广告列表
     */
    List<IndexAd> getList() throws Exception;
}
