package com.yanbao.service;

import com.mall.model.Goods;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.vo.GoodsSearchVo;

import java.util.List;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
public interface GoodsService {

    List<Goods> getRecommendList(Integer num) throws Exception;

    Goods getById(String id) throws Exception;

    PageResult<Goods> getPage(String goodsSortId, Page page) throws Exception;

    PageResult<Goods> getSearchPage(GoodsSearchVo goodsSearch, Page page) throws Exception;

    Integer add(Goods model) throws Exception;

    Integer update(String id, Goods model) throws Exception;

    Integer updateStock(String id, Integer stock) throws Exception;

    PageResult<Goods> getStoreGoodsPage(String storeId, Integer status, Page page) throws Exception;

    Integer countStoreGoods(String storeId) throws Exception;

    PageResult<Goods> getEpPage(String goodsSortId, Page page) throws Exception;

    public void addNewIssue(Goods goods) throws Exception;

    PageResult<Goods> getIndexEpListPage(Page page) throws Exception;

    List<Goods> getNesGoodsList(Integer maxRow) throws Exception;

    Integer updateSaleCount(String id, Integer sumSaleCount) throws Exception;
}
