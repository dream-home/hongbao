package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.model.GoodsDetail;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.GoodsDetailMapper;
import com.yanbao.service.GoodsDetailService;

/**
 * Created by summer on 2016-12-08:15:25;
 */
@Service
public class GoodsDetailServiceImpl extends CommonServiceImpl<GoodsDetail> implements GoodsDetailService {

    @Autowired
    GoodsDetailMapper goodsDetailDao;

    @Override
    protected CommonDao<GoodsDetail> getDao() {
        return goodsDetailDao;
    }

    @Override
    protected Class<GoodsDetail> getModelClass() {
        return GoodsDetail.class;
    }
    
}
