package com.yanbao.service.impl;

import com.yanbao.dao.GoodsWinDetailDao;
import com.mall.model.GoodsWinDetail;
import com.yanbao.service.GoodsWinDetailService;
import com.yanbao.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Jay.zheng
 * @date 2017年05月03日
 */
@Service
public class GoodsWinDetailServiceImpl implements GoodsWinDetailService {


    @Autowired
    private GoodsWinDetailDao goodsWinDetailDao;

    @Override
    public List<GoodsWinDetail> getListByGoodsWinId(String goodsWinId) throws Exception {
        return goodsWinDetailDao.getListByGoodsWinId(goodsWinId);
    }

    @Override
    public Integer add(GoodsWinDetail model) throws Exception {
        if (model == null) {
            return null;
        }
        model.setId(UUIDUtil.getUUID());
        model.setCreateTime(new Date());
        return goodsWinDetailDao.add(model);
    }

    @Override
    public Integer updateOrderNoByWinId(String newOrderNo, String goodsWinId) {
        return goodsWinDetailDao.updateOrderNoByWinId(newOrderNo, goodsWinId);
    }

    @Override
    public Integer updateByGoodsId(String goodsId, GoodsWinDetail goodsWinDetail) throws  Exception {
        return goodsWinDetailDao.updateByGoodsId(goodsId,goodsWinDetail);
    }
}
