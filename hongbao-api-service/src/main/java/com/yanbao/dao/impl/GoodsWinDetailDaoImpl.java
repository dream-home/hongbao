package com.yanbao.dao.impl;

import com.yanbao.dao.GoodsWinDetailDao;
import com.yanbao.mapper.GoodsWinDetailMapper;
import com.mall.model.GoodsWinDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jay.zheng
 * @date 2017年05月03日
 */
@Repository
public class GoodsWinDetailDaoImpl implements GoodsWinDetailDao {

    @Autowired
    private GoodsWinDetailMapper goodsWinDetailMapper;

    @Override
    public List<GoodsWinDetail> getListByGoodsWinId(String goodsWinId) throws Exception {
        if ("".equals(goodsWinId)) {
            return null;
        }
        return goodsWinDetailMapper.getListByGoodsWinId(goodsWinId);
    }

    @Override
    public Integer add(GoodsWinDetail model) throws Exception {
        return goodsWinDetailMapper.add(model);
    }

    @Override
    public Integer updateOrderNoByWinId(String newOrderNo, String goodsWinId) {
        return goodsWinDetailMapper.updateOrderNoByWinId(newOrderNo, goodsWinId);
    }

    @Override
   public Integer updateByGoodsId(String goodsId, GoodsWinDetail goodsWinDetail)   throws  Exception{
        return goodsWinDetailMapper.updateByGoodsId(goodsId, goodsWinDetail);
    }
}
