package com.yanbao.dao.impl;

import com.yanbao.dao.IndexAdDao;
import com.yanbao.mapper.IndexAdMapper;
import com.mall.model.IndexAd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @date 2017年05月18日
 */
@Repository
public class IndexAdDaoImpl implements IndexAdDao {

    @Autowired
    private IndexAdMapper indexAdMapper;

    @Override
    public List<IndexAd> getList() throws Exception {
        return indexAdMapper.getList();
    }
}
