package com.yanbao.dao.impl;

import com.mall.model.Goods;
import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.Page;
import com.yanbao.dao.GoodsDao;
import com.yanbao.mapper.GoodsMapper;
import com.yanbao.redis.Strings;
import com.yanbao.vo.GoodsSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class GoodsDaoImpl implements GoodsDao {

	@Override
	public List<Goods> getIndexEpList(Page page) throws Exception {
		return goodsMapper.getIndexEpList(page);
	}

	@Autowired
	private GoodsMapper goodsMapper;

	@SuppressWarnings("unchecked")
	@Override
	public List<Goods> getRecommendList(Integer num) throws Exception {
		List<Goods> list = null;
		/*String temp = Strings.get(RedisKey.HOME_HOT_LIST.getKey());
		if (null != temp) {
			list = (List<Goods>) JSONArray.parse(temp);
		} else {
			Strings.setEx(RedisKey.HOME_HOT_LIST.getKey(), RedisKey.HOME_HOT_LIST.getSeconds(), JSONArray.toJSONString(list));
		}*/
		list = goodsMapper.getRecommendList(num);
		return list;
	}

	@Override
	public Goods getById(String id) throws Exception {
		return goodsMapper.getById(id);
	}

	@Override
	public Integer count(String goodsSortId) throws Exception {
		Integer count = null;
		String temp = Strings.get(RedisKey.GOODS_COUNT.getKey() + goodsSortId);
		if (null != temp) {
			count = Integer.parseInt(temp);
		} else {
			count = goodsMapper.count(goodsSortId);
			Strings.setEx(RedisKey.GOODS_COUNT.getKey() + goodsSortId, RedisKey.GOODS_COUNT.getSeconds(), count.toString());
		}
		return count;
	}

	@Override
	public List<Goods> getList(String goodsSortId, Page page) throws Exception {
		return goodsMapper.getList(goodsSortId, page);
	}

	@Override
	public Integer add(Goods model) throws Exception {
		return goodsMapper.add(model);
	}

	@Override
	public Integer update(String id, Goods model) throws Exception {
		return goodsMapper.update(id, model);
	}

	@Override
	public Integer updateStock(String id, Integer stock) throws Exception {
		return goodsMapper.updateStock(id, stock);
	}

	@Override
	public Integer countStoreGoods(String storeId, Integer status) throws Exception {
		return goodsMapper.countStoreGoods(storeId, status);
	}

	@Override
	public List<Goods> getStoreGoodsList(String storeId, Integer status, Page page) throws Exception {
		return goodsMapper.getStoreGoodsList(storeId, status, page);
	}

	@Override
	public List<Goods> getEpList(String goodsSortId,Page page) throws Exception {
		return goodsMapper.getEpList(goodsSortId,page);
	}

	@Override
	public Integer countEp(String goodsSortId) throws Exception {
		return goodsMapper.countEp(goodsSortId);
	}

	@Override
	public List<Goods> getNesGoodsList(Integer maxRow) throws Exception  {
		return goodsMapper.getNesGoodsList(maxRow);
	}

	@Override
	public Integer updateSaleCount(String id, Integer sumSaleCount) throws Exception {
		return goodsMapper.updateSaleCount(id, sumSaleCount);
	}

	@Override
	public List<Goods> getSearchList( GoodsSearchVo goodsSearch, Page page) throws Exception {
		return goodsMapper.getSearchList(goodsSearch, page);
	}
}
