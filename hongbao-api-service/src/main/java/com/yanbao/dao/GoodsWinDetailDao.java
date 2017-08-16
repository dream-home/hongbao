package com.yanbao.dao;

import com.mall.model.GoodsWinDetail;
import java.util.List;

/**
 * @author Jay.zheng
 * @date 2017年05月03日
 */
public interface GoodsWinDetailDao {

    List<GoodsWinDetail> getListByGoodsWinId(String goodsWinId) throws Exception;

    Integer add(GoodsWinDetail model) throws Exception;

    Integer updateOrderNoByWinId(String newOrderNo, String goodsWinId);

    Integer updateByGoodsId(String goodsId,GoodsWinDetail goodsWinDetail) throws  Exception;

}
