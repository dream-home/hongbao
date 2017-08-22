package com.yanbao.mapper;

import java.util.List;

import com.yanbao.vo.GoodsSearchVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yanbao.core.page.Page;
import com.mall.model.Goods;

@Repository
public interface GoodsMapper {

	List<Goods> getRecommendList(@Param("num") Integer num);

	Goods getById(@Param("id") String id);

	Integer count(@Param("goodsSortId") String goodsSortId);

	List<Goods> getList(@Param("goodsSortId") String goodsSortId, @Param("page") Page page);

	List<Goods> getSearchList(@Param("goodsSearch") GoodsSearchVo goodsSearch, @Param("page") Page page) throws Exception;

	Integer add(@Param("model") Goods model);

	Integer update(@Param("id") String id, @Param("model") Goods model);

	Integer updateStock(@Param("id") String id, @Param("stock") Integer stock);

	Integer countStoreGoods(@Param("storeId") String storeId, @Param("status") Integer status);

	List<Goods> getStoreGoodsList(@Param("storeId") String storeId, @Param("status") Integer status, @Param("page") Page page);

	List<Goods> getEpList(@Param("goodsSortId")  String goodsSortId, @Param("page") Page page);
	
	Integer countEp(@Param("goodsSortId")  String goodsSortId);

	List<Goods> getIndexEpList(@Param("page") Page page);

	List<Goods> getNesGoodsList(@Param("maxRow") Integer maxRow);

	Integer updateSaleCount(@Param("id") String id, @Param("sumSaleCount") Integer sumSaleCount);

}
