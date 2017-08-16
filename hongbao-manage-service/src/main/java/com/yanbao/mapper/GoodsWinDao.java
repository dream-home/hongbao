package com.yanbao.mapper;

import com.mall.model.GoodsWin;
import com.mall.model.GoodsWinDetail;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.page.Page;
import com.yanbao.vo.GoodsWinVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by summer on 2016-12-14:10:05;
 */
public interface GoodsWinDao extends CommonDao<GoodsWin> {

    Double getIncome();

    List<GoodsWinVo> readListWithTime(@Param("model") GoodsWinVo goodsWin, @Param("page") Page page);

    List<GoodsWinVo> comSaleList(@Param("model") GoodsWinVo goodsWin, @Param("page") Page page);

    Integer readCountWithTime(@Param("model") GoodsWinVo goodsWinVo, @Param("page") Page page);

    Integer readSaleCount(@Param("model") GoodsWinVo goodsWinVo, @Param("page") Page page);

    List<GoodsWinVo> readListWithTimeByStore(@Param("model") GoodsWinVo goodsWin, @Param("page") Page page);

    Integer readCountWithTimeByStore(@Param("model") GoodsWinVo goodsWinVo, @Param("page") Page page);

    List<GoodsWinDetail> shoppGoodsList(@Param("orderNo") String orderNo);

    Integer count(@Param("type") Integer orderType);
}
