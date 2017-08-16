package com.yanbao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.GoodsWin;
import com.yanbao.vo.GoodsSalesVo;
import com.yanbao.vo.TopVo;

@Repository
public interface GoodsWinMapper {

	List<TopVo> getTopList(@Param("num") Integer num);

	GoodsWin getByOrderNo(@Param("orderNo") String orderNo, @Param("userId") String userId);

	GoodsWin getUserOrderByOrderNo(@Param("orderNo") String orderNo);

	GoodsWin getStoreOrderByOrderNo(@Param("orderNo") String orderNo, @Param("storeId") String storeId);

	Integer count(@Param("userId") String userId, @Param("status") Integer status, @Param("orderType") Integer orderType ,@Param("storeId") String  storeId );

	List<GoodsWin> getList(@Param("userId") String userId, @Param("status") Integer status, @Param("page") Page page, @Param("orderType") Integer orderType,@Param("storeId") String storeId);

	Integer add(@Param("model") GoodsWin model);

	Integer update(@Param("id") String id, @Param("model") GoodsWin model);

	Integer countStoreOrder(@Param("storeId") String storeId, @Param("status") Integer status);

	List<GoodsWin> getStoreOrderList(@Param("storeId") String storeId, @Param("status") Integer status, @Param("page") Page page);

	Integer countStoreSales(@Param("storeId") String storeId);

	List<GoodsSalesVo> countStoreGoodsSales(@Param("storeId") String storeId);

    GoodsWin getById(@Param("goodsWinId") String goodsWinId);
    
    List<GoodsWin> getListByTime(@Param("starttime")Date starttime,@Param("endtime")Date endtime,@Param("status")Integer status,@Param("payWay")Integer payWay,@Param("isShare")String isShare);

	Double sumUserBuyAmt(@Param("userId") String userId);
}
