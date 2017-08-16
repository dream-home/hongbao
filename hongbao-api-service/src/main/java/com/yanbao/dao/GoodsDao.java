package com.yanbao.dao;

import java.util.List;

import com.yanbao.core.page.Page;
import com.mall.model.Goods;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface GoodsDao {

	List<Goods> getRecommendList(Integer num) throws Exception;

	Goods getById(String id) throws Exception;

	Integer count(String goodsSortId) throws Exception;

	List<Goods> getList(String goodsSortId, Page page) throws Exception;

	Integer add(Goods model) throws Exception;

	Integer update(String id, Goods model) throws Exception;

	Integer updateStock(String id, Integer stock) throws Exception;

	Integer countStoreGoods(String storeId, Integer status) throws Exception;

	List<Goods> getStoreGoodsList(String storeId, Integer status, Page page) throws Exception;

	List<Goods> getEpList(String goodsSortId,Page page) throws Exception;
	
	Integer countEp(String goodsSortId) throws Exception;
	
	List<Goods> getIndexEpList(Page page) throws Exception;

	List<Goods> getNesGoodsList(Integer maxRow) throws Exception;

    Integer updateSaleCount(String id, Integer sumSaleCount) throws Exception;
}
