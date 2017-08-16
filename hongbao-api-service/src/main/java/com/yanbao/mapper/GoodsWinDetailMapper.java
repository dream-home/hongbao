package com.yanbao.mapper;

import com.mall.model.GoodsWinDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsWinDetailMapper {

    List<GoodsWinDetail> getListByGoodsWinId(@Param("goodsWinId") String goodsWinId);

    Integer add(@Param("model") GoodsWinDetail model);

    Integer updateOrderNoByWinId(@Param("newOrderNo") String newOrderNo, @Param("goodsWinId") String goodsWinId);

    Integer updateByGoodsId(@Param("goodsId") String goodsId ,@Param("model")GoodsWinDetail goodsWinDetail);

}
