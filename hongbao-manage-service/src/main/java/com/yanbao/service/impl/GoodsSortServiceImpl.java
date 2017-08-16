package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.GoodsSortDao;
import com.mall.model.GoodsSort;
import com.yanbao.service.GoodsSortService;
import com.yanbao.vo.GoodsSortVo;

/**
 * Created by summer on 2016-12-20:17:31;
 */
@Service
public class GoodsSortServiceImpl extends CommonServiceImpl<GoodsSort> implements GoodsSortService {

    @Autowired
    GoodsSortDao goodsSortDao;

    @Override
    protected CommonDao<GoodsSort> getDao() {
        return goodsSortDao;
    }

    @Override
    protected Class<GoodsSort> getModelClass() {
        return GoodsSort.class;
    }

    @Override
    public List<GoodsSortVo> getGoodsSortVoListAll(GoodsSort goodsSort, Page page) {
        return goodsSortDao.getGoodsSortVoListAll(goodsSort,page);
    }
}
