package com.yanbao.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.Page;
import com.yanbao.dao.GoodsWinDao;
import com.yanbao.mapper.GoodsWinMapper;
import com.mall.model.GoodsWin;
import com.yanbao.redis.Strings;
import com.yanbao.vo.GoodsSalesVo;
import com.yanbao.vo.TopVo;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class GoodsWinDaoImpl implements GoodsWinDao {

	@Autowired
	private GoodsWinMapper goodsWinMapper;

	@SuppressWarnings("unchecked")
	@Override
	public List<TopVo> getTopList(Integer num) throws Exception {
		List<TopVo> list = null;
		String temp = Strings.get(RedisKey.HOME_TOP_LIST.getKey());
		if (null != temp) {
			list = (List<TopVo>) JSONArray.parse(temp);
		} else {
			list = goodsWinMapper.getTopList(num);
			Strings.setEx(RedisKey.HOME_TOP_LIST.getKey(), RedisKey.HOME_TOP_LIST.getSeconds(), JSONArray.toJSONString(list));
		}
		return list;
	}

	@Override
	public GoodsWin getByOrderNo(String orderNo, String userId) throws Exception {
		return goodsWinMapper.getByOrderNo(orderNo, userId);
	}

	@Override
	public GoodsWin getUserOrderByOrderNo(String orderNo) throws Exception {
		return goodsWinMapper.getUserOrderByOrderNo(orderNo);
	}

	@Override
	public GoodsWin getStoreOrderByOrderNo(String orderNo, String storeId) throws Exception {
		return goodsWinMapper.getStoreOrderByOrderNo(orderNo, storeId);
	}

	@Override
	public Integer count(String userId, Integer status,Integer orderType,String storeId) throws Exception {
		Integer count = null;
//		String temp = Strings.get(RedisKey.GOODS_WIN_COUNT.getKey() + userId + "_" + status);
//		if (null != temp) {
//			count = Integer.parseInt(temp);
//		} else {
			count = goodsWinMapper.count(userId, status,orderType,storeId);
//			Strings.setEx(RedisKey.GOODS_WIN_COUNT.getKey() + userId + "_" + status, RedisKey.GOODS_WIN_COUNT.getSeconds(), count.toString());
//		}
		if(count==null){
			count=0;
		}
		return count;
	}

	@Override
	public List<GoodsWin> getList(String userId, Integer status, Page page,Integer orderType,String storeId) throws Exception {
		return goodsWinMapper.getList(userId, status, page,orderType, storeId);
	}

	@Override
	public Integer add(GoodsWin model) throws Exception {
		return goodsWinMapper.add(model);
	}

	@Override
	public Integer update(String id, GoodsWin model) throws Exception {
		return goodsWinMapper.update(id, model);
	}

	@Override
	public Integer countStoreOrder(String storeId, Integer status) throws Exception {
		return goodsWinMapper.countStoreOrder(storeId, status);
	}

	@Override
	public List<GoodsWin> getStoreOrderList(String storeId, Integer status, Page page) throws Exception {
		return goodsWinMapper.getStoreOrderList(storeId, status, page);
	}

	@Override
	public Integer countStoreSales(String storeId) throws Exception {
		return goodsWinMapper.countStoreSales(storeId);
	}

	@Override
	public List<GoodsSalesVo> countStoreGoodsSales(String storeId) throws Exception {
		return goodsWinMapper.countStoreGoodsSales(storeId);
	}

	@Override
	public GoodsWin getById(String goodsWinId) throws Exception {
		return goodsWinMapper.getById(goodsWinId);
	}

	@Override
	public List<GoodsWin> getListByTime(Date starttime, Date endtime,
			Integer status, Integer payWay, String isShare) {
		return goodsWinMapper.getListByTime(starttime, endtime, status, payWay, isShare);
	}

	@Override
	public Double sumUserBuyAmt(String userId) throws Exception {
		return goodsWinMapper.sumUserBuyAmt(userId);
	}
}
